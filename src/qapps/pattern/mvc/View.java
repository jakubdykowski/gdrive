/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.pattern.mvc;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Class representing the View from Model-View-Controller pattern.
 * Designed to override.
 * When you override this class method consider calling first super method,
 * of course first you should get to know what it do actually.
 * @author root
 */
public abstract class View<E> {

    private List<View> view = new LinkedList<View>();
    private E model = null;

    public View() {
        this(null);
    }

    public View(E model) {
        this.model = model;
    }

    /**
     * Free to override.
     * Synchronized.
     * By default calls refresh() for each View which gets by calling get(i) till method return null.
     */
    public void refresh() {
        synchronized (this) {
            int i = 0;
            View widok = null;
            while ((widok = get(i++)) != null) {
                widok.refresh();
            }
        }
    }

    /**
     * Free to override.
     * Synchronized.
     * @param view 
     */
    public void add(View view) {
        if (view == null) {
            throw new NullPointerException("view argument is null");
        }
        synchronized (this) {
            this.view.add(view);
        }
    }

    /**
     * Free to override.
     * Synchronized.
     * @param view 
     */
    public void remove(View view) {
        if (view == null) {
            throw new NullPointerException("view argument is null");
        }
        synchronized (this) {
            this.view.remove(view);
        }
    }

    /**
     * Free to override.
     * Synchronized.
     * Consumes the ArrayOutOfBoundException and returnes null.
     * @param i index of the view
     * @return the view or null
     */
    public View get(int i) {
        View widok = null;
        synchronized (this) {
            try {
                widok = this.view.get(i);
            } catch (IndexOutOfBoundsException ex) {
            } finally {
                return widok;
            }
        }
    }

    /**
     * Free to override.
     * Synchronized.
     * Sets the model field (implSetModel(model)) and calls refresh() to update view with new data
     * @param model 
     */
    public void setModel(E model) {
        synchronized (this) {
            implSetModel(model);
            if (model != null) {
                addNotifiers(model);
            }
            refresh();
        }
    }

    protected final void implSetModel(E model) {
        this.model = model;
    }

    public final synchronized E getModel() {
        return (E) this.model;
    }

    protected abstract void addNotifiers(E model);
    
    /**
     * The observer calls refresh() method when invoked by Observable.
     * @return implelentation of Observer interface
     */
    public Observer callback() {
        return new Observer() {

            @Override
            public void update(Observable o, Object arg) {
                refresh();
            }
            
        };
    }
}
