package gdrive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import sun.awt.shell.ShellFolder;

public class File extends JPanel
  implements PropertyChangeListener, FocusListener, MouseListener
{
  public Property<String> stan = new Property("pusty");
  public File[] subfiles = null;
  public boolean isdeleted = false;
  public File parent;
  public boolean isfile;
  boolean isselected = false;
  public Message msg;
  public Folder dir;
  public String filename;
  public String type;
  public int size;
  public java.io.File file;
  private boolean alien = false;
  private boolean is_multipart = false;
  private ArrayList parts = null;
  private boolean part = false;
  private boolean representative = false;
  private JLabel icon;
  private JPopupMenu pup_menu;
  private JMenuItem pup_usun;
  private JMenuItem pup_zapisz;
  private JLabel title;

  private File getRepresentative(ArrayList filelist)
  {
    File plik = null;
    for (int i = 0; i < filelist.size(); i++) {
      plik = (File)filelist.get(i);
      if ((plik.getFileName().equals(this.filename)) && (plik.isMultpart())) return plik;
    }
    return null;
  }
  public boolean isFile() {
    return this.isfile;
  }
  public boolean isPart() {
    return this.part;
  }
  private void setPart(boolean logic) {
    this.part = logic;
  }
  public Message[] getParts() {
    Message[] m = new Message[this.parts.size()];
    for (int i = 0; i < this.parts.size(); i++) m[i] = ((Message)this.parts.get(i));
    return m;
  }
  public Message getPart(int index) {
    return (Message)this.parts.get(index);
  }
  private void setMultipart(boolean true_or_false) {
    this.is_multipart = true_or_false;
  }
  public boolean isMultpart() {
    return this.is_multipart;
  }
  private void setAlien(boolean isOrNot) {
    this.alien = isOrNot;
  }
  public boolean isAlien() {
    return this.alien;
  }
  private void addPart(Message msg, int index) {
    this.parts.add(index, msg);
  }
  public int getPartsNumber() {
    return this.parts.size();
  }
  public void updateDir() {
    try {
      try {
        this.dir = Main.store.getFolder(this.dir.getFullName());
      } catch (FolderClosedException messagingException1) {
        this.dir.open(2);
        this.dir = Main.store.getFolder(this.dir.getFullName());
      }
    } catch (MessagingException me) {
      System.err.println(me);
    }
  }

  public void downloadSubFiles() {
        try {
            Folder[] dirs = null;
            Message[] msgs = null;
            dirs = Main.getDirs(this.dir);
            msgs = Main.getMsgs(this.dir);
            File[] pliki = new File[dirs.length + msgs.length];
            for (int i = 0; i < dirs.length; i++) {
                pliki[i] = new File(dirs[i], this);
            }
            int x = 0;
            for (int i = dirs.length; i < msgs.length + dirs.length; x++) {
                pliki[i] = new File(msgs[x], this);
                i++;
            }
            if (pliki.length == 0) {
                this.subfiles = null;
            } else {
                this.subfiles = pliki;
            }
        } catch (MessagingException ex) {
            Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
        }
  }

  public File[] downloadAndGetSubFiles() {
        try {
            Folder[] dirs = null;
            Message[] msgs = null;
            dirs = Main.getDirs(this.dir);
            msgs = Main.getMsgs(this.dir);
            File[] pliki = new File[dirs.length + msgs.length];
            for (int i = 0; i < dirs.length; i++) {
                pliki[i] = new File(dirs[i], this);
            }
            int x = 0;
            for (int i = dirs.length; i < msgs.length + dirs.length; x++) {
                pliki[i] = new File(msgs[x], this);
                i++;
            }
            if (pliki.length == 0) {
                this.subfiles = null;
            } else {
                this.subfiles = pliki;
            }
            return pliki;
        } catch (MessagingException ex) {
            Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
  }
  public File(Message msg, File parent) {
    setFocusable(true);

    addMouseListener(this);
    addFocusListener(this);
    this.parent = parent;
    this.msg = msg;
    this.subfiles = null;
    initComponents();
    String s = null;
    try {
      try {
        this.filename = msg.getSubject();
        this.size = msg.getSize();
        s = msg.getFileName();
      } catch (FolderClosedException fce) {
        Folder f = msg.getFolder();
        f.open(2);
        this.filename = msg.getSubject();
        this.size = msg.getSize();
        s = msg.getFileName();
      }

      if (!s.equals("unparted"))
      {
        Pattern wzorzec = Pattern.compile("\\$\\$[0-9]+\\$");
        Matcher m = wzorzec.matcher(s);
        if (m.find()) {
          String index = m.group();
          index = index.substring(2, index.length() - 1);
          int i = Integer.parseInt(index);
          if (i == 0) {
            setMultipart(true);
            this.parts = new ArrayList();
            addPart(msg, 0);
            Main.temporary_multiparts.add(this);
          }
          else {
            setPart(true);
            File repr = getRepresentative(Main.temporary_multiparts);
            try {
              repr.addPart(msg, i);
              repr.size += this.size;
            } catch (NullPointerException e) {
              System.err.println("unknown representative");
              Main.delete(msg);
            }
          }
        }
      }
    } catch (MessagingException ex) {
      Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
    }

    try
    {
      this.type = this.filename.substring(this.filename.lastIndexOf(".")); } catch (StringIndexOutOfBoundsException except) {
      this.type = ".xxx"; } catch (NullPointerException npe) {
      this.type = "";
    }try {
      if ((this.type.equals(".lnk")) || (this.type.length() < 4)) this.type = ".xxx";
      java.io.File temp = java.io.File.createTempFile("randomfile", this.type);
      ShellFolder sf = ShellFolder.getShellFolder(temp);
      temp.delete();
      this.icon.setIcon(new ImageIcon(sf.getIcon(true)));
    } catch (IOException ex) {
      Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NullPointerException exc) {
      this.icon.setIcon(new ImageIcon(Main.window.unknown_file_image));
    }
    this.isfile = true;
    this.title.setText("<html><center>" + this.filename);
  }

  public File(Folder dir, File parent)
  {
    setFocusable(true);
    addMouseListener(this);
    addFocusListener(this);
    this.parent = parent;
    Folder[] dirs = null; Message[] msgs = null;
    initComponents();
    this.dir = dir;
    this.type = ".dir";
    try { this.filename = dir.getName(); } catch (NullPointerException e) { this.filename = "???"; }
    this.icon.setIcon(new ImageIcon(Main.window.dir_image));

    this.isfile = false;
    this.title.setText("<html><center>" + this.filename);
  }
  public File(Message msg) {
    this.parent = this.parent;
    this.msg = msg;
    initComponents();
    String s = null;
    try {
      s = msg.getSubject();
    } catch (MessagingException ex) {
      Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
    }
    this.filename = s.substring(0, s.length() - 4);
    try {
      this.size = msg.getSize();
    } catch (MessagingException ex) {
      Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
    }
    this.type = s.substring(s.lastIndexOf("."));
    try
    {
      if ((this.type.equals(".lnk")) || (this.type.length() < 4)) this.type = ".xxx";
      java.io.File temp = java.io.File.createTempFile("randomfile", this.type);
      ShellFolder sf = ShellFolder.getShellFolder(temp);
      temp.delete();
      this.icon.setIcon(new ImageIcon(sf.getIcon(true)));
    } catch (IOException ex) {
      Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NullPointerException exc) {
      this.icon.setIcon(new ImageIcon(Main.window.unknown_file_image));
    }
    this.isfile = true;
    this.title.setText("<html>" + this.filename + this.type);
  }

  public File(Folder dir)
  {
    Folder[] dirs = null; Message[] msgs = null;
    initComponents();
    this.dir = dir;
    this.type = ".dir";
    this.filename = dir.getName();
    this.icon.setIcon(new ImageIcon(Main.window.dir_image));
        try {
            dirs = Main.getDirs(dir);
            msgs = Main.getMsgs(dir);
        } catch (MessagingException ex) {
            Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, ex);
        }

    File[] pliki = new File[dirs.length + msgs.length];
    for (int i = 0; i < dirs.length; i++) pliki[i] = new File(dirs[i], this);
    int x = 0; for (int i = dirs.length; i < msgs.length + dirs.length; x++) { pliki[i] = new File(msgs[x], this); i++; }
    this.subfiles = pliki;

    this.isfile = false;
    this.title.setText("<html>" + this.filename);
  }

  public String getFileName() {
    return this.filename;
  }
  public File[] getSubFiles() {
    return this.subfiles;
  }
  public void addSubFile(File file) {
    ArrayList a = new ArrayList();
    a.addAll(Arrays.asList(this.subfiles));
    a.add(file);
    this.subfiles = ((File[])a.toArray(new File[this.subfiles.length + 1]));
  }

  public void addSubFile(File[] files) {
    for (int i = 0; i < files.length; i++) addSubFile(files[i]);
  }

  public void addSubFile(ArrayList msgs_and_dirs) {
    for (int i = 0; i < msgs_and_dirs.size(); i++)
      if ((msgs_and_dirs.get(i) instanceof Message))
        addSubFile(new File((Message)msgs_and_dirs.get(i), this));
      else
        addSubFile(new File((Folder)msgs_and_dirs.get(i), this));
  }

  public void setSelected(boolean is) {
    if (is) {
      setBackground(Main.MOUSE_SELECTED_BACKGROUND);
      Main.selected_file = this;
    } else {
      setBackground(Color.white);
      Main.selected_file = null;
    }
    this.isselected = is;
  }
  public boolean isSelected() {
    return this.isselected;
  }

  private void initComponents()
  {
    this.pup_menu = new JPopupMenu();
    this.pup_zapisz = new JMenuItem();
    this.pup_usun = new JMenuItem();
    this.title = new JLabel();
    this.icon = new JLabel();

    this.pup_menu.setBorder(BorderFactory.createBevelBorder(0));

    this.pup_zapisz.setText("Zapisz...");
    this.pup_zapisz.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent evt) {
        File.this.pup_zapiszMouseReleased(evt);
      }
    });
    this.pup_zapisz.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        File.this.pup_zapiszActionPerformed(evt);
      }
    });
    this.pup_menu.add(this.pup_zapisz);

    this.pup_usun.setText("Usuń");
    this.pup_usun.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        File.this.pup_usunActionPerformed(evt);
      }
    });
    this.pup_menu.add(this.pup_usun);

    setBackground(new Color(255, 255, 255));
    setPreferredSize(new Dimension(55, 60));
    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        File.this.formMouseClicked(evt);
      }
      public void mouseEntered(MouseEvent evt) {
        File.this.formMouseEntered(evt);
      }
      public void mouseExited(MouseEvent evt) {
        File.this.formMouseExited(evt);
      }
      public void mousePressed(MouseEvent evt) {
        File.this.formMousePressed(evt);
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent evt) {
        File.this.formMouseMoved(evt);
      }
    });
    setLayout(new BorderLayout());

    this.title.setFont(new Font("Microsoft Sans Serif", 0, 10));
    this.title.setHorizontalAlignment(0);
    this.title.setText("<html><center>name");
    this.title.setVerticalAlignment(1);
    this.title.setAutoscrolls(true);
    this.title.setHorizontalTextPosition(0);
    this.title.setPreferredSize(new Dimension(55, 25));
    add(this.title, "South");

    this.icon.setHorizontalAlignment(0);
    this.icon.setHorizontalTextPosition(0);
    add(this.icon, "Center");
  }

  private void formMouseClicked(MouseEvent evt)
  {
  }

  private void formMouseEntered(MouseEvent evt)
  {
  }

  private void formMouseExited(MouseEvent evt)
  {
  }

  private void formMouseMoved(MouseEvent evt)
  {
  }

  private void formMousePressed(MouseEvent evt)
  {
  }

  private void pup_zapiszActionPerformed(ActionEvent evt)
  {
  }

  private void pup_zapiszMouseReleased(MouseEvent evt) {
    JFileChooser fc = new JFileChooser();
    fc.setApproveButtonText("Zapisz");
    fc.setSelectedFile(new java.io.File(getFileName()));
    fc.setDialogTitle("Zapisz na dysku");
    int result = fc.showSaveDialog(null);
    if (result == 0) {
      DownloadFile task = null;
      if (isFile()) {
        task = new DownloadFile(this, fc.getCurrentDirectory().getAbsolutePath());
        Main.taskManager.add(task);
      } else {
        File[] pliki = { this };
        new Download(pliki, fc.getCurrentDirectory()).start();
      }
    }
  }

  private void pup_usunActionPerformed(ActionEvent evt) {
    Main.IN_PROGRESS = true;
    if (Main.delete(new File[] { this })) {
      ArrayList newSubFiles = new ArrayList();
      newSubFiles.addAll(Arrays.asList(this.parent.getSubFiles()));
      boolean result = newSubFiles.remove(this);
      System.out.println("successfully removed: " + result);
      this.parent.subfiles = ((File[])newSubFiles.toArray(new File[this.parent.subfiles.length - 1]));
      getParent().remove(this);
    }

    Main.window.requestFocusInWindow();
    Main.window.panel.doLayout();
    Main.IN_PROGRESS = false;
  }

  public void propertyChange(PropertyChangeEvent evt)
  {
    System.out.println("File property changed");
    System.out.println("  " + evt.getPropertyName());
    System.out.println("  old" + evt.getOldValue());
    System.out.println("  new" + evt.getNewValue());
    System.out.println("  mypropertychanged" + evt.getPropertyName());
  }
  public void focusGained(FocusEvent e) {
    setBackground(Main.MOUSE_SELECTED_BACKGROUND); }
  public void focusLost(FocusEvent e) { setBackground(Color.white); }
  public void mouseClicked(MouseEvent e) {
  }
  public void mousePressed(MouseEvent e) { requestFocusInWindow();
    if ((e.getClickCount() == 2) && (e.getButton() == 1))
    {
      if (!isFile())
      {
        Main.IN_PROGRESS = true;
        Main.current_dir = this;
        if (this.subfiles == null) downloadSubFiles();
        Main.IN_PROGRESS = false;
        Main.window.display(getSubFiles());
        Main.window.requestFocusInWindow();
      } else if (this.isfile)
      {
        JFileChooser fc = new JFileChooser();
        fc.setApproveButtonText("Zapisz");
        fc.setSelectedFile(new java.io.File(getFileName()));
        fc.setDialogTitle("Zapisz na dysku");
        int result = fc.showSaveDialog(null);
        if (result == 0) {
          DownloadFile task = null;
          task = new DownloadFile(this, fc.getCurrentDirectory().getAbsolutePath());
          Main.taskManager.add(task);
        }
      }
    } }

  public void mouseReleased(MouseEvent e) {
    if (e.getButton() == 3)
    {
      this.pup_menu.show(this, e.getX(), e.getY());
    }
  }

  public void mouseEntered(MouseEvent e) {
    if (!isFocusOwner()) setBackground(Main.MOUSE_OVER_BACKGROUND);
  }
  public void mouseExited(MouseEvent e) { if (!isFocusOwner()) setBackground(Color.white);
  }
}