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
public interface DriveListener extends EventListener {
    void change(DriveEvent evt);
}
