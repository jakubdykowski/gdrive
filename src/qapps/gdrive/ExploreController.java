/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import qapps.gdrive.mvc.Explorator;
import qapps.io.AbstractFile;
import qapps.pattern.mvc.Controller;

/**
 *
 * @author root
 */
public final class ExploreController extends Controller<Explorator, qapps.pattern.mvc.View> implements MouseListener {

    private final JList list;
    private final Explorator explorator;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ExploreController(JList list, Explorator explorator) {
        this.list = list;
        this.explorator = explorator;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getClickCount() > 1) {
            final AbstractFile file = list.getSelectedIndex() > -1 ? (AbstractFile) list.getSelectedValue() : null;
            if (file == null) {
                return;
            }
            list.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            explorator.explore(file, new AbstractFile[]{});
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        explorator.explore(file, file.list());
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                list.setCursor(Cursor.getDefaultCursor());
                            }
                        });
                    } catch (IOException ex) {
                        ex.printStackTrace(System.out);
                    }
                }
            });
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        list.clearSelection();
    }

    @Override
    protected void addModelObservers(Explorator model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void removeModelObservers(Explorator model) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void addViewObservers(qapps.pattern.mvc.View view) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void removeViewObservers(qapps.pattern.mvc.View view) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
