/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import qapps.io.AbstractFile;

/**
 *
 * @author root
 */
public class FileRenderer extends JLabel implements ListCellRenderer {

    private final IconHandler manager;
    private final IconIdentifier identifier;

    public FileRenderer(IconHandler manager, IconIdentifier identifier) {
        if (manager == null || identifier == null) {
            throw new NullPointerException("manager or identifier argument is null");
        }
        this.manager = manager;
        this.identifier = identifier;
        setOpaque(true);
        setVerticalTextPosition(JLabel.BOTTOM);
        setHorizontalTextPosition(JLabel.CENTER);
        this.setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof AbstractFile) {
            setIcon(manager.getIcon(identifier.identify(value)));
            AbstractFile file = (AbstractFile) value;
            setText("<html>" + file.getName());
            setToolTipText(file.getPath());
            if(isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(Color.WHITE); 
            }
        }
        return this;
    }
}
