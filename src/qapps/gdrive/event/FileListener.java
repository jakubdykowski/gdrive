/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.event;

import java.util.EventListener;

/**
 *
 * @author root
 */
public interface FileListener extends EventListener {
    void action(FileEvent evt);
}
