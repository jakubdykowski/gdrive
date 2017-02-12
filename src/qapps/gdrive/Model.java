///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package qapps.gdrive;
//
//import java.beans.PropertyChangeListener;
//import java.beans.PropertyChangeSupport;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import javax.mail.event.ConnectionEvent;
//import javax.swing.SwingUtilities;
//import qapps.event.CountListener;
//import qapps.gdrive.event.DriveEvent;
//import qapps.gdrive.event.DriveListener;
//import qapps.gdrive.event.ErrorEvent;
//import qapps.gdrive.event.ErrorListener;
//import qapps.gdrive.event.ExploreEvent;
//import qapps.gdrive.event.ExploreListener;
//import qapps.gdrive.event.TransferEvent;
//import qapps.gdrive.event.TransferListener;
//import qapps.gdrive.util.ListenerSupport;
//import qapps.gdrive.util.ListenerSupport.Type;
//import qapps.gdrive.util.ThreadedPCS;
//import qapps.io.AbstractFile;
//import qapps.io.drive.AuthenticationException;
//import qapps.io.drive.Drive;
//import qapps.io.drive.event.ConnectionListener;
//
///**
// *
// * @author root
// */
//public class Model {
//
//    private ExecutorService eventQueue = Executors.newSingleThreadExecutor();
//    private ListenerSupport ls = new ListenerSupport(eventQueue);
//    private PropertyChangeSupport pcs = new ThreadedPCS(this, eventQueue);
//    private AbstractFile current = null;
//    private Drive drive;
//    private AbstractFile root;
//    private AbstractFile[] children;
//
//    public Model(Drive drive) {
//        this.drive = drive;
//        drive.addConnectionListener(new ConnectedListener());
//        drive.addCountListener(new DriveBridge());
//    }
//    private boolean busy = false;
//    private volatile boolean connected;
//    private String status = "";
//
//    public synchronized void list(AbstractFile parent) {
//        try {
////            setBusy(true);
//            setStatus("pobieranie listy plików...");
////            fireExplore(new ExploreEvent(parent, true));
//            AbstractFile[] childs = parent.list();
//            System.out.println("current == " + Arrays.toString(childs));
//            setChildren(childs, parent);
//            setCurrent(parent);
//            System.out.println("model: list.end");
//        } catch (IOException ex) {
//            setChildren(null, null);
//            fireError(new ErrorEvent(ex));
//        } finally {
////            setBusy(false);
//            fireExplore(new ExploreEvent(parent, false));
//        }
//    }
//
//    public synchronized void backward(AbstractFile file) {
//        setBusy(true);
//        setStatus("pobieranie nadrzędnego folderu...");
//        try {
//            list(file.getParent());
//        } catch (IOException ex) {
//            fireError(new ErrorEvent(ex));
//            setBusy(false);
//        }
//    }
//
//    public synchronized void login(String login, String passwd) {
////        setBusy(true);
//        System.out.println("model: login(" + login + ", " + passwd + ")");
//        try {
//            setStatus("logowanie...");
//            drive.login(login, passwd);
//        } catch (AuthenticationException ex) {
//            fireError(new ErrorEvent(ex));
//        } catch (IOException ex) {
//            fireError(new ErrorEvent(ex));
//        } finally {
//            setStatus("");
////            setBusy(false);
//        }
//    }
//
//    public synchronized void delete(AbstractFile file) {
//        setBusy(true);
//        try {
//            setStatus("usuwanie...");
//            file.delete();
//        } catch (IOException ex) {
//            fireError(new ErrorEvent(ex));
//        } finally {
//            setBusy(false);
//            setStatus("");
//        }
//    }
//
//    public AbstractFile getRoot() {
//        return root;
//    }
//
//    private int count(AbstractFile file) throws IOException {
//        int count = 0;
//        if (!file.isFile()) {
//            AbstractFile[] childs = file.list();
//            for (AbstractFile child : childs) {
//                if (!child.isFile()) {
//                    count += count(child);
//                } else {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }
//
//    public void addErrorListener(ErrorListener l) {
//        ls.add(ErrorListener.class, l);
//    }
//
//    public void removeErrorListener(ErrorListener l) {
//        ls.remove(ErrorListener.class, l);
//    }
//
//    public void addExploreListener(ExploreListener l) {
//        ls.add(ExploreListener.class, l);
////        listenerList.add(ExploreListener.class, l);
//    }
//
//    public void removeExploreListener(ExploreListener l) {
//        ls.remove(ExploreListener.class, l);
////        listenerList.remove(ExploreListener.class, l);
//    }
//
//    public void addDriveListener(DriveListener l) {
//        ls.add(DriveListener.class, l);
//    }
//
//    public void removeDriveListener(DriveListener l) {
//        ls.remove(DriveListener.class, l);
//    }
//
//    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
//        this.pcs.addPropertyChangeListener(propertyName, listener);
//    }
//
//    public void addPropertyChangeListener(PropertyChangeListener listener) {
//        this.pcs.addPropertyChangeListener(listener);
//    }
//
//    public void addTransferListener(TransferListener l) {
//        ls.add(TransferListener.class, l);
//    }
//
//    public void removeTreansferListener(TransferListener l) {
//        ls.remove(TransferListener.class, l);
//    }
//
//    private void fireError(ErrorEvent evt) {
//        ls.fire(ErrorListener.class, "error", evt);
//    }
//
//    private void fireExplore(ExploreEvent evt) {
//        ls.fire(ExploreListener.class, "explore", evt);
//    }
//
//    private void fireDrive(DriveEvent evt) {
//        ls.fire(DriveListener.class, "change", evt);
////        }
//    }
//
//    private void fireTransfer(TransferEvent evt) {
//        ls.fire(TransferListener.class, "transfer", evt);
//    }
//
//    private synchronized void setStatus(String status) {
////        System.err.println("setStatus(" + status + ")");
//        pcs.firePropertyChange("status", this.status, this.status = status);
//    }
//
//    public synchronized AbstractFile getCurrent() {
//        return this.current;
//    }
//
//    private synchronized void setCurrent(AbstractFile file) {
//        AbstractFile old = this.current;
//        this.current = file;
//        if (file == null) {
//            setChildren(null, null);
//        }
//        pcs.firePropertyChange("current", old, file);
//    }
//
//    private synchronized void setChildren(AbstractFile[] childs, AbstractFile parent) {
//        if (children != null) {
//            for (AbstractFile child : this.children) {
//                fireExplore(new ExploreEvent(child, false, parent));
//            }
//        }
//        this.children = childs;
//        for (AbstractFile child : this.children) {
//            fireExplore(new ExploreEvent(child, true, parent));
//        }
//    }
//
//    private synchronized AbstractFile[] getChildren() {
//        return this.children;
//    }
//
//    private synchronized void setBusy(boolean busy) {
//        System.out.println("setBusy(" + busy + ")");
//        pcs.firePropertyChange("busy", this.busy, this.busy = busy);
//    }
//
//    private synchronized void setConnected(boolean connected) {
//        pcs.firePropertyChange("connected", this.connected, this.connected = connected);
//    }
//
//    private class ConnectedListener implements ConnectionListener {
//
//        @Override
//        public void opened(ConnectionEvent ce) {
//            synchronized (Model.this) {
//                try {
//                    root = drive.getRoot();
//                } catch (IOException ex) {
//                    fireError(new ErrorEvent(ex));
//                    setConnected(false);
//                    return;
//                }
//            }
//            setConnected(true);
//        }
//
//        @Override
//        public void disconnected(ConnectionEvent ce) {
//            setConnected(false);
//        }
//
//        @Override
//        public void closed(ConnectionEvent ce) {
//            setConnected(false);
//        }
//    }
//
//    private class DriveBridge implements CountListener<AbstractFile> {
//
//        @Override
//        public void removed(final AbstractFile e) {
//            SwingUtilities.invokeLater(new Runnable() {
//
//                @Override
//                public void run() {
//                    fireDrive(new DriveEvent(e, false));
//                }
//            });
//        }
//
//        @Override
//        public void added(final AbstractFile e) {
//            SwingUtilities.invokeLater(new Runnable() {
//
//                @Override
//                public void run() {
//                    fireDrive(new DriveEvent(e, true));
//                }
//            });
//        }
//    }
//}
