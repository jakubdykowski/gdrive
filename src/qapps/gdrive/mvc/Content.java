///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package qapps.gdrive.mvc;
//
//import qapps.pattern.mvc.SwingView;
//import java.awt.Component;
//import java.awt.Container;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import qapps.gdrive.Account.State;
//
///**
// *
// * @author root
// */
//public class Content extends SwingView<Status> {
//
//    private final Container panel;
//    private final Component explorer;
//    private final Component login;
//    private final Component loading;
//
//    public Content(SwingView explorer, SwingView login, SwingView loading) {
//        this(null, explorer, login, loading);
//    }
//
//    public Content(Status model, SwingView explorer, SwingView login, SwingView loading) {
//        super(model, new JPanel());
//        this.panel = (Container) getComponent();
//        this.add(explorer);
//        this.explorer = explorer.getComponent();
//        this.add(login);
//        this.login = login.getComponent();
//        this.add(loading);
//        this.loading = loading.getComponent();
//    }
//
//    @Override
//    public void refresh() {
//        panel.removeAll();
//        State state = getModel().getState();
//        switch (getModel().getState()) {
//            case LOADING:
//                panel.add(login);
//                break;
//            case EXPLORE:
//                panel.add(explorer);
//                break;
//            case LOGIN:
//                panel.add(loading);
//                break;
//            default:
//                panel.add(new JLabel("not found apropriate component for state: " + state));
//        }
//        panel.validate();
//        panel.repaint();
//
//        super.refresh();
//    }
//
//    @Override
//    protected void addNotifiers(Status model) {
//        model.add(callback());
//    }
//}
