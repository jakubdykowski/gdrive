/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import qapps.io.AbstractFile;

/**
 *
 * @author root
 */
public class Download extends Transfer {

    private static final Properties props = new Properties();
    private static final int BUFF_SIZE = 1024 * 50;

    public static Object put(Object key, Object value) {
        return props.put(key, value);
    }
    private final AbstractFile file;
    private final File out;
    private ReadableByteChannel inC;
    private WritableByteChannel outC;

    public synchronized void putAll(Map<? extends Object, ? extends Object> t) {
        props.putAll(t);
    }

    public Download(AbstractFile file, File output, ExecutorService executor) throws IOException {
        super(file.getName(), (int) file.getSize(), executor);
        this.file = file;
        this.out = output;
    }

    @Override
    public void transfer() {
        setStatus("inicjowanie");
        ByteBuffer buff = ByteBuffer.allocate(BUFF_SIZE);
        int bytesRead = 0;
        try {
            outC = Channels.newChannel(new FileOutputStream(out));
            inC = Channels.newChannel(file.getInputStream());
            setStatus(null);
            do {
                if (isCancelled) {
                    setStatus("anulowano");
                    return;
                }
                checkPaused();
                buff.clear();
                bytesRead = inC.read(buff);
                buff.flip();
                addProgress(outC.write(buff));
            } while (bytesRead > -1);
            setProgress(100);
            setStatus("zakończono");
        } catch (IOException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
            setStatus("error: " + ex.getMessage());
        }
    }

    @Override
    public void done() {
        try {
            outC.close();
        } catch (IOException ex) {
        }
        try {
            inC.close();
        } catch (IOException ex) {
        }
    }

    @Override
    public synchronized void cancel() {
        if(getState() == State.DONE) {
            throw new IllegalStateException("executed and finished already");
        }
        this.isCancelled = true;
    }
    
}
