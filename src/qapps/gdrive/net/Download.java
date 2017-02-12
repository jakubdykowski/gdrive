/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.net;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import qapps.io.AbstractFile;

/**
 * ThreadSafe class.
 * Can be called repeatedly but not simultaneously.
 * After exception can be called again.
 * @author qba
 */
public class Download extends Transfer {

    private volatile boolean stop = false;
    private volatile CountDownLatch latch = null;
    private final Lock lock = new ReentrantLock();
    private final AbstractFile source;
    private final File output;

    public Download(AbstractFile source, File output, long size) {
        super(size);
        if (source == null || output == null) {
            throw new NullPointerException("null argument");
        }
        this.source = source;
        this.output = output;
    }

    /**
     * After interrupting transfer is started from beginning.
     */
    @Override
    public void run() {
        //prevent from running simultaneously the same job
        try {
            lock.lock();
            if (getState() == State.PENDING) {
                throw new IllegalStateException("Already running or terminating");
            }
            stop = false;
            latch = new CountDownLatch(1);
            setState(State.PENDING);
        } finally {
            lock.unlock();
        }

        boolean finished = false;
        try (ReadableByteChannel in = Channels.newChannel(source.getInputStream());
                FileChannel out = new FileOutputStream(output).getChannel();
                FileLock fileLock = out.tryLock()) {
            if (fileLock == null) {
                throw new IllegalAccessException(output.getAbsolutePath() + " is used by another process");
            }
            ByteBuffer buff = ByteBuffer.allocate(1024 * 100);
            while (in.read(buff) != -1) {
                buff.flip();
                out.write(buff);
                pcs.firePropertyChange("progress", position, position += buff.position());
                if (stop) {
                    return;
                }
            }
            finished = true;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            //resources are released automatilly
            latch.countDown();
            if (finished) {
                setState(State.DONE);
            } else {
                setState(State.STOPPED);
            }
        }
    }

    /**
     * ThreadSafe.
     * Blocks unitil task is stopped.
     * This an ensurance that task is stopped.
     * @throws InterruptedException 
     */
    @Override
    public void stop() throws InterruptedException {
        try {
            //prevent for simultaneously starting/stopping
            lock.lock();

            if (getState() == State.PENDING) {
                this.stop = true;
            } else {
                return;
            }
            latch.await();
        } finally {
            lock.unlock();
        }
    }
}
