package gdrive;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Folder;
import javax.mail.Message;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;
import sun.awt.shell.ShellFolder;

public class MainWindow extends JFrame
  implements ActionListener, PropertyChangeListener
{
  public File last_selected_file = null;
  public Image dir_image;
  public Image unknown_file_image;
  JLabel currentDownload;
  private JButton delete;
  private JButton download;
  public JPanel downloaded;
  private JPanel downloadedC;
  private JPanel head;
  JPanel hello;
  private JButton jButton1;
  private JLabel jLabel1;
  private JLabel jLabel2;
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLayeredPane jLayeredPane1;
  JLabel logo;
  private JButton md;
  private JButton md1;
  public JPanel panel;
  private JPanel panel_login;
  private JPanel panel_logo;
  private JPanel panel_menu;
  JProgressBar progressbar;
  JLabel queue;
  private JButton refresh;
  private JButton register;
  JPanel statusBar;
  private JTextField text_login;
  private JPasswordField text_pass;
  private JButton up;
  private JButton upload;

  public void actionPerformed(ActionEvent e)
  {
    System.out.println("MainWindow actionPerformed");
  }

  public MainWindow()
  {
    try
    {
      java.io.File temp = java.io.File.createTempFile("tempfile", ".dir");
      java.io.File tempdir = new java.io.File(temp.getParent(), "tempdirectory");
      tempdir.mkdir();
      this.dir_image = ShellFolder.getShellFolder(tempdir).getIcon(true);
      this.unknown_file_image = ShellFolder.getShellFolder(temp).getIcon(true);
      temp.delete();
      tempdir.delete();
    }
    catch (IOException ex) {
      Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
    }

    initComponents();

    KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    focusManager.addPropertyChangeListener(this);

    this.head.remove(this.panel_menu);
    this.head.add(this.panel_login);
    this.register.setIcon(new ImageIcon(getClass().getResource("/gdrive/img/gmail.png")));
  }

  public boolean exists(String filename) {
    for (int i = 0; i < this.panel.getComponentCount(); i++)
      if ((((File)this.panel.getComponent(i)).getFileName().equals(filename)) && (!((File)(File)this.panel.getComponent(i)).isdeleted) && (!((File)(File)this.panel.getComponent(i)).isPart()))
      {
        return true;
      }
    return false;
  }
  public File getRepresentative(String filename) {
    File plik = null;
    for (int i = 0; i < this.panel.getComponentCount(); i++) {
      plik = (File)(File)this.panel.getComponent(i);
      if ((plik.getFileName().equals(filename)) && (plik.isMultpart())) return plik;
    }
    return null;
  }
  public void updateMenu(boolean connected_or_not) {
    if (connected_or_not) {
      this.head.remove(this.panel_login);
      this.head.add(this.panel_menu);
    } else {
      this.head.remove(this.panel_menu);
      this.head.add("Center", this.panel_login);
    }
    this.head.updateUI();
  }

  private void initComponents()
  {
    this.panel_login = new JPanel();
    this.text_login = new JTextField();
    this.text_pass = new JPasswordField();
    this.jLabel1 = new JLabel();
    this.jLabel2 = new JLabel();
    this.jButton1 = new JButton();
    this.downloaded = new JPanel();
    this.downloadedC = new JPanel();
    this.hello = new JPanel();
    this.register = new JButton();
    this.jLabel3 = new JLabel();
    this.jLabel4 = new JLabel();
    this.refresh = new JButton();
    this.head = new JPanel();
    this.jLayeredPane1 = new JLayeredPane();
    this.panel_logo = new JPanel();
    this.logo = new JLabel();
    this.panel_menu = new JPanel();
    this.up = new JButton();
    this.download = new JButton();
    this.upload = new JButton();
    this.delete = new JButton();
    this.md = new JButton();
    this.md1 = new JButton();
    this.panel = new JPanel();
    this.statusBar = new JPanel();
    this.queue = new JLabel();
    this.currentDownload = new JLabel();
    this.progressbar = new JProgressBar();

    this.panel_login.setBackground(new Color(255, 255, 255));

    this.text_login.setFont(new Font("Microsoft Sans Serif", 0, 11));
    this.text_login.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MainWindow.this.text_loginActionPerformed(evt);
      }
    });
    this.text_pass.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MainWindow.this.text_passActionPerformed(evt);
      }
    });
    this.jLabel1.setFont(new Font("Microsoft Sans Serif", 0, 10));
    this.jLabel1.setText("email:");

    this.jLabel2.setFont(new Font("Microsoft Sans Serif", 0, 10));
    this.jLabel2.setText("hasło:");

    this.jButton1.setIcon(new ImageIcon(getClass().getResource("/gdrive/img/login.png")));
    this.jButton1.setToolTipText("Podłącz wirtualnego Pendrive'a");
    this.jButton1.setFocusable(false);
    this.jButton1.setPreferredSize(new Dimension(50, 50));
    this.jButton1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MainWindow.this.jButton1ActionPerformed(evt);
      }
    });
    GroupLayout panel_loginLayout = new GroupLayout(this.panel_login);
    this.panel_login.setLayout(panel_loginLayout);
    panel_loginLayout.setHorizontalGroup(panel_loginLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(panel_loginLayout.createSequentialGroup().addContainerGap().addComponent(this.jButton1, -2, 50, -2).addGap(18, 18, 18).addGroup(panel_loginLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel1, GroupLayout.Alignment.TRAILING).addComponent(this.jLabel2, GroupLayout.Alignment.TRAILING)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(panel_loginLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.text_pass, -1, 193, 32767).addComponent(this.text_login, -1, 193, 32767)).addContainerGap()));

    panel_loginLayout.setVerticalGroup(panel_loginLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(panel_loginLayout.createSequentialGroup().addContainerGap().addGroup(panel_loginLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(panel_loginLayout.createSequentialGroup().addGroup(panel_loginLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.text_login, -2, -1, -2).addComponent(this.jLabel1)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(panel_loginLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.text_pass, -2, -1, -2).addComponent(this.jLabel2))).addComponent(this.jButton1, -1, 49, 32767)).addContainerGap()));

    this.downloaded.setBackground(new Color(255, 255, 255));

    GroupLayout downloadedLayout = new GroupLayout(this.downloaded);
    this.downloaded.setLayout(downloadedLayout);
    downloadedLayout.setHorizontalGroup(downloadedLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 145, 32767));

    downloadedLayout.setVerticalGroup(downloadedLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 229, 32767));

    this.downloadedC.setLayout(new BorderLayout());

    this.hello.setBackground(new Color(255, 255, 255));

    this.register.setFocusable(false);
    this.register.setPreferredSize(new Dimension(100, 100));
    this.register.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MainWindow.this.registerActionPerformed(evt);
      }
    });
    this.jLabel3.setText("<html><center>Aby korzystać z tej aplikacji musisz posiadać konto email na www.gmail.com. W kwestii bezpieczeństwa zalecane jest by zrobić osobne konto specjalnie do tego celu. ");

    this.jLabel4.setText("<html><center>Jeśli jeszcze nie posiadasz konta gmail to tu możesz je założyć.");

    GroupLayout helloLayout = new GroupLayout(this.hello);
    this.hello.setLayout(helloLayout);
    helloLayout.setHorizontalGroup(helloLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(helloLayout.createSequentialGroup().addGap(69, 69, 69).addGroup(helloLayout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(this.jLabel3, -1, 412, 32767).addGroup(helloLayout.createSequentialGroup().addComponent(this.jLabel4, -2, 167, -2).addGap(3, 3, 3).addComponent(this.register, -2, 115, -2))).addGap(71, 71, 71)));

    helloLayout.setVerticalGroup(helloLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(helloLayout.createSequentialGroup().addGap(23, 23, 23).addComponent(this.jLabel3).addGap(46, 46, 46).addGroup(helloLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel4, GroupLayout.Alignment.TRAILING, -2, -1, -2).addComponent(this.register, GroupLayout.Alignment.TRAILING, -2, 100, -2)).addContainerGap()));

    this.refresh.setIcon(new ImageIcon(getClass().getResource("/gdrive/img/refresh.png")));
    this.refresh.setToolTipText("Odśwież");
    this.refresh.setFocusable(false);
    this.refresh.setMargin(new Insets(0, 0, 0, 0));
    this.refresh.setPreferredSize(new Dimension(50, 50));
    this.refresh.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MainWindow.this.refreshActionPerformed(evt);
      }
    });
    setDefaultCloseOperation(3);
    setTitle("GDrive");
    setBackground(new Color(255, 255, 255));
    setIconImage(new ImageIcon(getClass().getResource("/gdrive/img/icon.png")).getImage());
    setLocationByPlatform(true);
    setMinimumSize(new Dimension(605, 350));
    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        MainWindow.this.formMouseClicked(evt);
      }
      public void mousePressed(MouseEvent evt) {
        MainWindow.this.formMousePressed(evt);
      }
    });
    addComponentListener(new ComponentAdapter() {
      public void componentResized(ComponentEvent evt) {
        MainWindow.this.formComponentResized(evt);
      }
    });
    addWindowStateListener(new WindowStateListener() {
      public void windowStateChanged(WindowEvent evt) {
        MainWindow.this.formWindowStateChanged(evt);
      }
    });
    addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        MainWindow.this.formPropertyChange(evt);
      }
    });
    addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent evt) {
        MainWindow.this.formKeyPressed(evt);
      }
      public void keyReleased(KeyEvent evt) {
        MainWindow.this.formKeyReleased(evt);
      }
      public void keyTyped(KeyEvent evt) {
        MainWindow.this.formKeyTyped(evt);
      }
    });
    this.head.setBackground(new Color(255, 255, 255));
    this.head.setLayout(new FlowLayout(0));
    this.head.add(this.jLayeredPane1);

    this.panel_logo.setBackground(new Color(255, 255, 255));
    this.panel_logo.setLayout(new BorderLayout());

    this.logo.setBackground(new Color(255, 255, 255));
    this.logo.setIcon(new ImageIcon(getClass().getResource("/gdrive/img/gdrive.png")));
    this.logo.setVerticalAlignment(1);
    this.panel_logo.add(this.logo, "Center");

    this.head.add(this.panel_logo);

    this.panel_menu.setBackground(new Color(255, 255, 255));
    this.panel_menu.setLayout(new BoxLayout(this.panel_menu, 2));

    this.up.setIcon(new ImageIcon(getClass().getResource("/gdrive/img/b.png")));
    this.up.setToolTipText("Wstecz");
    this.up.setFocusable(false);
    this.up.setMargin(new Insets(0, 0, 0, 0));
    this.up.setPreferredSize(new Dimension(50, 50));
    this.up.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MainWindow.this.upActionPerformed(evt);
      }
    });
    this.panel_menu.add(this.up);

    this.download.setIcon(new ImageIcon(getClass().getResource("/gdrive/img/pobierz.png")));
    this.download.setToolTipText("Pobierz");
    this.download.setFocusable(false);
    this.download.setMargin(new Insets(0, 0, 0, 0));
    this.download.setPreferredSize(new Dimension(50, 50));
    this.download.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MainWindow.this.downloadActionPerformed(evt);
      }
    });
    this.panel_menu.add(this.download);

    this.upload.setIcon(new ImageIcon(getClass().getResource("/gdrive/img/dodaj.png")));
    this.upload.setToolTipText("Dodaj do GDrive");
    this.upload.setFocusable(false);
    this.upload.setMargin(new Insets(0, 0, 0, 0));
    this.upload.setPreferredSize(new Dimension(50, 50));
    this.upload.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MainWindow.this.uploadActionPerformed(evt);
      }
    });
    this.panel_menu.add(this.upload);

    this.delete.setIcon(new ImageIcon(getClass().getResource("/gdrive/img/delete.png")));
    this.delete.setToolTipText("Usuń");
    this.delete.setFocusable(false);
    this.delete.setMargin(new Insets(0, 0, 0, 0));
    this.delete.setPreferredSize(new Dimension(50, 50));
    this.delete.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MainWindow.this.deleteActionPerformed(evt);
      }
    });
    this.panel_menu.add(this.delete);

    this.md.setIcon(new ImageIcon(getClass().getResource("/gdrive/img/folder.png")));
    this.md.setToolTipText("Utwórz nowy folder");
    this.md.setFocusable(false);
    this.md.setMargin(new Insets(0, 0, 0, 0));
    this.md.setPreferredSize(new Dimension(50, 50));
    this.md.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MainWindow.this.mdActionPerformed(evt);
      }
    });
    this.panel_menu.add(this.md);

    this.md1.setIcon(new ImageIcon(getClass().getResource("/gdrive/img/info.png")));
    this.md1.setToolTipText("Informacje");
    this.md1.setFocusable(false);
    this.md1.setMargin(new Insets(0, 0, 0, 0));
    this.md1.setPreferredSize(new Dimension(50, 50));
    this.md1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        MainWindow.this.md1ActionPerformed(evt);
      }
    });
    this.panel_menu.add(this.md1);

    this.head.add(this.panel_menu);

    getContentPane().add(this.head, "First");

    this.panel.setBackground(new Color(255, 255, 255));
    this.panel.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent evt) {
        MainWindow.this.panelMousePressed(evt);
      }
    });
    this.panel.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent evt) {
        MainWindow.this.panelMouseDragged(evt);
      }
    });
    this.panel.setLayout(new FlowLayout(0, 15, 10));
    getContentPane().add(this.panel, "Center");

    this.statusBar.setBackground(new Color(255, 255, 255));
    this.statusBar.setLayout(new FlowLayout(4, 5, 0));

    this.queue.setText("W kolejce: ");
    this.statusBar.add(this.queue);

    this.currentDownload.setText("pobieranie GDrive.jar");
    this.statusBar.add(this.currentDownload);

    this.progressbar.setFocusable(false);
    this.progressbar.setString("");
    this.progressbar.setStringPainted(true);
    this.progressbar.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        MainWindow.this.progressbarPropertyChange(evt);
      }
    });
    this.statusBar.add(this.progressbar);

    getContentPane().add(this.statusBar, "Last");

    pack();
  }

  private void text_passActionPerformed(ActionEvent evt) {
    Main.log.add("logging in..");
    if (verifyEmail(this.text_login.getText())) {
      Main.IN_PROGRESS = true;
      boolean result = Main.login(this.text_login.getText(), this.text_pass.getText());
      Main.log.add("connected: " + result);
      if (result) {
        this.panel.removeAll();
        this.panel.setLayout(new FlowLayout(0, 15, 10));
        this.panel.updateUI();
        Main.mainfolder = new File(Main.dir, null);
        Main.mainfolder.downloadSubFiles();
        Main.current_dir = Main.mainfolder;
        Main.IN_PROGRESS = false;
        display(Main.current_dir.subfiles);
        refreshButtons();
      } else {
        Main.IN_PROGRESS = false;
        JOptionPane.showConfirmDialog(this, "Logowanie nie powiodło się.\nLogin bądź hasło prawdopodobnie niepoprawne.", "Komunikat", -1, 0);
      }
    }
    else {
      Main.IN_PROGRESS = false;
      JOptionPane.showConfirmDialog(this, "Niepoprawny adres e-mail!", "Komunikat", -1, 2);
    }
  }

  private void deleteActionPerformed(ActionEvent evt)
  {
    Main.IN_PROGRESS = true;
    if (Main.delete(new File[] { (File)getFocusOwner() })) {
      File plik = (File)getFocusOwner();
      ArrayList newSubFiles = new ArrayList();
      newSubFiles.addAll(Arrays.asList(plik.parent.getSubFiles()));
      boolean result = newSubFiles.remove(plik);
      plik.parent.subfiles = ((File[])newSubFiles.toArray(new File[plik.parent.getSubFiles().length - 1]));
      getFocusOwner().getParent().remove((File)getFocusOwner());
    }
    requestFocusInWindow();
    this.panel.doLayout();
    Main.IN_PROGRESS = false;
  }

  private void mdActionPerformed(ActionEvent evt) {
    String s = (String)JOptionPane.showInputDialog(this, "Podaj nazwę folderu:", "Tworzenie nowego folderu", 2, new ImageIcon(Main.window.dir_image), null, null);
    Main.IN_PROGRESS = true;

    if (s != null) { System.out.println("tworzenie folderu");
      Folder f = Main.mkdir(s, Main.current_dir.dir.getFullName());
      if (f != null) {
        File file = new File(f, Main.current_dir);
        Main.current_dir.addSubFile(file);
        this.panel.add(file);
        this.panel.updateUI();
      }
    }
    Main.IN_PROGRESS = false;
  }

  private void upActionPerformed(ActionEvent evt) {
    display(Main.current_dir.parent.subfiles);
    Main.current_dir = Main.current_dir.parent;
    requestFocusInWindow();
  }

  private void panelMouseDragged(MouseEvent evt) {
    System.out.println("mouse dragged   #529");
  }

  private void panelMousePressed(MouseEvent evt)
  {
    requestFocusInWindow();
  }

  private void uploadActionPerformed(ActionEvent evt) {
    JFileChooser fc = new JFileChooser();
    fc.setMultiSelectionEnabled(true);
    fc.setApproveButtonText("Dodaj");
    fc.setFileSelectionMode(2);
    fc.setDialogTitle("Dodaj do GDrive");
    int result = fc.showOpenDialog(null);
    if (result == 0) {
      Main.upload = new Upload(fc.getSelectedFiles());
      Main.upload.start();
    }
  }

  private void formMouseClicked(MouseEvent evt)
  {
  }

  private void formMousePressed(MouseEvent evt)
  {
    requestFocusInWindow();
  }

  private void refreshActionPerformed(ActionEvent evt)
  {
    Main.IN_PROGRESS = true;
    this.panel.removeAll(); this.panel.updateUI();
    Main.current_dir.downloadSubFiles();
    Main.IN_PROGRESS = false;
    display(Main.current_dir.getSubFiles());
    refreshButtons();
  }

  private void formPropertyChange(PropertyChangeEvent evt)
  {
  }

  private void formWindowStateChanged(WindowEvent evt)
  {
  }

  private void formComponentResized(ComponentEvent evt) {
    Loading.setWindowSize(getWidth(), getHeight());
  }

  private void text_loginActionPerformed(ActionEvent evt) {
    text_passActionPerformed(evt);
  }

  private void registerActionPerformed(ActionEvent evt) {
    Desktop d = Desktop.getDesktop();
    try {
      d.browse(URI.create("http://mail.google.com/mail/signup"));
    } catch (IOException ex) {
      Main.log.add(ex.toString());
    }
  }

  private void downloadActionPerformed(ActionEvent evt)
  {
    JFileChooser fc = new JFileChooser();
    File chosedFile = (File)getFocusOwner();
    fc.setApproveButtonText("Zapisz");
    fc.setSelectedFile(new java.io.File(chosedFile.getFileName()));
    fc.setDialogTitle("Zapisz na dysku");
    int result = fc.showSaveDialog(null);
    if (result == 0) {
      File[] pliki = { chosedFile };
      new Download(pliki, fc.getCurrentDirectory()).start();
    }
  }

  private void jButton1ActionPerformed(ActionEvent evt)
  {
    text_passActionPerformed(evt);
  }

  private void formKeyTyped(KeyEvent evt)
  {
  }

  private void formKeyPressed(KeyEvent evt)
  {
  }

  private void formKeyReleased(KeyEvent evt)
  {
    if (Main.selected_file != null) {
      if (KeyEvent.getKeyText(evt.getKeyCode()).equals("Delete")) {
        Main.IN_PROGRESS = true;
        if (Main.delete(Main.selected_file)) {
          this.panel.remove(Main.selected_file);
          Main.selected_file.isdeleted = true;
          Main.selected_file = null;
          this.panel.updateUI();
          refreshButtons();
        }
        Main.IN_PROGRESS = false;
      }
    } else if (KeyEvent.getKeyText(evt.getKeyCode()).equals("Backspace")) {
      if (Main.current_dir != Main.mainfolder) {
        display(Main.current_dir.parent.subfiles);
        Main.current_dir = Main.current_dir.parent;
        refreshButtons();
      }
    } else if (KeyEvent.getKeyText(evt.getKeyCode()).equals("F1"))
      new MainAbout().setVisible(true);
    else if (KeyEvent.getKeyText(evt.getKeyCode()).equals("F12"))
      Main.log.setVisible(true);
  }

  private void md1ActionPerformed(ActionEvent evt)
  {
    MainAbout about = new MainAbout();
    about.setAlwaysOnTop(true);
    about.setLocation((getLocationOnScreen().x + about.getHeight()) / 2, (getLocationOnScreen().x + about.getWidth()) / 2);

    about.setVisible(true);
  }

  private void progressbarPropertyChange(PropertyChangeEvent evt)
  {
  }

  private boolean verifyEmail(String inputtext)
  {
    Pattern p = Pattern.compile("^[a-zA-Z0-9_]+@[a-zA-Z0-9\\-]+\\.[a-zA-Z0-9\\-\\.]+$");
    Matcher m = p.matcher(inputtext);
    return m.find();
  }

  public void refreshButtons()
  {
    boolean status;
    if ((getFocusOwner() instanceof JFrame)) status = false; else status = true;
    this.download.setEnabled(status);
    this.delete.setEnabled(status);
    if (Main.current_dir == Main.mainfolder) status = false; else status = true;
    this.up.setEnabled(status);
  }
  public void display(String foldername) {
    this.panel.removeAll();
    Folder[] fds = Main.getFolders(foldername);
    for (int i = 0; i < fds.length; i++) {
      this.panel.add(new File(fds[i]));
    }
    Message[] msg = Main.getFiles(foldername);
    for (int i = 0; i < msg.length; i++) {
      this.panel.add(new File(msg[i]));
      this.panel.updateUI();
    }
  }

  public void display(File[] pliki) {
    this.panel.removeAll(); this.panel.updateUI();
    if (pliki != null) for (int i = 0; i < pliki.length; i++) { if ((pliki[i].isdeleted) || (pliki[i].isPart())) continue; this.panel.add(pliki[i]); }
    this.panel.updateUI();
    refreshButtons();
  }
  public void display() {
    DragSource ds = DragSource.getDefaultDragSource();
    Main.update();
    this.panel.removeAll();
    this.panel.updateUI();
    Folder[] fds = Main.getFolders(Main.path);
    for (int i = 0; i < fds.length; i++) {
      File file = new File(fds[i]);
      DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(file, 1, new FileDragGestureListener(file));

      this.panel.add(file);
      this.panel.updateUI();
    }

    Message[] msg = Main.getFiles(Main.path);
    for (int i = 0; i < msg.length; i++) {
      File file = new File(msg[i]);

      DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(file, 1, new FileDragGestureListener(file));

      this.panel.add(file);
      this.panel.updateUI();
    }
  }

  public void propertyChange(PropertyChangeEvent e)
  {
    String propertyName = e.getPropertyName();
    if ("focusOwner".equals(propertyName)) {
      if (e.getNewValue() != null)
        refreshButtons();
    }
    else if (!"focusedWindow".equals(propertyName));
  }
}