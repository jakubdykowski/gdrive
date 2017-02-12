/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.net;

import java.io.IOException;
import java.io.InputStream;

/**
 * Defines that object can be represented as stream, a file-like object.
 * @author qba
 */
public interface FileLike {

    /**
     * Assuming that objec has not changed, multiple calls return streams with equal content but strams are separatet, they are not equal - the same object.
     * @return
     * @throws IOException 
     */
    InputStream getInputStream() throws IOException;
}
