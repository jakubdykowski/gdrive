package gdrive;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Update
{
  static String HOMEPAGE = "http://www.qapps.cba.pl/gdrive/";
  static String INFO_FILE = "update.php";
  static String LATEST_APP = "GDrive.jar";
  static String LIBRARY_DIR_NAME = ".gdrive";
  static String[] libs = { "mail.jar", "activation.jar" };
  static boolean REBOOT = false;

  public static boolean isNewVersionAvaliable() {
    URL url = null;

    BufferedReader br = null;
    try
    {
      url = new URL(HOMEPAGE + INFO_FILE);
    } catch (MalformedURLException mURLe) {
      System.err.println(mURLe);
      return false;
    }
    try {
      br = new BufferedReader(new InputStreamReader(url.openStream()));
    } catch (IOException ioe) {
      System.err.println(ioe);
      return false;
    }
    try
    {
      String linia;
      while ((linia = br.readLine()) != null) {
        if (linia.contains("latestversion")) {
          Pattern wzorzec = Pattern.compile("latestversion<[0-9]+\\.[0-9]+>");
          Matcher m = wzorzec.matcher(linia);
          if (m.find()) {
            String latestVersion = m.group();
            latestVersion = latestVersion.replaceFirst("latestversion", "");
            latestVersion = latestVersion.substring(1, latestVersion.length() - 1);
            double ver = Double.parseDouble(latestVersion);
            if (Main.CURRENT_APP_VERSION < ver) return true;
          }
        }
        if (linia.contains("special")) {
          Pattern wzorzec = Pattern.compile("special<[a-z]+>");
          Matcher m = wzorzec.matcher(linia);
          if (m.find()) {
            String latestVersion = m.group();
            latestVersion = latestVersion.replaceFirst("special", "");
            latestVersion = latestVersion.substring(1, latestVersion.length() - 1);
          }
        }
      }
    } catch (IOException ioe) {
      System.out.println(ioe);
      return false;
    }
    return false;
  }

  public static boolean downloadLatestVersion() throws IOException
  {
    URL url;
    try
    {
      url = new URL(HOMEPAGE + LATEST_APP);
    } catch (MalformedURLException murle) {
      System.err.println(murle);
      return false;
    }BufferedInputStream bin;
    try { bin = new BufferedInputStream(url.openStream());
    } catch (IOException ioe) {
      System.err.println(ioe);
      return false;
    }
    File file = null;
    try {
      file = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
    } catch (URISyntaxException ex) {
      Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
    }FileOutputStream out;
    try { out = new FileOutputStream(file);
    } catch (FileNotFoundException ex) {
      Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }

    byte[] buff = new byte[4096];
    try
    {
      int bytesRead;
      while ((bytesRead = bin.read(buff)) != -1) out.write(buff, 0, bytesRead);
    }
    catch (IOException ioe)
    {
      System.err.println(ioe);
      return false;
    }
    Main.log.add("downloaded " + file.getAbsolutePath());
    try {
      out.close();
    } catch (IOException ex) {
      Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
      System.err.println(ex);
      return false;
    }
    Process ps = Runtime.getRuntime().exec(new String[] { "java", "-jar", file.getName() });
    System.exit(1);
    return true;
  }

  public static boolean buildLibraries()
  {
    File dir = new File(System.getProperty("user.home", Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()) + "\\" + LIBRARY_DIR_NAME);

    if (!dir.exists()) { if (!dir.mkdir()) return false;
      if (Main.OS_NAME.contains("indows")) {
        try {
          Runtime.getRuntime().exec("attrib +h " + dir.getAbsolutePath());
        } catch (IOException ex) {
          Main.log.add(ex.toString());
        }

      }

    }

    for (int i = 0; i < libs.length; i++) {
      File file = new File(System.getProperty("user.home", Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()) + "\\" + LIBRARY_DIR_NAME + "\\" + libs[i]);

      if (!file.exists()) { REBOOT = true;
        URL url;
        try { url = new URL(HOMEPAGE + libs[i]);
        } catch (MalformedURLException ex) {
          Main.log.add(ex.toString());
          continue; } BufferedInputStream bin;
        try { bin = new BufferedInputStream(url.openStream());
        } catch (IOException ioe) {
          Main.log.add(ioe.toString());
          continue; } FileOutputStream out;
        try { out = new FileOutputStream(file);
        } catch (FileNotFoundException ex) {
          Main.log.add(ex.toString());
          continue;
        }

        byte[] buff = new byte[4096];
        try
        {
          int bytesRead;
          while ((bytesRead = bin.read(buff)) != -1) out.write(buff, 0, bytesRead);
        }
        catch (IOException ioe) {
          Main.log.add(ioe.toString());
          continue;
        }
        Main.log.add("lib " + file.getAbsolutePath() + " created");
        try { out.close();
        } catch (IOException ex) {
          Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
          Main.log.add(ex.toString());
        }
      }
    }

    for (int i = 0; i < libs.length; i++) {
      File file = new File(System.getProperty("user.home", Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()) + "\\" + LIBRARY_DIR_NAME + "\\" + libs[i]);

      if (!file.exists()) return false;
    }
    if (REBOOT) {
      try {
        Runtime.getRuntime().exec(new String[] { "java", "-jar", "GDrive.jar" });
      } catch (IOException ex) {
        Main.log.add(ex.toString());
      }
      System.exit(1);
    }
    return true;
  }
}