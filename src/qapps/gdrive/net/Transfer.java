/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.net;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class representing a transfer. Designed to extend.
 * After exception throw in run() method object is supposed to be runnable again, just 
 * call again run(), depending on implementation following call of run() should continue transferring or 
 * start all over again. After finishing transfer objec shouldn't be useable again.
 * run() throw IOException.
 * @author qba
 */
public abstract class Transfer implements Runnable {

    protected volatile long position = 0;
    private final long length;
    protected final AtomicReference<State> state = new AtomicReference<>(State.NEW);
    protected final PropertyChangeSupport pcs = new java.beans.PropertyChangeSupport(this);

    /**
     * 
     * @param length non negative
     */
    public Transfer(long length) {
        if (length < 0) {
            throw new IllegalArgumentException("length can't be negative");
        }
        this.length = length;
    }

    /**
     * After calling while State==Pending should throw IllegalStateException
     * @throws IOexception when IOException occurs while transferring
     */
    @Override
    public abstract void run();

    /**
     * Stops current transfer and release any resources. 
     * Method is en ensurance that transfer is stopped.
     * May block until operation is cancelled.
     * ThreadSafe - actually should be., no guarantee.
     */
    public abstract void stop() throws InterruptedException;

    /**
     * ThreadSafe.
     * @return 
     */
    public State getState() {
        return this.state.get();
    }

    /**
     * ThreadSafe. After getState() state could already change so before setState(state) you have to synchronize those invocations.
     * @param state 
     */
    protected void setState(State state) {
        State old = this.state.getAndSet(state);
        pcs.firePropertyChange("state", old, state);
    }

    /**
     * ThradSafe.
     * Alwas the same value.
     * @return length in bytes
     */
    public long getLength() {
        return this.length;
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

    public enum State {

        NEW, PENDING, STOPPED, DONE
    }
}
