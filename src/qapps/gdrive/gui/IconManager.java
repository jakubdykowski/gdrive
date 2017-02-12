/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.gui;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.Icon;
import qapps.swing.FileView;

/**
 * Object that constains icons where each of them are represented by unique name. When name repeats old icon is replaced by new one. The best thing is that icons don't rally have to exists int handler, like in container, they can be dynamically loaded from disk, exerything depends on implementation.
 * @author root
 */
public class IconManager implements FileView.IconHandler {

    protected HashMap<String, Icon> icons = new HashMap<String, Icon>();

    /**
     * Creates empty IconManager
     */
    public IconManager() {
    }

    /**
     * Implementation is based on a HashMap containing map of icons. Creates IconManager initailly filled by given icons array with appropriate names array (same size)
     * @param names array of names (can't repeats, older will be replaced)
     * @param icons array of icons (can repeats)
     */
    public IconManager(String[] names, Icon[] icons) {
        for (int i = 0; i < names.length; i++) {
            try {
                if (names[i] != null && icons[i] != null) {
                    this.icons.put(names[i], icons[i]);
                }
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Creates IconManager initially filled by given map of icons
     * @param icons map of icons
     */
    public IconManager(Map<String, Icon> icons) {
        this.icons.putAll(icons);
    }

    @Override
    public Icon getIcon(String name) {
        return icons.get(name);
    }

    @Override
    public String[] names() {
        Set<String> names = icons.keySet();
        return names.toArray(new String[names.size()]);
    }

    @Override
    public Icon[] icons() {
        Collection<Icon> collection = this.icons.values();
        return collection.toArray(new Icon[icons.size()]);
    }

    /**
     * Adds given icon with representing name.
     * @param name non null unique String
     * @param icon non null Icon
     * @return 
     */
    public Icon add(String name, Icon icon) {
        return icons.put(name, icon);
    }

    /**
     * Removes icon represented by given name.
     * @param name
     * @return 
     */
    public Icon remove(String name) {
        return icons.remove(name);
    }

    /**
     * Removes all icons.
     */
    public void clear() {
        icons.clear();
    }
}
