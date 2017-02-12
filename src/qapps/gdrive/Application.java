///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package qapps.gdrive;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.lang.reflect.InvocationTargetException;
//import java.util.Properties;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.prefs.Preferences;
//import javax.swing.JOptionPane;
//import javax.swing.UIManager;
//import qapps.io.drive.Drive;
//
///**
// *
// * @author root
// */
//public class Application {
//
//    private static final Application app = new Application();
//
//    public static Application getApplication() {
//        return app;
//    }
//    private boolean log = true;
//    private Preferences prefs;
//    private Drive drive;
//    private Properties settings;
//    private Model model;
//    private View view;
//    private Controller controller;
//
//    {
//        //setting native look & feel
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, ex.toString(), "Błąd", JOptionPane.ERROR_MESSAGE);
//        }
//
//        try {
//            //init preferences
//            prefs = Preferences.userNodeForPackage(this.getClass());
//
//            //init drive
//            drive = new Drive();
//
//            //init app logic here
//            model = new Model(drive);
//
////            Account ac = new Account(drive);
//            
//            //initializing properties
//            settings = new java.util.Properties();
//            settings.load(this.getClass().getResourceAsStream("resources/settings.properties"));
//
//            //initializing graphics
//            initGraphics();
//
//            //downloads / uploads threads manager
//            ExecutorService transferExecutor = Executors.newCachedThreadPool();
//            TransferManagerDerecated tManager = new TransferManagerDerecated(transferExecutor);
//            
//            //init controller
//            controller = new Controller(model, view, tManager);
//
//            if (view
//                    == null) {
//                throw new RuntimeException("Interfejs graficzny nie zainicjowany");
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace(System.out);
//            StringWriter sw = new StringWriter();
//            PrintWriter ps = new PrintWriter(sw);
//            ex.printStackTrace(ps);
//            JOptionPane.showMessageDialog(null, sw, "Błąd krytyczny - startup", JOptionPane.ERROR_MESSAGE);
//            System.exit(1);
//        }
//
//    }
//
//    public void show() {
//        view.show();
//    }
//
//    public void hide() {
//        view.hide();
//    }
//
//    public synchronized void shutdown() {
//        System.out.println("zamykanie programu...");
//        System.exit(0);
////        throw new UnsupportedOperationException("Not supported yet");
//        //turned off in early implementation
////        try {
////            prefs.flush();
////        } catch (BackingStoreException ex) {
////            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
////        }
//    }
//
//    public Preferences getPreferences() {
//        return prefs;
//    }
//
//    private void initGraphics() throws InterruptedException, InvocationTargetException {
////        SwingUtilities.invokeAndWait(new Runnable() {
////
////            @Override
////            public void run() {
////                try {
////                    System.out.println(5.5);
//                    view = new View(model, settings);
////                } catch (Exception ex) {
////                    JOptionPane.showMessageDialog(
////                            null, ex, "Błąd tworzenia interfejsu graficznego", JOptionPane.ERROR_MESSAGE);
////                    System.exit(1);
////                }
////            }
////        });
//    }
//}
