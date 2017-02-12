/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.mvc;

import qapps.pattern.mvc.SwingView;
import qapps.swing.FilePanel;

/**
 *
 * @author root
 */
public class Explorer extends SwingView<Explorator> {

    private final FilePanel panel;

    public Explorer(Explorator model) {
        super(model, new FilePanel());
        this.panel = (FilePanel) getComponent();
    }

    @Override
    public void refresh() {
        //update view
        panel.clear();
        for(Object element : getModel().getFiles()) {
            panel.add(element);
        }
        //update sub views
        super.refresh();
    }

    @Override
    protected void addNotifiers(Explorator model) {
        //add notifiers
//        model.add(callback());
    }
}
