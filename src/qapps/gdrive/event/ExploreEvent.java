/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.event;

import java.util.EventObject;
import qapps.io.AbstractFile;

/**
 *
 * @author root
 */
public class ExploreEvent extends EventObject {

    private volatile boolean added;
    private volatile boolean removed;
    private volatile boolean finished;
    private volatile boolean started;
    private AbstractFile parent;

    public ExploreEvent(AbstractFile file, boolean added, AbstractFile parent) {
        super(file);
        this.parent = parent;
        this.added = added;
        this.removed = !added;
    }

    public ExploreEvent(AbstractFile parent, boolean started) {
        super(parent);
        this.started = started;
        this.finished = !started;
    }

    public boolean isAdded() {
        return added;
    }

    public boolean isRemoved() {
        return removed;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public boolean isStarted() {
        return this.started;
    }

    public AbstractFile getFile() {
        return (AbstractFile) source;
    }

    public AbstractFile getParent() {
        if (this.parent == null) {
            throw new UnsupportedOperationException("this is marker that exploring started/ended");
        }
        return this.parent;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + ": " + getFile().getPath() + " "
                + (added ? "added" : removed ? "removed" : started ? "started" : finished ? "finished" : "???");
    }
}
