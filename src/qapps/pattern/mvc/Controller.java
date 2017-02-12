/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.pattern.mvc;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author root
 */
public abstract class Controller<M, V extends View> {

    private M model;
    private List<V> view = new LinkedList<V>();

    public Controller() {
        this(null);
    }

    public Controller(M model) {
        setModel(model);
    }

    public final synchronized void addView(V view) {
        view.add(view);
        if (view != null) {
            this.addViewObservers(view);
        }
    }

    public synchronized void removeView(V view) {
        view.remove(view);
    }

    /**
     * Do not call this method. It's called by setModel(model) when model is setting up.
     * @param model 
     */
    protected abstract void addModelObservers(M model);

    protected abstract void removeModelObservers(M model);

    /**
     * Do not call this method. It's called by setView(view) when view is setting up.
     * @param view 
     */
    protected abstract void addViewObservers(V view);

    protected abstract void removeViewObservers(V view);

    public synchronized final void setModel(M model) {
        this.model = model;
        if (model != null) {
            addModelObservers(model);
        }
    }

    public synchronized final M getModel() {
        return (M) this.model;
    }

    public synchronized final V getView() {
        return (V) this.view;
    }
}
