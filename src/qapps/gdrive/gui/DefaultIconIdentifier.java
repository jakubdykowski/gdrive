/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.gui;

import qapps.io.AbstractFile;

/**
 *
 * @author root
 */
public class DefaultIconIdentifier implements IconIdentifier {

    private static final IconIdentifier strategy = new DefaultIconIdentifier();

    public static IconIdentifier get() {
        return strategy;
    }

    private DefaultIconIdentifier() {
    }

    @Override
    public String identify(Object obj) {
        if (obj instanceof AbstractFile) {
            AbstractFile file = (AbstractFile) obj;
            try {
                if (!file.isFile()) {
                    return "folder";
                }
                String s = file.getName();
                if (!s.contains(".")) {
                    return "default";
                }
                return s.substring(s.lastIndexOf('.') + 1);
            } catch (Exception ex) {
                return "default";
            }
        }
        return null;
    }
}
