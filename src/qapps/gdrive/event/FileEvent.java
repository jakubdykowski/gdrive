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
public class FileEvent extends EventObject {
    private final Type type;

    public FileEvent(AbstractFile file, Type type) {
        super(file);
        this.type = type;
    }

    public AbstractFile getFile() {
        return (AbstractFile) source;
    }
    
    public Type getType() {
        return this.type;
    }
    public static enum Type {
        PROPERTIES, DELETE, CLICK, DOUBLE_CLICK, RIGHT_CLICK, LIST, BACKWARD, DOWNLOAD
    }
}
