///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package qapps.gdrive;
//
//import java.awt.Component;
//import java.awt.Container;
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//
///**
// *
// * @author root
// */
//public class ContentController  implements PropertyChangeListener{
//    private final Account ac;
//    private final Container content;
//    private final Component explorer;
//    private final Component login;
//    private final Component loading;
//
//    public ContentController(
//            Account ac,
//            Container content,
//            Component explorer,
//            Component login,
//            Component loading) {
//        this.ac = ac;
//        this.content = content;
//        this.explorer = explorer;
//        this.login = login;
//        this.loading = loading;
//    }
//
//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        if(evt.getPropertyName().equals("logged")) {
//            if(evt.getNewValue().equals(false)) {
//                content.removeAll();
//                content.add(login);
//                content.validate();
//                content.repaint();
//            }
//        } else if(evt.getPropertyName().equals("directory")) {
//            if(evt.getNewValue() != null) {
//                content.removeAll();
//                content.add(explorer);
//                content.validate();
//                content.repaint();
//            } 
//        }
//    }
//    
//    
//}
