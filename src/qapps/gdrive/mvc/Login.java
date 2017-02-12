/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.mvc;

import qapps.gdrive.gui.LoginPanel;
import qapps.io.drive.Drive;
import qapps.pattern.mvc.SwingView;

/**
 *
 * @author root
 */
public class Login extends SwingView<Drive> {

    private LoginPanel login;
    public Login(Drive model) {
        super(model, new LoginPanel());
        this.login = (LoginPanel) getComponent();
    }

    public Login() {
        this(null);
    }

    @Override
    protected void addNotifiers(Drive model) {
       
    }
    
}
