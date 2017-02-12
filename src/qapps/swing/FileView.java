/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.swing;

import javax.swing.Icon;

/**
 *
 * @author root
 */
public interface FileView<E> {

    void add(E file);

    FileLabel<E> remove(E file);

    void clear();

    IconHandler getIconHandler();

    void setIconHandler(IconHandler iconHandler);

    /**
     * The best thing is that icons don't rally have to exists int handler, like in container, they can be dynamically loaded from disk, exerything depends on implementation.
     * @author root
     */
    public static interface IconHandler {

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

    /**
     * The way to obtain name therefore to obtain icon from IconHandler
     * @param <E> 
     */
    public static interface IconIdentifier<E> {

        String name(E file);
    }
}
