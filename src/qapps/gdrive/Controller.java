///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package qapps.gdrive;
//
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import java.util.concurrent.Executors;
//import qapps.gdrive.event.ErrorEvent;
//import qapps.gdrive.event.ErrorListener;
//import qapps.gdrive.event.ExploreEvent;
//import qapps.gdrive.event.ExploreListener;
//import qapps.gdrive.event.FileEvent;
//import qapps.gdrive.event.FileListener;
//
///**
// *
// * @author root
// */
//public class Controller {
//
//    private final Model model;
//    private final View view;
//    private final TransferManagerDerecated transferManager;
//
//    public Controller(Model m, View v, TransferManagerDerecated transferManager) {
//        this.model = m;
//        this.view = v;
//   
//        
//        //connection listener
//        model.addPropertyChangeListener("connected", new PropertyChangeListener() {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if (evt.getNewValue().equals(true)) {
//                    model.list(model.getRoot());
//                } else {
//                    view.showLogin();
//                }
//            }
//        });
//
//        //displays string representing status
//        model.addPropertyChangeListener("status", new PropertyChangeListener() {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if (evt.getNewValue() != null || !evt.getNewValue().equals("")) {
//                    view.setStatus(evt.getNewValue().toString());
//                }
//            }
//        });
//
//        //notifies gui about longer task to do
//        model.addPropertyChangeListener("busy", new PropertyChangeListener() {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                System.err.println(evt.getNewValue());
//                if (evt.getNewValue().equals(true)) {
////                    view.setIndetermiante(true);
//                    view.showLoad();
//                } else {
////                    view.setIndetermiante(false);
////                    view.setStatus("");
//                }
//            }
//        });
//
//        //notifies gui when listing directories
//        model.addExploreListener(new ExploreListener() {
//
//            @Override
//            public void explore(ExploreEvent evt) {
////                System.out.println(evt);
//                if (evt.isFinished()) {
//                    view.showExplorator();
//                } else if (evt.isStarted()) {
//                    view.explore(null);
//                } else if (evt.isAdded()) {
//                    view.explore(evt.getFile());
//                }
//            }
//        });
//
//        //global error listener with popping up dialog with an error
//        model.addErrorListener(new ErrorListener() {
//
//            @Override
//            public void error(ErrorEvent evt) {
//                view.showError(evt.getException().toString(), evt.getType());
//            }
//        });
//
//        //adding error listener when not connected
//        final ErrorListener loginError = new LoginError();
//        model.addErrorListener(loginError);
//        model.addPropertyChangeListener("connected", new PropertyChangeListener() {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if (evt.getNewValue().equals(true)) {
//                    model.removeErrorListener(loginError);
//                } else if (evt.getNewValue().equals(false)) {
//                    model.addErrorListener(loginError);
//                }
//            }
//        });
//
//        view.addFileListener(new FileListener() {
//
//            @Override
//            public void action(FileEvent evt) {
//                if (evt.getSource() == null) {
//                    return;
//                }
//                switch (evt.getType()) {
//                    case LIST: {
//                        model.list(evt.getFile());
//                        break;
//                    }
//                    case BACKWARD: {
//                        model.backward(evt.getFile());
//                        break;
//                    }
//                    case DOUBLE_CLICK: {
//                        if (!evt.getFile().isFile()) {
//                            model.list(evt.getFile());
//                        }
//                        break;
//                    }
//                    case PROPERTIES: {
//                        view.showProperties(evt.getFile());
//                    }
//                }
//            }
//        });
//        this.transferManager = transferManager;
//        
//    }
//
//    private class PCL implements PropertyChangeListener {
//
//        @Override
//        public void propertyChange(PropertyChangeEvent evt) {
//            System.out.println(evt);
//        }
//    }
//
//    private class TUI implements PropertyChangeListener {
//
//        @Override
//        public void propertyChange(PropertyChangeEvent evt) {
//            if (evt.getPropertyName().equals("busy") && evt.getNewValue().equals(true)) {
//                System.out.println("loading...");
//            } else if (evt.getPropertyName().equals("status")) {
//                System.out.println(evt.getNewValue());
//            }
//        }
//    }
//
//    private class LoginError implements ErrorListener {
//
//        @Override
//        public void error(ErrorEvent evt) {
//            view.showLogin();
//        }
//    }
//}
