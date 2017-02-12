package gdrive;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class XML
{
  private File xml;

  public XML(File file)
  {
    this.xml = file;
  }
  public void download() {
  }

  public static void write(Object f, String filename) throws Exception {
    XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));

    encoder.writeObject(f);
    encoder.writeObject(f);
    encoder.close();
  }

  public static Object[] read(String filename) throws Exception {
    ArrayList a = new ArrayList();
    XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
    try
    {
      Object o;
      while ((o = decoder.readObject()) != null) a.add((String)o);
    }
    catch (ArrayIndexOutOfBoundsException e) {
      decoder.close();
      return a.toArray();
    }
    decoder.close();
    return a.toArray();
  }
  public static Object[] read(URL xml) throws IOException {
    ArrayList a = new ArrayList();
    XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(xml.openStream()));
    try
    {
      Object o;
      while ((o = decoder.readObject()) != null) a.add((String)o);
    }
    catch (ArrayIndexOutOfBoundsException e) {
      decoder.close();
      return a.toArray();
    }
    decoder.close();
    return a.toArray();
  }
}