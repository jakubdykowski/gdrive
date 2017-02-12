///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package qapps.gdrive.gui;
//
//import java.beans.PropertyChangeEvent;
//import java.beans.PropertyChangeListener;
//import javax.swing.JToolBar;
////import qapps.gdrive.Model;
//
///**
// *
// * @author root
// */
//public class DriveToolBar extends JToolBar {
//    public DriveToolBar(Model model) {
//        model.addPropertyChangeListener("connected", new PropertyChangeListener() {
//
//            @Override
//            public void propertyChange(PropertyChangeEvent evt) {
//                if(evt.getNewValue().equals(false)) {
//                    DriveToolBar.this.setEnabled(false);
//                } else {
//                    DriveToolBar.this.setEnabled(true);
//                }
//            }
//            
//        });
//    }
//}
