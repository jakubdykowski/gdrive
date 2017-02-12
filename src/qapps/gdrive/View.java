///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package qapps.gdrive;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.FocusEvent;
//import java.awt.event.FocusListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.io.File;
//import java.io.IOException;
//import java.util.Properties;
//import javax.swing.AbstractAction;
//import javax.swing.JButton;
//import javax.swing.JComponent;
//import javax.swing.JFrame;
//import javax.swing.JMenuItem;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JPopupMenu;
//import javax.swing.JProgressBar;
//import javax.swing.JToolBar;
//import javax.swing.SwingUtilities;
//import javax.swing.event.EventListenerList;
//import qapps.event.CountListener;
//import qapps.gdrive.event.DriveEvent;
//import qapps.gdrive.event.DriveListener;
//import qapps.gdrive.event.FileEvent;
//import qapps.gdrive.event.FileEvent.Type;
//import qapps.gdrive.event.FileListener;
//import qapps.gdrive.gui.DefaultIconIdentifier;
//import qapps.gdrive.gui.AutoCenterPanel;
//import qapps.gdrive.gui.CachedIconManager;
//import qapps.gdrive.gui.IdleIndicator;
//import qapps.gdrive.gui.LoginPanel;
//import qapps.gdrive.gui.ToolBar;
//import qapps.io.AbstractFile;
//import qapps.swing.FileLabel;
//import qapps.swing.FilePanel;
//import qapps.swing.FileView.IconHandler;
//
///**
// *
// * @author root
// */
//public final class View {
//
//    private final Model model;
//    private final Properties props;
//    private final EventListenerList listenerList;
//    private final JFrame window;
//    private final JPanel content;
//    private final FilePanel<AbstractFile> filePanel;
//    private final LoginPanel loginPanel;
//    private final ProgressBarIndicator idle;
//    private final JToolBar toolBar;
//    private final IconHandler iconHandler;
//
//    public View(Model m, Properties prop) {
//        this.model = m;
//        props = new DefaultProps();
//        this.props.putAll(prop);
//        listenerList = new EventListenerList();
//
//        idle = new ProgressBarIndicator(
//                Integer.parseInt(props.getProperty("loading.width")), Integer.parseInt(props.getProperty("loading.height")));
//        idle.progressBar.setIndeterminate(true);
//        idle.setBackground(Color.WHITE);
//        window = new JFrame();
//        window.setTitle(props.getProperty("window.title", "null"));
//        window.setSize(
//                new Integer(props.getProperty("window.width")), new Integer(props.getProperty("window.height")));
//        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//
//        //operation on window closing
//        window.addWindowListener(new WindowAdapter() {
//
//            @Override
//            public void windowClosing(WindowEvent e) {
//                Application.getApplication().shutdown();
//            }
//        });
//        window.addMouseListener(new MouseAdapter() {
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                window.requestFocusInWindow();
//            }
//        });
//
//        //initilizing icons
//        File folder = new File(getClass().getResource(props.getProperty("icons.folder")).getFile());
//        iconHandler = new CachedIconManager(folder);
//
//        filePanel = new FilePanel<AbstractFile>();
//        filePanel.setIconHandler(iconHandler);
////        filePanel.setIconStrategy(DefaultIconIdentifier.get());
//        filePanel.setOpaque(true);
//        filePanel.setBackground(Color.WHITE);
//
//        //thing on highlight or clicked on FileLabel
//        filePanel.addCountListener(new CountListener<FileLabel<AbstractFile>>() {
//
//            @Override
//            public void removed(FileLabel<AbstractFile> e) {
//            }
//
//            @Override
//            public void added(final FileLabel<AbstractFile> label) {
//                label.setOpaque(true);
//                label.setBackground(Color.WHITE);
//                label.setComponentPopupMenu(new FilePopup(label.getFile()));
//                //focus highlight
//                label.addFocusListener(new FocusListener() {
//
//                    @Override
//                    public void focusGained(FocusEvent e) {
//                        label.setBackground(Color.ORANGE);
//                    }
//
//                    @Override
//                    public void focusLost(FocusEvent e) {
//                        label.setBackground(Color.WHITE);
//                    }
//                });
//                label.add(new JPopupMenu());
//                label.addMouseListener(new MouseAdapter() {
//
//                    @Override
//                    public void mousePressed(MouseEvent e) {
//                        if (SwingUtilities.isLeftMouseButton(e)) {
//                            System.out.println("s left click");
//                            if (e.getClickCount() > 1) {
//                                fireFile(new FileEvent(label.getFile(), label.getFile().isFile() ? Type.DOWNLOAD : Type.LIST));
//                            } else {
//                                fireFile(new FileEvent(label.getFile(), Type.CLICK));
//                            }
//                        }
//                        if(e.isPopupTrigger()) {
//                            System.out.println("popuptrigger");
//                        }
//                    }
//                });
//            }
//        });
//
//        toolBar = new ToolBar(m, filePanel, iconHandler);
//
//        //add or remove files from explorer when drive change
//        model.addDriveListener(new DriveListener() {
//
//            @Override
//            public void change(DriveEvent evt) {
//                try {
//                    if (evt.getSource() == null) {
//                        System.err.println("NULL source DriveEvent");
//                        return;
//                    }
//                    if (model.getCurrent() == evt.getFile().getParent()) {
//                        if (evt.isAdded()) {
//                            filePanel.add(evt.getFile());
//                        } else {
//                            filePanel.remove(evt.getFile());
//                        }
//                    }
//                } catch (IOException ex) {
//                    showError(ex.toString(), "Błąd");
//                }
//            }
//        });
//
//        //init toolBar
//        Component[] cs = toolBar.getComponents();
//        for (Component c : cs) {
//            if (c instanceof JButton) {
//                ((JButton) c).addActionListener(new ButtonsActionListener());
//            }
//        }
////        final JTextField tf = new JTextField();
////        toolBar.add(tf);
////        toolBar.add(new JButton(new AbstractAction("enable") {
////
////            @Override
////            public void actionPerformed(ActionEvent e) {
////                Component[] cs = toolBar.getComponents();
////                for (Component c : cs) {
////                    if (c instanceof JButton) {
////                        if (((JButton) c).getActionCommand().equals("delete")) {
////                            c.setEnabled(true);
////                        }
////                    }
////                }
////            }
////        }));
////        toolBar.add(new JButton(new AbstractAction("disable") {
////
////            @Override
////            public void actionPerformed(ActionEvent e) {
////                Component[] cs = toolBar.getComponents();
////                for (Component c : cs) {
////                    if (c instanceof JButton) {
////                        if (((JButton) c).getActionCommand().equals("delete")) {
////                            c.setEnabled(false);
////                        }
////                    }
////                }
////            }
////        }));
//
//        //init login panel
//        loginPanel = new LoginPanel();
//        ActionListener loginAction = new LoginAction();
//        loginPanel.login.addActionListener(loginAction);
//        loginPanel.setBackground(Color.WHITE);
//
//        //build and pack window
//        content = new JPanel();
//        content.setOpaque(true);
//        content.setLayout(new BorderLayout());
//        content.setBackground(Color.RED);
//        showLogin();
//
//        window.setBackground(Color.BLUE);
//        window.getContentPane().add(toolBar, BorderLayout.NORTH);
//        window.getContentPane().add(content, BorderLayout.CENTER);
//    }
//
//    public void show() {
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                window.setVisible(true);
//            }
//        });
//    }
//
//    public void hide() {
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                window.setVisible(false);
//            }
//        });
//    }
//
//    public void showExplorator() {
//        content.removeAll();
////        JScrollPane sp = new javax.swing.JScrollPane(
////                filePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
////        sp.setOpaque(true);
//        content.add(filePanel);
//        content.validate();
//        content.repaint();
//    }
//
//    public void showLogin() {
//        content.removeAll();
//        Component center = new AutoCenterPanel(loginPanel);
//        center.setBackground(Color.WHITE);
//        content.add(center);
//        content.validate();
//        content.repaint();
//    }
//
//    public void showLoad() {
//        content.removeAll();
//        content.add(idle);
//        content.validate();
//        content.repaint();
//    }
//
//    public void showError(String message, String title) {
//        JOptionPane.showMessageDialog(window, message, title, JOptionPane.ERROR_MESSAGE);
//    }
//
//    public void showProperties(AbstractFile file) {
//        StringBuilder desc = new StringBuilder();
//        System.out.println("=======================>");
//        desc.append("ścieżka:");
//        desc.append(file.getPath());
//        desc.append("\n");
//        try {
//            desc.append("rozmiar: ");
//            desc.append(file.getSize());
//        } catch (IOException ex) {
//            System.out.println("ERROR");
//        } finally {
//            desc.append("\n");
//        }
//        desc.append("plik?: ");
//        desc.append(file.isFile());
//        desc.append("\n");
//        JOptionPane.showMessageDialog(window, desc, "Właściwości", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    public synchronized void explore(final AbstractFile file) {
//        if (file == null) {
//            filePanel.clear();
//            return;
//        }
////        System.out.println("view: explore(" + file.getPath() + ")");
//
//        filePanel.add(file);
//
////        filePanel.validate();
////        filePanel.repaint();
//    }
//
//    public void setStatus(final String status) {
////        SwingUtilities.invokeLater(new Runnable() {
////
////            @Override
////            public void run() {
////        System.out.println("view: setStatus(" + status + ")");
//        idle.setStatus(status);
////            }
////            
////        });
//    }
//
//    public void setIndetermiante(boolean i) {
//        idle.progressBar.setIndeterminate(i);
//    }
//
//    public void addFileListener(FileListener l) {
//        listenerList.add(FileListener.class, l);
//    }
//
//    public void removeFileListener(FileListener l) {
//        listenerList.add(FileListener.class, l);
//    }
//
//    private void fireFile(FileEvent evt) {
//        Object[] listeners = listenerList.getListenerList();
//        for (int i = 0; i < listeners.length; i += 2) {
//            if (listeners[i] instanceof FileListener) {
//                ((FileListener) listeners[i + 1]).action(evt);
//            }
//        }
//    }
//
//    private class DefaultProps extends Properties {
//
//        @Override
//        public String getProperty(String key) {
//            String result = super.getProperty(key);
//            return result != null ? result : "null";
//        }
//    }
//
//    private class ProgressBarIndicator extends AutoCenterPanel implements IdleIndicator {
//
//        private JProgressBar progressBar = new JProgressBar();
//        private int status = 0;
//
//        public ProgressBarIndicator(int w, int h) {
//            progressBar.setStringPainted(true);
//            progressBar.setString("");
//            progressBar.setOpaque(true);
//            progressBar.setPreferredSize(new Dimension(w, h));
//            this.add(progressBar);
//            //unnecessary
////            this.setLayout(new AbsoluteLayout());
////            this.setOpaque(true);
////            this.add(progressBar, new AbsoluteConstraints(1, 1, w, h));
//
//        }
//        private final Runnable busy = new Runnable() {
//
//            @Override
//            public void run() {
//                content.removeAll();
//                content.add(ProgressBarIndicator.this, BorderLayout.CENTER);
//                content.repaint();
//                content.validate();
//            }
//        };
//
//        @Override
//        public synchronized boolean busy() {
//            if (++status == 1) {
//                progressBar.setIndeterminate(true);
//                if (SwingUtilities.isEventDispatchThread()) {
//                    System.out.println("running in current thread");
//                    busy.run();
//                } else {
//                    System.out.println("running in dispatch thread");
//                    SwingUtilities.invokeLater(busy);
//                }
//                return true;
//            }
//            return false;
////            System.out.println("busy: " + status);
//        }
//
//        @Override
//        public synchronized boolean relieve() {
//            if (--status < 1) {
//                status = 0;
//                Runnable relieve = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        progressBar.setIndeterminate(false);
//                        content.removeAll();
//                        content.validate();
//                        content.repaint();
//                        setStatus("");
//                    }
//                };
//                if (SwingUtilities.isEventDispatchThread()) {
//                    System.out.println("running in current thread");
//                    relieve.run();
//                } else {
//                    System.out.println("running in dispatch thread");
//                    SwingUtilities.invokeLater(relieve);
//                }
//                return true;
//            }
//            return false;
////            System.out.println("busy:" + status);
//        }
//
//        @Override
//        public synchronized void setStatus(String status) {
//            progressBar.setString(status);
//        }
//    }
//
//    private class LoginAction extends AbstractAction {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            showLoad();
//            new Thread() {
//
//                @Override
//                public void run() {
//                    model.login(loginPanel.getUsername(), loginPanel.getPassword());
//                }
//            }.start();
//        }
//    }
//
//    private class ButtonsActionListener implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            String action = e.getActionCommand();
//            System.out.println("clicked: " + action);
//            if (action.equals("previous")) {
//                fireFile(new FileEvent(model.getCurrent(), Type.BACKWARD));
//            } else if (action.equals("refresh")) {
//                fireFile(new FileEvent(model.getCurrent(), Type.LIST));
//            } else if (action.equals("delete")) {
//                AbstractFile selected = filePanel.getSelected().getFile();
//                if (selected == null) {
//                    return;
//                }
//                model.delete(selected);
//            } else if (action.equals("mkdir")) {
//            }
//        }
//    }
//
//    private class FilePopup extends JPopupMenu {
//
//        private final AbstractFile file;
//
//        public FilePopup(AbstractFile aFile) {
//            super("menu kontekstowe pliku");
//            this.file = aFile;
//
//            JMenuItem item;
//
//            if (file.isFile()) {
//                item = new JMenuItem("Pobierz");
//                item.setAction(new AbstractAction("download") {
//
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        View.this.fireFile(new FileEvent(file, FileEvent.Type.DOWNLOAD));
//                    }
//                });
//                add(item);
//            } else if(!file.isFile()) {
//                item = new JMenuItem("Otwórz");
//                item.setAction(new AbstractAction("list") {
//
//                    @Override
//                    public void actionPerformed(ActionEvent e) {
//                        View.this.fireFile(new FileEvent(file, FileEvent.Type.LIST));
//                    }
//                });
//                add(item);
//            }
//
//            item = new javax.swing.JMenuItem("Usuń");
//            item.setAction(new AbstractAction("delete") {
//
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    View.this.fireFile(new FileEvent(file, FileEvent.Type.DELETE));
//                }
//            });
//            add(item);
//
//            item = new JMenuItem("Właściwości");
//            item.setAction(new AbstractAction("properties") {
//
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    View.this.fireFile(new FileEvent(file, FileEvent.Type.PROPERTIES));
//                }
//            });
//            add(item);
//        }
//    }
//}
