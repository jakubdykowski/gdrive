/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.event;

import java.util.EventObject;
import qapps.gdrive.util.Transfer;

/**
 *
 * @author root
 */
public class TransferEvent extends EventObject {

    private final boolean isDownload;
    private final boolean isStarted;

    public TransferEvent(Transfer source, boolean isDownload, boolean isStarted) {
        super(source);
        this.isDownload = isDownload;
        this.isStarted = isStarted;
    }

    public boolean isDownload() {
        return this.isDownload;
    }

    public boolean isUpload() {
        return !this.isDownload;
    }

    public boolean isStarted() {
        return this.isStarted;
    }
    
    public boolean isFinished() {
        return !this.isStarted;
    }
    
    public Transfer getTransfer() {
        return (Transfer) this.source;
    }
}
