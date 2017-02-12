/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import qapps.io.AbstractFile;

/**
 *
 * @author root
 */
public class RandomAbstractFile {
    private static final String[] data = new File("/").list();
    private static Random rand = new Random(System.currentTimeMillis());
    private static int pos = 0;
    public static synchronized AbstractFile generate() {
        return new AbstractFile() {

            private String path;
            private int size;
            private boolean isFile;
            private AbstractFile parent;
            private boolean exists;
            {
                path = data[pos++];
                if(pos == data.length) {
                    pos = 0;
                }
                size = rand.nextInt(9999);
                isFile = rand.nextBoolean();
                exists = !(rand.nextInt(10) == 0);
            }
            @Override
            public AbstractFile delete() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public AbstractFile add(String string, InputStream in) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public AbstractFile add(String string) throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public AbstractFile[] list() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getName() {
                return path.substring(path.lastIndexOf('/') + 1);
            }

            @Override
            public String getPath() {
                return path;
            }

            @Override
            public long getSize() throws IOException {
                return size;
            }

            @Override
            public AbstractFile getParent() throws IOException {
                return parent != null ? parent : (parent = generate());
            }

            @Override
            public boolean isFile() {
                return isFile;
            }

            @Override
            public boolean exists() throws IOException {
                return exists;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int compareTo(AbstractFile o) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            
        };
    }
}
