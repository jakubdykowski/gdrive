package gdrive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

public class Main
{
  public static String OS_NAME;
  public static double CURRENT_APP_VERSION = 0.61D;
  static String from = "gdrive1502"; static String to = "jakub.dykowski@gmail.com";
  static Session session;
  static Store store;
  static Folder dir;
  static Folder prev_folder;
  static String username;
  static String password;
  static MainWindow window;
  static String path = "GDRIVE";
  static String path1;
  static String path2;
  static String current_add_path;
  static Message selectedM;
  static Folder selectedD;
  static Upload upload;
  static Download download;
  static FileSystemView fsv;
  static File mainfolder;
  static java.io.File dragged_temp;
  static File dragged_file;
  static boolean is_dragged_file_ready;
  static File selected_file;
  static boolean IN_PROGRESS = false;
  static Loading progress;
  static File[] prevFiles;
  static Image img1;
  static Image img2;
  static File current_dir;
  static ArrayList temporary_multiparts = new ArrayList();
  static ArrayList msgs_dirs_ready_to_add;
  static File parent_ready_to_recive_kids;
  static Log log;
  static ImageIcon FILE_OVER;
  static ImageIcon FILE_SELECTED;
  static Color MOUSE_OVER_BACKGROUND = new Color(207, 230, 239);
  static Color MOUSE_SELECTED_BACKGROUND = new Color(155, 214, 235);
  static TaskManager taskManager;
  static Folder TRASH;

  public static boolean login(String username, String password)
  {
    username = username;
    password = password;
    boolean output = false;
    Properties props = System.getProperties();
    props.setProperty("mail.imaps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    props.setProperty("mail.imaps.socketFactory.fallback", "false");
    props.setProperty("mail.store.protocol", "imaps");
    props.setProperty("mail.imaps.host", "imap.gmail.com");
    props.setProperty("mail.imaps.port", "993");
    props.setProperty("mail.imaps.connectiontimeout", "5000");
    props.setProperty("mail.imaps.timeout", "5000");

    URLName urlName = new URLName("imaps://" + username + ":" + password + "@imap.gmail.com");
    session = Session.getDefaultInstance(props, null);
    try
    {
      store = session.getStore();
      store.addConnectionListener(new SessionListener()); } catch (NoSuchProviderException ex) {
      output = false;
    }try {
      store.connect(username, password);
    } catch (MessagingException ex) {
      output = false; log.add("couldn't connect  #128");
    }if (store.isConnected()) {
      try {
        TRASH = store.getFolder("[Gmail]/Kosz");
        TRASH.addMessageCountListener(new TrashListener());
        Folder folder = store.getFolder("GDRIVE");
        if (!folder.exists()) folder.create(1);
        update(); } catch (MessagingException ex) {
        log.add("failed to get GDRIVE folder #136");
      }output = true; } else {
      output = false;
    }
    return output;
  }
  public static void checkForLog() {
    java.io.File file = new java.io.File("log");
    if (file.exists()) log.setVisible(true);
  }

  public File[] getDir(Folder folder)
  {
    update();
    try {
      Folder[] folders = folder.list();
      if (!folder.isOpen()) folder.open(1);
      Message[] messages = folder.getMessages();
      ArrayList a = new ArrayList();
      for (int i = 0; i < folders.length; i++) {
        File file = new File(folders[i]);

        a.add(file);
      }
      for (int i = 0; i < messages.length; i++) {
        File file = new File(messages[i]);

        a.add(file);
      }
      File[] file = (File[])(File[])a.toArray();
      folder.close(true);
      return file;
    }
    catch (MessagingException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  public static Message[] getFiles(String foldername)
  {
        Message[] msg;
    try {
      Folder folder = store.getFolder(foldername);
      try {
        msg = folder.getMessages();
        return msg;
      } catch (FolderClosedException fce) {
        folder.open(2);
        msg = folder.getMessages();
        return msg;
      } } catch (MessagingException ex) {
    }
    return null;
  }

  public static Folder[] getDirs(Folder dir) throws MessagingException
  {
    try
    {
      return dir.list();
    } catch (FolderClosedException exc) {
      dir.open(2);
      return dir.list();
    }
    catch (MessagingException ex)
    {
      System.err.println("MessagingException catched |Main.getDirs");
      try {
        store.connect();
        try {
          return dir.list();
        } catch (FolderClosedException exc) {
          dir.open(2);
          return dir.list();
        }
      } catch (MessagingException ex1) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex1); }
    }return null;
  }

  public static Message[] getMsgs(Folder dir) throws MessagingException{
    try{
      return dir.getMessages();
    } catch (IllegalStateException exc) {
      dir.open(2);
      return dir.getMessages();
    }catch (MessagingException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex); } return null;
  }

  public static Message[] getFiles()
    throws MessagingException
  {
    update();
    dir.open(2);
    Message[] msg = new Message[0];
    try {
      if (dir.exists()) {
        msg = dir.getMessages();
      }
      dir.close(true); } catch (MessagingException ex) {
    }
    return msg;
  }
  public static Folder[] getFolders(String foldername) {
    Folder[] fds = new Folder[0];
    try {
      Folder folder = store.getFolder(foldername);
      folder.open(2);
      if (folder.exists())
        fds = folder.list();
    } catch (MessagingException ex) {
    }
    return fds;
  }
  public static Folder[] getFolders() throws MessagingException {
    update();
    dir.open(2);
    Folder[] fds = new Folder[0];
    try {
      if (dir.exists()) {
        fds = dir.list();
      }
      dir.close(true); } catch (MessagingException ex) {
    }
    return fds;
  }
  static void setLocation(String addpath) {
    path = path + "/" + addpath;
    update();
  }

  static void setLocation(Folder folder) {
    dir = folder;
    path = folder.getFullName();

    update();
  }

  static void setLocation() {
    update();
    if (!path.equals("GDRIVE")) {
      while (path.charAt(path.length() - 1) != '/') {
        path = path.substring(0, path.length() - 1);
      }
      path = path.substring(0, path.length() - 1);
    }
    update();
  }

  static boolean fileExists(String filename)
  {
    try
    {
      update();
      dir.open(2);
      Message[] msgs = dir.getMessages();
      Folder[] folder = dir.list();
      for (int i = 0; i < msgs.length; i++) {
        if (msgs[i].getSubject().equals(filename)) return false;
      }
      for (int i = 0; i < folder.length; i++)
        if (folder[i].exists()) return false;
    }
    catch (MessagingException ex)
    {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
    return true;
  }
  static Folder mkdir(String dir_name, String path) {
    try {
      Folder folder = store.getFolder(path + "/" + dir_name);

      if ((!folder.exists()) && (folder.create(1))) return folder;
      System.out.println("   " + folder.getFullName());

      return null;
    } catch (MessagingException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }

  static boolean get(File file, String outputdirectory)
  {
    if (file.isFile()) {
      taskManager.add(new DownloadFile(file, outputdirectory));
    } else if (!file.isFile()) {
      java.io.File folder = new java.io.File(outputdirectory + System.getProperty("file.separator") + file.filename);

      folder.mkdir();
      if (folder.exists()) {
        log.add("created dir " + folder.getAbsolutePath());
        get(file.downloadAndGetSubFiles(), outputdirectory + System.getProperty("file.separator") + file.filename);
      }
    }

    return true;
  }
  static boolean getOLD(File file, String outputdirectory) {
    java.io.File temp = new java.io.File(outputdirectory, "/" + file.getFileName());
    FileOutputStream out = null;
    BufferedOutputStream bout = null;
    BufferedInputStream bin = null;
    int dialogResult = 0;
    if ((temp.exists()) && (!file.isPart())) {
      dialogResult = JOptionPane.showConfirmDialog(window, "Plik " + temp.getName() + " już istnieje." + "\n" + "Czy chcesz zastąpić istniejący plik?", "Zastąp", 2, 3);

      if (dialogResult == 0) temp.delete();
    } else {
      dialogResult = 0;
    }if (dialogResult == 0) {
      if (file.isFile()) {
        if ((!file.isMultpart()) && (!file.isPart())) {
          try
          {
            Multipart mp = (Multipart)file.msg.getContent();
            Part part = mp.getBodyPart(0);

            bout = new BufferedOutputStream(new FileOutputStream(temp));
            bin = new BufferedInputStream(part.getInputStream());
            int size = part.getSize();

            log.add("downloading file " + temp.getAbsolutePath() + " |" + size + " bytes");
            byte[] buffer = new byte[1024100];
            int bytesRead;
            while ((bytesRead = bin.read(buffer)) != -1) {
              bout.write(buffer, 0, bytesRead);
            }
            bout.close(); bin.close();
            log.add("file downloaded");
          } catch (IOException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
            log.add(ex.toString());
            try { out.close(); bin.close(); } catch (IOException ioe) { log.add(ioe.toString()); }
            return false;
          } catch (MessagingException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
            log.add(ex.toString());
            try { out.close(); bin.close(); } catch (IOException ioe) { log.add(ioe.toString()); }
            return false;
          }
        }
        else if (file.isMultpart()) {
          try {
            out = new FileOutputStream(temp);
            log.add("downloading multipart " + temp.getAbsolutePath());
            try {
              for (int i = 0; i < file.getPartsNumber(); i++) {
                Multipart mp = null;
                Object ref;
                if (((ref = file.getPart(i).getContent()) instanceof Multipart))
                  mp = (Multipart)ref;
                else return false;
                Part part = mp.getBodyPart(0);
                InputStream in = part.getInputStream();
                int size = part.getSize();
                log.add("   downloading part: file size " + size + " bytes");
                byte[] buf = new byte[1048576];
                int bytesRead;
                while ((bytesRead = in.read(buf)) != -1) out.write(buf, 0, bytesRead);
              }
            } catch (IOException ioe) {
              log.add(ioe.toString()); } catch (MessagingException me) {
              log.add(me.toString()); } catch (Exception e) {
              log.add(e.toString());
              try { out.close(); } catch (IOException ioe) { log.add(ioe.toString()); }
              return false;
            }

            out.close();
            log.add("file downloaded");
          } catch (IOException ex) {
            Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
            log.add(ex.toString());
            try { out.close(); } catch (IOException ioe) { log.add(ioe.toString()); }
            return false;
          }

        }

      }
      else if (!file.isFile()) {
        java.io.File folder = new java.io.File(outputdirectory + "/" + file.filename);
        folder.mkdir();
        if (folder.exists()) {
          log.add("created dir " + folder.getAbsolutePath());
          get(file.downloadAndGetSubFiles(), outputdirectory + "/" + file.filename);
        }
      }
    }

    return true;
  }
  static boolean get(File[] files, String outputdirectory) {
    System.out.println("recived some files, processing files");
    for (int i = 0; i < files.length; i++) {
      get(files[i], outputdirectory);
    }
    return true;
  }
  static boolean add(java.io.File plik, String path) {
    boolean result = true;
    if (plik.isFile()) {
      if (plik.length() > 25165824L) addMp(plik, path); else {
        add(plik.getAbsolutePath(), path);
      }

    }
    else if (plik.isDirectory()) {
      Folder f = mkdir(plik.getName(), path);
      if (f == null) return false;
      result = add(plik.listFiles(), path + "/" + plik.getName());
    }

    return result;
  }
  static boolean add(java.io.File[] pliki, String path) {
    boolean result = false;
    ArrayList ar = new ArrayList();
    for (int i = 0; i < pliki.length; i++)
    {
      result = add(pliki[i], path);
    }

    return result;
  }
  static boolean add(String filepath, String gdrivepath) {
    System.out.println("adding... " + filepath);
    try {
      Folder directory = store.getFolder(gdrivepath);
      Message message = new MimeMessage(session);

      BodyPart messageBodyPart = new MimeBodyPart();
      DataSource source = new FileDataSource(filepath);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(source.getName());
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);

      message.setFileName("unparted");
      message.setContent(multipart);
      message.setSubject(source.getName());
      update();
      try
      {
        message.setFrom(new InternetAddress("GDRIVE"));
        Message[] msgs = new Message[1];
        msgs[0] = message;
        directory.appendMessages(msgs);
      } catch (IllegalStateException e) {
        directory.open(2);
        message.setFrom(new InternetAddress("GDRIVE"));
        Message[] msgs = new Message[1];
        msgs[0] = message;
        directory.appendMessages(msgs);
      }
    }
    catch (MessagingException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      System.out.println("actual add error, probably connection error, fixing >>>");
      if (!store.isConnected()) try {
          store.connect("imap.gmail.com", username, password);
          add(filepath, gdrivepath); } catch (MessagingException ex1) {
        } else if (store.isConnected()) System.out.println("error unknown cause store are actually connected");
    }
    System.out.println("added");
    return true;
  }
  static boolean add(String filepath) {
    System.out.println("adding...");
    try {
      Message message = new MimeMessage(session);

      BodyPart messageBodyPart = new MimeBodyPart();
      DataSource source = new FileDataSource(filepath);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(source.getName());
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);

      message.setContent(multipart);
      message.setSubject(source.getName());
      update();
      if (!dir.isOpen()) dir.open(2);
      message.setFrom(new InternetAddress(dir.getFullName()));
      Message[] msgs = new Message[1];
      msgs[0] = message;

      dir.appendMessages(msgs);
    }
    catch (MessagingException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }System.out.println("added");
    return true;
  }
  static boolean addMp(java.io.File file, String gdrivepath) {
    System.out.println("adding ... " + file.getAbsolutePath());
    try {
      Folder directory = store.getFolder(gdrivepath);

      String filename = file.getName();
      FileOutputStream out = null;
      FileInputStream in = null;
      java.io.File partoffile = null;
      try {
        in = new FileInputStream(file);
      } catch (FileNotFoundException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
      byte[] buff = new byte[4096];
      int bytesRead = 0;

      int countParts = -1;
      do {
        countParts++;
        try {
          partoffile = java.io.File.createTempFile(file.getName(), "");
        } catch (IOException ex) {
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
          out = new FileOutputStream(partoffile);
        } catch (FileNotFoundException ex) {
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
          int countBytes = 0;
          while ((countBytes < 6144) && ((bytesRead = in.read(buff)) != -1)) {
            countBytes++;
            out.write(buff, 0, bytesRead);
          }
          out.close();
        }
        catch (IOException ex) {
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(partoffile);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename + "$$" + countParts + "$");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        Message message = new MimeMessage(session);
        message.setContent(multipart);
        message.setSubject(filename);
        message.setFileName(filename + "$$" + countParts + "$");
        message.setFrom(new InternetAddress("GDRIVE"));
        Message[] msgs = new Message[1];
        msgs[0] = message;

        if (directory.isOpen()) directory.close(true);
        directory.open(2);
        directory.appendMessages(msgs);
        directory.close(true);
        partoffile.delete();
      }while (bytesRead != -1);
      try {
        in.close();
      }
      catch (IOException ex)
      {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      }
    } catch (MessagingException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      System.out.println("actual add error, probably connection error, fixing >>>");
      if (!store.isConnected()) try {
          store.connect("imap.gmail.com", username, password); } catch (MessagingException ex1) {
        } else if (store.isConnected()) System.out.println("error unknown cause store are actually connected");
    }
    System.out.println("added");
    return true;
  }
  static boolean mkdir(String dir_name) {
    boolean result = false;
    try {
      update();
      if (!dir.isOpen()) dir.open(2);
      Folder folder = store.getFolder(path + "/" + dir_name);
      if (!folder.exists()) {
        folder.create(1);
      }
      if (folder.exists()) result = true;
      dir.close(true);
    } catch (MessagingException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex); return false;
    }
    return result;
  }
  static void update() {
    try {
      try { if (dir.isOpen()) dir.close(true);
      } catch (NullPointerException exc) {
      }
      dir = store.getFolder(path);
      dir.open(2);
    } catch (MessagingException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  static boolean delete(File[] file) {
    boolean result = true;
    for (File plik : file) {
      if (delete(plik)) continue; result = false;
    }
    try {
      Folder kosz = store.getFolder("[Gmail]/Kosz");
      if (kosz != null)
        try {
          Message[] msgs = kosz.getMessages();
          for (Message m : msgs) m.setFlag(Flags.Flag.DELETED, true);
          msgs = kosz.expunge();
          for (Message m : msgs) log.add("trashed: " + m.toString());
        }
        catch (IllegalStateException e) {
          kosz.open(2);
          Message[] msgs = kosz.getMessages();
          for (Message m : msgs) m.setFlag(Flags.Flag.DELETED, true);
          msgs = kosz.expunge();
          for (Message m : msgs) log.add("trashed: " + m.toString());
        }
    }
    catch (MessagingException ue)
    {
      log.add(ue.getMessage() + " |TrashListener");
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ue);
    }
    return result;
  }
  static boolean delete(File file) {
    boolean result = true;
    if (file.isfile) {
      if (!file.isMultpart()) result = delete(file.msg);
      else
        for (int i = 0; i < file.getPartsNumber(); i++) {
          if (delete(file.getPart(i))) continue; result = false;
        }
    }
    else result = delete(file.dir);
    return result;
  }
  static boolean delete(Message[] msg) {
    boolean result = true;
    for (int i = 0; i < msg.length; i++) { if ((msg[i] == null) || (delete(msg[i]))) continue; result = false; }
    return result;
  }
  static boolean delete(Message msg) {
    Folder folder = msg.getFolder();
    try {
      Folder kosz;
      try { kosz = store.getFolder("[Gmail]/Kosz");
      } catch (MessagingException e) {
        log.add("[Gmail]/Kosz couldn't get  #665");
        return false;
      }
      try {
        folder.copyMessages(new Message[] { msg }, kosz);
      } catch (FolderClosedException fce) {
        folder.open(2);
        folder.copyMessages(new Message[] { msg }, kosz);
        folder.close(true);
        return true;
      }
    }
    catch (MessagingException ex) {
      System.err.println("MessagingException |delete(Message msg)");
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);

      return false;
    }
    return true;
  }

  static boolean delete(Folder folder) {
    boolean result = true;
    try {
      if (!delete(getMsgs(folder))) result = false;
      for (Folder f : getDirs(folder)) { if (delete(f)) continue; result = false; }
      try
      {
        folder.delete(false);
      } catch (IllegalStateException e) {
        folder.close(false);
        folder.delete(false);
      }
      if (folder.exists()) result = false;
    }
    catch (MessagingException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
      return false;
    }
    return result;
  }
  static void list(Folder folder) {
    update();
    try {
      folder.open(2);
      Folder[] folders = folder.list();
      for (int i = 0; i < folders.length; i++) {
        System.out.println(folders[i].getFullName() + " |dir");
      }
      Message[] msgs = folder.getMessages();
      for (int i = 0; i < msgs.length; i++) {
        System.out.println(msgs[i].getSubject() + " |file");
      }
      dir.close(true);
    } catch (MessagingException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  static void list(boolean ggg) {
    update();
    try {
      if (!dir.isOpen()) dir.open(2);
      Folder[] folders = dir.list();
      for (int i = 0; i < folders.length; i++) {
        System.out.println(folders[i].getFullName() + " |dir");
      }
      Message[] msgs = dir.getMessages();
      for (int i = 0; i < msgs.length; i++) {
        System.out.println(msgs[i].getSubject() + " |file");
      }
      dir.close(true);
    } catch (MessagingException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static void main(String[] args) throws Exception {
    try {
      String s = UIManager.getSystemLookAndFeelClassName();
      UIManager.setLookAndFeel(s); } catch (Exception e) {
      throw new RuntimeException(e);
    }Thread t = Thread.currentThread();
    t.setPriority(10);
    OS_NAME = System.getProperty("os.name");

    log = new Log();
    log.setLocation(850, 80);
    log.setSize(400, 500);
    checkForLog(); log.add(OS_NAME);

    window = new MainWindow();

    window.getContentPane().setBackground(Color.white);

    window.panel.setLayout(new BorderLayout());
    window.panel.add("Center", window.hello);
    window.setVisible(true);

    taskManager = new TaskManager(1);
    taskManager.start();

    progress = new Loading();
    Loading.init();
    progress.start();
  }
}