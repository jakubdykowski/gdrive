/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;

/**
 * This panel gives feature that allow to auto center the containing component. Note that this Container can hold just one component
 * @author root
 */
public class AutoCenterPanel extends JPanel {

    private Component component;
    private volatile boolean changed = true;

    public AutoCenterPanel() {
    }

    public AutoCenterPanel(Component comp) {
        this.add(comp);
    }

    {
//        this.setLayout(new BorderLayout());
        this.addComponentListener(new ComponentListener() {

            @Override
            public void componentResized(ComponentEvent e) {
                center();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        if (component != null) {
            this.remove(component);
        }
//        changed = true;
        component = comp;
        super.addImpl(comp, constraints, index);
    }

    @Override
    public void invalidate() {
        super.validate();
        center();
    }

    private void center() {
        int x = (this.getWidth() - component.getWidth()) / 2;
        int y = (this.getHeight() - component.getHeight()) / 2;
        component.move(x, y);
    }
}
