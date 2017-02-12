/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.gui;

import java.awt.Component;

/**
 *
 * @author root
 */
public interface IdleIndicator {

    boolean busy();

    boolean relieve();

    void setStatus(String status);
}
