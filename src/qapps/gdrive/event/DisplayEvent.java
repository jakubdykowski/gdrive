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
public class DisplayEvent extends EventObject {
    DisplayEvent(AbstractFile source) {
        super(source);
    }
    public AbstractFile getFile() {
        return (AbstractFile) this.source;
    }
}
