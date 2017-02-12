/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.pattern.mvc;

import java.awt.Component;
import qapps.pattern.mvc.Model;
import qapps.pattern.mvc.View;

/**
 *
 * @author root
 */
public abstract class SwingView<E> extends View<E> {

    private Component component;

    public SwingView(Component component) {
        this(null, component);
    }

    public SwingView(E model, Component component) {
        super(model);
        this.component = component;
    }

    public Component getComponent() {
        return this.component;
    }
}
