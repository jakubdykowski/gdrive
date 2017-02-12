/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.concurrent.Executor;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

/**
 *
 * @author root
 */
public class ListenerSupport extends EventListenerList {

    private final Executor executor;
    private final Type type;

    /**
     * Fires events using ExecutorService
     * @param executor 
     */
    public ListenerSupport(Executor executor) {
        this.executor = executor;
        type = Type.EXECUTOR_SERVICE;
    }

    /**
     * Fires event in specified by Type argument way.
     * @param type 
     */
    public ListenerSupport(Type type) {
        this.executor = null;
        this.type = type;
    }

    /**
     * Call dynamiclly 'fire' mothod to listeners by giving apropriate arguments.
     * @param l listener class
     * @param method an existing method name
     * @param evt list of argument to be given when invokin method
     * @throws RuntimeException - when method name or arguments is wrong
     */
    public void fire(Class<? extends EventListener> l, String method, Object... evt) {
        fire(new FireEvent(l, method, evt));
    }

    public void fire(FireEvent evt) {
        switch (this.type) {
            case AWT_EVENTQUEUE:
                SwingUtilities.invokeLater(evt);
                break;
            case CURRENT_THREAD:
                evt.run();
                break;
            case EXECUTOR_SERVICE:
                executor.execute(evt);
                break;
        }
    }

    public class FireEvent implements Runnable {

        private final Class<? extends EventListener> l;
        private final String method;
        private final Object[] evt;

        public FireEvent(Class<? extends EventListener> l, String method, Object... evt) {
            this.l = l;
            this.method = method;
            this.evt = evt;
        }

        @Override
        public void run() {
            try {
                Object[] listener = getListeners(l);
                Class[] parameters = new Class[evt.length];
                int p = 0;
                for (Object o : evt) {
                    parameters[p++] = o.getClass();
                }
                Method m = l.getDeclaredMethod(method, parameters);
                for (int i = 0; i < listener.length; i++) {
                    m.invoke(listener[i], evt);
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public enum Type {

        AWT_EVENTQUEUE, CURRENT_THREAD, EXECUTOR_SERVICE
    }
}
