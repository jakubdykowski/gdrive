/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import qapps.gdrive.event.TransferEvent;
import qapps.gdrive.event.TransferListener;
import qapps.gdrive.util.Download;
import qapps.gdrive.util.ListenerSupport;
import qapps.gdrive.util.Transfer;
import qapps.gdrive.util.Upload;
import qapps.io.AbstractFile;

/**
 *
 * @author root
 */
public class TransferManagerDerecated {

    private volatile boolean autoStart = Boolean.valueOf(false);
    private final ExecutorService executor;
    private final ListenerSupport ls = new ListenerSupport(ListenerSupport.Type.AWT_EVENTQUEUE);
    private final Collection<Transfer> transfers = new HashSet<>(10);

    public TransferManagerDerecated(ExecutorService executor) {
        if (executor == null) {
            throw new NullPointerException();
        }
        this.executor = executor;
    }

    public void addTransferListener(TransferListener l) {
        ls.add(TransferListener.class, l);
    }

    public void removeTransferListener(TransferListener l) {
        ls.remove(TransferListener.class, l);
    }

    public Transfer setupDownload(AbstractFile file, File output) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("directories not supported");
        }
        Transfer transfer = null;
        try {
            transfer = new Download(file, output, executor);
        } catch (IOException ex) {
        }
        //there events means download finished but it has to mean: download is removed from manager
//        transfer.addPropertyChangeListener("state", new FireFinished(transfer));
        if (transfers.add(transfer)) {
            ls.fire(TransferListener.class, "transfer", new TransferEvent(transfer, true, true));
            return transfer;
        }
        return null;
    }

    public Transfer setupDownload(Transfer transfer) {
        ls.fire(TransferListener.class, "transfer", new TransferEvent(transfer, true, true));
        return transfer;
    }

    public Transfer setupUpload(File file, AbstractFile folder) {
        Transfer transfer = null;
        transfer = new Upload(file, folder, executor) {
        };
        if (transfers.add(transfer)) {
            ls.fire(TransferListener.class, "transfer", new TransferEvent(transfer, false, true));
            return transfer;
        }
        return null;

    }

    public synchronized Transfer[] getTransfers() {
        return transfers.toArray(new Transfer[transfers.size()]);
    }

    public synchronized void shutdown(boolean force) {
        if (force) {
            executor.shutdownNow();
            return;
        }
        for (Transfer t : transfers) {
            t.cancel();
        }
        transfers.clear();
        //testing
        for (Transfer t : transfers) {
            System.err.println(t + " is still runing");
        }
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    private class FireFinished implements PropertyChangeListener {

        private final Transfer transfer;

        public FireFinished(Transfer transfer) {
            this.transfer = transfer;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue() == Transfer.State.DONE) {
                ls.fire(TransferListener.class, "transfer", new TransferEvent(transfer, true, false));
            }
        }
    }
}
