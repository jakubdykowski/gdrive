/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.gui;

import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import qapps.swing.FileView;

/**
 * Icons are dynamically loaded from disk and for more efficiency cached using HashMap.
 * @author root
 */
public class CachedIconManager implements IconHandler, FileView.IconHandler {

    private Map<String, Icon> cache = new WeakHashMap<String, Icon>();
    private File folder;

    /**
     * Created IconHandler with given file as a folder containing only icons which are to be used by this handler
     * @throws IllegalArgumentException - when given folder is null or doesn't exists
     */
    public CachedIconManager(File folder) {
        if (folder == null || !folder.exists()) {
            throw new IllegalArgumentException("given folder is null or doesn't exists");
        }
        this.folder = folder;
    }

    @Override
    public synchronized Icon getIcon(String name) {
        Icon icon = cache.get(name);
        if (icon != null) {
            return icon;
        }
        File file = new File(folder, name);
//        System.out.print("pobieranie ikony.. " + file.getAbsolutePath());
        if (file.exists()) {
            icon = new ImageIcon(file.getAbsolutePath(), name);
            cache.put(name, icon);
        }
//        System.out.println(" done");
        return icon;
    }

    @Override
    public synchronized String[] names() {
        return folder.list();
    }

    @Override
    public synchronized Icon[] icons() {
        File[] children = folder.listFiles();
        LinkedList<Icon> icons = new LinkedList();
        for (File file : children) {
            try {
                icons.add(getIcon(file.getAbsolutePath()));
            } catch (Exception ex) {
            }
        }
        return icons.toArray(new Icon[icons.size()]);
    }
}
