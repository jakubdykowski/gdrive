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
public interface ErrorListener extends EventListener {

    void error(ErrorEvent evt);
}
