/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.event;

import java.util.EventObject;
import qapps.io.AbstractFile;

/**
 *
 * @author root
 */
public class DriveEvent extends EventObject {
    private final boolean added;
    public DriveEvent(AbstractFile file, boolean added) {
        super(file);
        this.added = added;
    }
    
    public AbstractFile getFile() {
        return (AbstractFile) source;
    }
    
    public boolean isAdded() {
        return this.added;
    }
    
    public boolean isRemoved() {
        return !this.added;
    }
}
