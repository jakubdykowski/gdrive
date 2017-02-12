/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive;

import java.io.IOException;
import java.util.concurrent.Executors;
import qapps.gdrive.util.Transfer;
import qapps.io.drive.AuthenticationException;

/**
 *
 * @author root
 */
public class Test {

    public static void main(String[] args) throws IOException, AuthenticationException {
//        FilePanel<AbstractFile> panel = new FilePanel<AbstractFile>();
//        JFrame frame = new JFrame("hello");
//        frame.setSize(640, 400);
//        frame.setVisible(true);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        frame.add(panel);
//        AbstractFile root = new AbstractFileAdapter(new File("/home/qba"));
//        for (AbstractFile child : root.list()) {
//            panel.add(child);
//        }
//        System.out.println(new Drive().login("gdrive1502@gmail.com", "19691426"));
        new TransferTest(Executors.newCachedThreadPool()) {
//
//            @Override
//            public void transfer() {
//                System.out.println("started");
//            }
//
//            @Override
//            public void done() {
//                System.out.println("done");
//            }
//            
            
        }.execute();

    }
}
