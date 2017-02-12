/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.swing;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JPanel;
import qapps.event.CountListener;
import qapps.event.CountSupport;

/**
 * Class giving feature to present set of file, like an explorer, trying to simplify as much as it is possible.
 * @author root
 */
public class FilePanel<E> extends JPanel implements FileView<E> {

    private final CountSupport<FileLabel<E>> countSupport = new CountSupport<FileLabel<E>>();
    private IconHandler iconHandler;
    private IconIdentifier<E> iconStrategy;
    private FileLabel<E> selected = null;

    public FilePanel(IconHandler iHandler, IconIdentifier iStrategy) {
        super();
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.addContainerListener(new ContainerListener() {

            @Override
            public void componentAdded(ContainerEvent e) {
                if (e.getChild() instanceof FileLabel) {
                    FileLabel<E> label = (FileLabel<E>) e.getChild();
//                    System.out.println("FilePanel: added: " + label);
//                    labels.add(label);
                    countSupport.fireAdded(label);
                }
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                if (e.getChild() instanceof FileLabel) {
                    FileLabel<E> label = (FileLabel<E>) e.getChild();
//                    System.out.println("FilePanel: removed: " + label);
//                    labels.remove(label);
                    countSupport.fireRemoved(label);
                }
            }
        });
        this.iconHandler = iHandler;
        this.iconStrategy = iStrategy;
    }

    public FilePanel(IconHandler iHandler) {
        this(iHandler, null);
        this.iconStrategy = new DefaultIconStrategy();
    }

    public FilePanel() {
        this(null, null);
        this.iconStrategy = new DefaultIconStrategy();
    }

    @Override
    public void add(final E file) {
        Icon icon = null;
        if (iconHandler != null && iconStrategy != null) {
            icon = iconHandler.getIcon(iconStrategy.name(file));
        }
        final FileLabel label = new FileLabel(file, icon);
        label.addFocusListener(new FocusListener(){

            @Override
            public void focusGained(FocusEvent e) {
                synchronized(FilePanel.this) {
                    selected = label;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                synchronized(FilePanel.this) {
                    selected = null;
                }
            }
            
        });
        add(label);
    }

    public synchronized IconIdentifier<E> getIconStrategy() {
        return iconStrategy;
    }

    public synchronized void setIconStrategy(IconIdentifier<E> iconStrategy) {
        this.iconStrategy = iconStrategy;
    }

    /**
     * 
     * @param file
     * @return component that has been removes, null otherwise
     */
    @Override
    public FileLabel<E> remove(E file) {
        //way with FileLabels container
//        for(FileLabel<E> label : labels) {
//            if(label.file.equals(file)) {
//                this.remove(label);
//                labels.remove(label);
//                break;
//            }
//        }

        //way without separate FileLabels container
        for (Component component : getComponents()) {
            if (component instanceof FileLabel) {
                FileLabel label = (FileLabel) component;
                if (label.file.equals(file)) {
                    this.remove(component);
                    return label;
                }
            }
        }
        return null;
    }

    /**
     * removes all Compionents from this panel
     */
    @Override
    public void clear() {
        this.removeAll();
    }
    
    public synchronized List<E> getCurrent() {
        Component[] cmps = this.getComponents();
        List<E> list = new LinkedList<E>();
        for(Component c : cmps) {
            try {   
            if(c instanceof FileLabel) list.add(((FileLabel<E>)c).getFile());
            } catch(Exception ex) {}
        }
        return list;
    }
    
    public synchronized FileLabel<E> getSelected() {
        return selected;
    }

    @Override
    public synchronized IconHandler getIconHandler() {
        return iconHandler;
    }

    @Override
    public synchronized void setIconHandler(IconHandler iconHandler) {
        this.iconHandler = iconHandler;
    }

    /**
     * Fires component added just before actually he is added so you can prepare him to that, set some preferences
     * @param cl
     * @return 
     */
    public boolean addCountListener(CountListener<FileLabel<E>> cl) {
        return countSupport.addCountListener(cl);
    }

    public boolean removeCountListener(CountListener<FileLabel<E>> cl) {
        return countSupport.removeCountListener(cl);
    }

    public void removeAllCountListeners() {
        countSupport.clear();
    }

    private class DefaultIconStrategy implements IconIdentifier<E> {

        @Override
        public String name(E file) {
            try {
                String s = file.toString();
                if (!s.contains(".")) {
                    return "default";
                }
                return s.substring(s.lastIndexOf('.') + 1);
            } catch (Exception ex) {
                return null;
            }
        }
    }
}
