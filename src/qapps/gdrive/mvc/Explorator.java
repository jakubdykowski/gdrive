/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.mvc;

import javax.swing.AbstractListModel;
import qapps.io.AbstractFile;
import qapps.io.drive.Drive;

/**
 *
 * @author root
 */
public class Explorator extends AbstractListModel {

    private Drive drive;
    private AbstractFile current;
    private AbstractFile[] files;
    private AbstractFile root;

    public Explorator() {
        
    }
    
    public Explorator(Drive drive) {
        this.drive = drive;
    }

    public synchronized AbstractFile getRoot() {
        return root;
    }

    public synchronized AbstractFile[] getFiles() {
        return files;
    }

    public synchronized AbstractFile current() {
        return current;
    }

    public synchronized void setRoot(AbstractFile root) {
        this.root = root;
    }

    public synchronized void explore(AbstractFile file, AbstractFile[] files) {
        if (file == null || files == null) {
            throw new IllegalArgumentException("argument file or files is NULL");
        }
        this.current = file;
        this.files = files;
        this.fireContentsChanged(this, 0, files.length);
//        this.letKnow();
    }

    @Override
    public synchronized int getSize() {
        return files.length;
    }

    @Override
    public synchronized Object getElementAt(int index) {
        return files[index];
    }
}
