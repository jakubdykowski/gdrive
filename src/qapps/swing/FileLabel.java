/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.swing;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author root
 */
public class FileLabel<E> extends JLabel {

    final E file;

    public FileLabel(E file, Icon icon) {
        super("<html>" + file.toString(), icon, JLabel.CENTER);
        this.file = file;

        //just temporary
        this.setPreferredSize(new Dimension(100, 100));

        this.setVerticalTextPosition(JLabel.BOTTOM);
        this.setHorizontalTextPosition(JLabel.CENTER);
        this.setFocusable(true);

        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
//                if (e.getComponent().isFocusable()) {
                    e.getComponent().requestFocusInWindow();
//                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

    }

    public E getFile() {
        return file;
    }
}
