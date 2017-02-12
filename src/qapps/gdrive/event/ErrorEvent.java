/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.event;

import java.util.EventObject;

/**
 *
 * @author root
 */
public class ErrorEvent extends EventObject {

    private String type = null;

    public ErrorEvent(Exception ex) {
        this(ex, null);
    }

    public ErrorEvent(Exception ex, String type) {
        super(ex);
        this.type = type;
    }

    public Exception getException() {
        return (Exception) source;
    }

    public String getType() {
        return this.type != null ? this.type : "Error";
    }
}
