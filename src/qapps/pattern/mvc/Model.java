/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.pattern.mvc;

import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author root
 */
public abstract class Model {

    private Observable observable = new Observable();

    /**
     * 
     */
    public final synchronized void letKnow() {
        observable.notifyObservers();
    }

    public final synchronized void add(Observer observer) {
        observable.addObserver(observer);
    }

    public final synchronized void remove(Observer observer) {
        observable.deleteObserver(observer);
    }

    public final synchronized void removeAll() {
        observable.deleteObservers();
    }
}
