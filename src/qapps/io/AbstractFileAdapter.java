/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import qapps.io.AbstractFile;
import qapps.io.AbstractFile;

/**
 *
 * @author root
 */
public class AbstractFileAdapter implements AbstractFile {

    private final File file;

    public AbstractFileAdapter(File file) {
        this.file = file;
    }

    public AbstractFileAdapter(String absolutePath) {
        this(new File(absolutePath));
    }

    @Override
    public AbstractFile delete() throws IOException {
        return file.delete() ? this : null;
    }

    @Override
    public AbstractFile add(String string, InputStream in) throws IOException {
        File child = new File(file, string);
        return child.createNewFile() ? new AbstractFileAdapter(child) : null;
    }

    @Override
    public AbstractFile add(String string) throws IOException {
        File child = new File(file, string);
        if (child.exists()) {
            throw new IOException("cannot create" + file.getAbsolutePath() + "cause already exists");
        }
        if (child.mkdir()) {
            return new AbstractFileAdapter(child);
        }
        return null;
    }

    @Override
    public AbstractFile[] list() throws IOException {
        File[] files = file.listFiles();
        AbstractFile[] result = new AbstractFile[files.length];
        for (int i = 0; i < files.length; i++) {
            result[i] = new AbstractFileAdapter(files[i]);
        }
        return result;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getPath() {
        return file.getAbsolutePath();
    }

    @Override
    public long getSize() throws IOException {
        return file.length();
    }

    @Override
    public AbstractFile getParent() throws IOException {
        return new AbstractFileAdapter(file.getParentFile());
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public boolean exists() throws IOException {
        return file.exists();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public int compareTo(AbstractFile o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractFileAdapter)) {
            return false;
        }
        AbstractFileAdapter other = (AbstractFileAdapter) obj;
        return file.equals(other.file);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.file != null ? this.file.hashCode() : 0);
        return hash;
    }
}
