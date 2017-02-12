/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import qapps.io.AbstractFile;

/**
 *
 * @author root
 */
public class Upload extends Transfer {

    private final File file;
    private InputStream input;
    private final AbstractFile folder;

    public Upload(File file, AbstractFile folder, ExecutorService executor) {
        super(file.getName(), (int) file.length(), executor);
        this.file = file;
        this.folder = folder;
    }

    @Override
    public void transfer() {
        try {
            setStatus("inicjalizowanie");
            input = new MonitoredInput(new FileInputStream(file));
            setStatus("wysyłanie");
            folder.add(file.getName(), input);
        } catch (Exception ex) {
            Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
            setStatus(ex.getMessage());
        }
    }

    @Override
    public void done() {
        //close streams
        try {
            input.close();
        } catch (Exception ex) {
        }
    }

    @Override
    public void cancel() {
        if(getState() == State.DONE) throw new IllegalStateException("executed and finished already");
        this.isCancelled = true;
    }

    private class MonitoredInput extends InputStream {

        private final InputStream input;

        public MonitoredInput(InputStream input) {
            this.input = input;
        }

        @Override
        public int read() throws IOException {
            if (isCancelled) {
                setStatus("anulowano");
                Thread.currentThread().interrupt();
            }
            checkPaused();
            int b = input.read();
            addProgress(1);
            return b;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            checkPaused();
            int bytes = input.read(b, off, len);
            addProgress(bytes);
            return bytes;
        }
    }
}
