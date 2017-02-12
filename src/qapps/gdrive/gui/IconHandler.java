/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.gui;

import javax.swing.Icon;

/**
 *
 * @author root
 */
public interface IconHandler {

        /**
         * return icon by its unique name
         * @param name representing the icon
         * @return icon or null if not found
         */
        Icon getIcon(String name);

        /**
         * Returns array containing unigue names
         * @return set of icon names
         */
        String[] names();

        /**
         * Gives array of all icons associated with this handler
         * @return icons
         */
        Icon[] icons();
    }
