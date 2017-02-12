/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import javax.swing.SwingUtilities;

/**
 * An PropertyChangeSupport with support of executing listeners in defined by user threads. By default in AWT EventDispatchThread.
 * @author root
 */
public class ThreadedPCS extends PropertyChangeSupport {

    private final Executor executor;
    private final Exec type;

    /**
     * Runs listeners in AWT EventDispatchThread.
     * @param source 
     */
    public ThreadedPCS(Object source, Exec type) {
        super(source);
        this.executor = null;
        this.type = type;
    }

    /**
     * Runs listener using given executor or AWT EventDispatchThread when NULL is given.
     * @param source
     * @param executor 
     */
    public ThreadedPCS(Object source, Executor executor) {
        super(source);
        this.executor = executor;
        this.type = Exec.EXECUTOR;
    }

    @Override
    public synchronized void firePropertyChange(final PropertyChangeEvent evt) {
        Runnable fire = new Runnable() {

            @Override
            public void run() {
                ThreadedPCS.super.firePropertyChange(evt);
            }
        };
        if (executor == null) {
            SwingUtilities.invokeLater(fire);
        } else {
            executor.execute(fire);
        }
    }
    
    public enum Exec {
        CURREN_THREAD, AWT_EVENT_QUEUE, EXECUTOR
    }
}
