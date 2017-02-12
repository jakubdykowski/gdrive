///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package qapps.gdrive.mvc;
//
//import qapps.gdrive.Account;
//import qapps.pattern.mvc.SwingView;
//import java.awt.BorderLayout;
//import java.awt.Component;
//import java.util.Properties;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import qapps.gdrive.gui.LoginPanel;
//import qapps.io.drive.Drive;
//import qapps.pattern.mvc.View;
//import qapps.swing.FilePanel;
//
///**
// *
// * @author root
// */
//public class Window extends SwingView<Application> {
//    
//    private final JFrame frame;
//
//    public Window(Properties props) {
//        super(new JFrame());
//        this.frame = new JFrame(props.getProperty("window.title"));
//        this.frame.setSize(Integer.parseInt(props.getProperty("window.width", "640"))
//                , Integer.parseInt(props.getProperty("window.height", "400")) );
//    
//        SwingView view = new Explorer(new Explorator(new Drive()));
//        View content = new Content(new Account(), view, view, view);
//        this.add(content);
//    }
//
//    @Override
//    public void refresh() {
//        frame.validate();
//        frame.repaint();
//        super.refresh();
//    }
//    @Override
//    protected void addNotifiers(Application model) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//}
