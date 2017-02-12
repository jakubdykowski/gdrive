/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive;

import java.util.ArrayList;
import javax.swing.event.EventListenerList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import qapps.gdrive.net.Transfer;
import static qapps.gdrive.net.Transfer.State;

/**
 * ThreadSafe.
 * @author qba
 */
public class TransferManager {

    private EventListenerList listeners = new EventListenerList();
    private Lock lock = new ReentrantLock();
    private List<Transfer> transfers = new ArrayList<>();
    private final ExecutorService service;

    public TransferManager(ExecutorService service) {
        this.service = service;
    }

    public ExecutorService getService() {
        return service;
    }

    public void add(Transfer transfer, boolean execute) {
        try {
            lock.lock();
            if (!transfers.contains(transfer)) {
                //fire that new transfer occured
                if (transfers.add(transfer)) {
                    this.fireTransfer(transfer, true, transfers.size() - 1);
                }
            } else {
                //transfer already exists
            }
            State state = transfer.getState();
            if (state != State.PENDING) {
                //propably execute or inform that is ready to execute
                if (execute) {
                    service.execute(transfer);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void remove(Transfer transfer) {
        try {
            lock.lock();
            int index = transfers.indexOf(transfer);
            if (index >= 0) {
                if (transfers.remove(index) != null) {
                    this.fireTransfer(transfer, false, index);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Return array copy
     * @return 
     */
    public Transfer[] getTransfers() {
        try {
            lock.lock();
            return transfers.toArray(new Transfer[transfers.size()]);
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        try {
            lock.lock();
            return transfers.size();
        } finally {
            lock.unlock();
        }
    }

    public ExecutorService getExecutorService() {
        return this.service;
    }

    private void fireTransfer(Transfer transfer, boolean added, int index) {
        for (TransferListener l : listeners.getListeners(TransferListener.class)) {
            l.transfer(transfer, added, index);
        }
    }

    public void connect(TransferListener l) {
        listeners.add(TransferListener.class, l);
    }

    public void disconnect(TransferListener l) {
        listeners.remove(TransferListener.class, l);
    }

    public interface TransferListener extends EventListener {

        void transfer(Transfer transfer, boolean added, int index);
    }
}
