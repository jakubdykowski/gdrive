/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import qapps.gdrive.util.Transfer;

/**
 *
 * @author root
 */
public class TransferTest extends Transfer {

    private static Random rand = new Random(47);
    private static int count = 0;

    public TransferTest(ExecutorService executor) {
        super("Transfer.id=" + count++, rand.nextInt(4000), executor);
    }

    @Override
    public void transfer() {
//        System.out.println(getName() + " started");
        setStatus("inicjalizowanie");
        int portion = rand.nextInt(60);
        
        setStatus("pobieranie");
        do {
            try {
                portion = rand.nextInt(60);
                TimeUnit.MILLISECONDS.sleep(500);
                addProgress(portion);
            } catch (InterruptedException ex) {
                setStatus(ex.getMessage());
                ex.printStackTrace(System.out);
            }
        } while(getProgress() < getSize());
//        System.out.println(getName() + " finished");
    }

    @Override
    public void done() {
        setStatus("pobrano");
//        System.out.println(this.getName() + " done()");
    }
}
