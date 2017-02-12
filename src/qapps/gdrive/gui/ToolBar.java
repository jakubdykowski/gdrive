///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package qapps.gdrive.gui;
//
//import java.awt.Component;
//import java.awt.event.FocusEvent;
//import java.awt.event.FocusListener;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import java.util.LinkedList;
//import java.util.List;
//import javax.swing.JButton;
//import javax.swing.JToolBar;
//import qapps.event.CountListener;
//import qapps.swing.FilePanel;
//import qapps.swing.FileView.IconHandler;
//
///**
// *
// * @author root
// */
//public class ToolBar extends JToolBar {
//
//    private final JButton[] buttons;
//    private final Model model;
//    private FilePanel filePanel;
//
//    public ToolBar(Model model, FilePanel filePanel, IconHandler iconHandler) {
//        super(JToolBar.HORIZONTAL);
//        this.setSize(400, 40);
//
//        JButton button = new JButton();
//        button.setIcon(iconHandler.getIcon("previous"));
//        button.setActionCommand("previous");
//        this.add(button);
//
//        button = new JButton();
//        button.setIcon(iconHandler.getIcon("remove"));
//        button.setActionCommand("delete");
//        button.setEnabled(false);
//        this.add(button);
//        focusListener = new TurnOnFocus(button);
//
//        button = new JButton();
//        button.setIcon(iconHandler.getIcon("folder"));
//        button.setActionCommand("mkdir");
//        this.add(button);
//
//        button = new JButton();
//        button.setIcon(iconHandler.getIcon("add"));
//        button.setActionCommand("add");
//        this.add(button);
//
//        button = new JButton();
//        button.setIcon(iconHandler.getIcon("refresh"));
//        button.setActionCommand("refresh");
//        this.add(button);
//
//        List<JButton> btns = new LinkedList<JButton>();
//        for (Component component : getComponents()) {
//            if (component instanceof JButton) {
//                btns.add((JButton) component);
//                component.setEnabled(false);
//            }
//        }
//        this.buttons = btns.toArray(new JButton[btns.size()]);
//
//        this.filePanel = filePanel;
//        this.model = model;
//        model.addPropertyChangeListener("connected", connectedPCS);
//        filePanel.addCountListener(new CountListener() {
//
//            @Override
//            public void removed(Object e) {
//                if (e instanceof Component) {
//                    Component component = (Component) e;
//                    component.removeFocusListener(focusListener);
//                }
//            }
//
//            @Override
//            public void added(Object e) {
//                if (e instanceof Component) {
//                    Component component = (Component) e;
//                    component.addFocusListener(focusListener);
//                }
//            }
//        });
//
//    }
//
//    public FilePanel setFilePanel(FilePanel filePanel) {
//        FilePanel old = this.filePanel;
//        this.filePanel = filePanel;
//        return old;
//    }
//    private PropertyChangeListener connectedPCS = new ConnectedPCS();
//
//    private class ConnectedPCS implements PropertyChangeListener {
//
//        @Override
//        public void propertyChange(PropertyChangeEvent evt) {
//            if (evt.getNewValue().equals(true)) {
//                System.out.println("enable");
//                for (JButton button : buttons) {
//                    if (!button.getActionCommand().equals("delete"))  {
//                        button.setEnabled(true);
//                    }
//                }
//            } else {
//                System.out.println("disable");
//                for (JButton button : buttons) {
//                        button.setEnabled(false);
//                }
//            }
//        }
//    }
//    private FocusListener focusListener;
//
//    private class TurnOnFocus implements FocusListener {
//
//        private final Component c;
//
//        public TurnOnFocus(Component c) {
//            this.c = c;
//        }
//
//        @Override
//        public void focusGained(FocusEvent e) {
//            c.setEnabled(true);
//        }
//
//        @Override
//        public void focusLost(FocusEvent e) {
//            c.setEnabled(false);
//        }
//    }
//}
