/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import qapps.gdrive.util.ThreadedPCS.Exec;


/**
 *
 * @author root
 */
public abstract class Transfer implements Runnable {

    private Executor executor;
    private PropertyChangeSupport pcs = new ThreadedPCS(this, Exec.AWT_EVENT_QUEUE);
    private String status = "";
    private int progress = 0;
    private State state = State.NEW;
    private volatile boolean executed = Boolean.valueOf(false);
    private final String name;
    private final int size;
    protected volatile boolean isCancelled = Boolean.valueOf(false);
    private volatile boolean isPaused = Boolean.valueOf(false);

    public Transfer(String name, int size) {
        this.executor = null;
        this.name = name;
        this.size = size;
    }

    public Transfer(String name, int size, Executor executor) {
        this.name = name;
        this.size = size;
        this.executor = executor;
    }

    public void cancel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final void pause() {
        if(state == State.DONE) throw new IllegalStateException("executed and finished already");
        setStatus("wstrzymano");
        this.isPaused = true;
    }

    public final synchronized void resume() {
        if(state == State.DONE) throw new IllegalStateException("executed and finished already");
        setStatus("pobieranie");
        this.isPaused = false;
        this.notifyAll();
    }

    public abstract void transfer();

    public void done() {
    }

    public String getName() {
        return this.name;
    }

    public final synchronized String getStatus() {
        return this.status;
    }

    public final synchronized int getProgress() {
        return this.progress;
    }

    public final synchronized State getState() {
        return this.state;
    }

    public final synchronized int getSize() {
        return this.size;
    }

    @Override
    public final void run() {
        if (executed) {
            throw new IllegalStateException("can't be executed twice");
        }
        executed = Boolean.valueOf(true);
        try {
            setState(State.STARTED);
            transfer();
        } finally {
            setState(State.DONE);
            done();
        }
    }

    public final void execute() {
        if (executed) {
            throw new IllegalStateException("can't be executed twice");
        }
        if (executor != null) {
            executor.execute(this);
        } else {
            new Thread(this).start();
        }
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    protected final int addProgress(int bytes) {
        int already = this.progress;
        pcs.firePropertyChange("progress", already, this.progress += bytes);
        return already;
    }

    protected final synchronized void setStatus(String status) {
        pcs.firePropertyChange("status", this.status, this.status = status);
    }

    protected final synchronized void setProgress(int progress) {
        pcs.firePropertyChange("progress", this.progress, this.progress = progress);
    }

    protected final void checkPaused() {
        if (isPaused) {
            do {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Upload.class.getName()).log(Level.SEVERE, null, ex);
                        isPaused = false;
                    }
                }
            } while (isPaused);
        }
    }

    private synchronized void setState(State state) {
        pcs.firePropertyChange("state", this.state, this.state = state);
    }

    public enum State {

        NEW, STARTED, DONE
    }
}
