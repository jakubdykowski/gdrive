package gdrive;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Path extends JFrame
{
  private JButton b1;
  private JButton b2;
  private JButton b3;
  private JScrollPane jScrollPane1;
  private JButton ok;
  private JButton q;
  private JButton t;
  private JTextArea ta;
  private JTextField tf1;
  private JTextField tf2;
  private JTextField tf3;

  public Path()
  {
    initComponents();
  }

  private void initComponents()
  {
    this.jScrollPane1 = new JScrollPane();
    this.ta = new JTextArea();
    this.tf1 = new JTextField();
    this.tf2 = new JTextField();
    this.tf3 = new JTextField();
    this.b1 = new JButton();
    this.b2 = new JButton();
    this.b3 = new JButton();
    this.ok = new JButton();
    this.q = new JButton();
    this.t = new JButton();

    setDefaultCloseOperation(3);

    this.ta.setColumns(20);
    this.ta.setRows(5);
    this.jScrollPane1.setViewportView(this.ta);

    this.tf1.setText("jTextField1");

    this.tf2.setText("jTextField2");

    this.tf3.setText("jTextField3");

    this.b1.setText("1");

    this.b2.setText("2");
    this.b2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Path.this.b2ActionPerformed(evt);
      }
    });
    this.b3.setText("3");
    this.b3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Path.this.b3ActionPerformed(evt);
      }
    });
    this.ok.setText("ok");

    this.q.setText("q");
    this.q.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Path.this.qActionPerformed(evt);
      }
    });
    this.t.setText("t");
    this.t.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Path.this.tActionPerformed(evt);
      }
    });
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.tf3, GroupLayout.Alignment.TRAILING).addComponent(this.tf2, GroupLayout.Alignment.TRAILING).addComponent(this.tf1, GroupLayout.Alignment.TRAILING).addComponent(this.jScrollPane1, GroupLayout.Alignment.TRAILING, -1, 269, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.t).addComponent(this.b1).addComponent(this.b3).addComponent(this.b2)).addContainerGap(76, 32767)).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(302, 32767).addComponent(this.q).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.ok).addContainerGap()));

    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(this.jScrollPane1, -2, 131, -2).addComponent(this.t)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.tf1, -2, -1, -2).addComponent(this.b1)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.tf2, -2, -1, -2).addComponent(this.b2)).addGap(9, 9, 9).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.tf3, -2, -1, -2).addComponent(this.b3)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 34, 32767).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.ok).addComponent(this.q)).addContainerGap()));

    pack();
  }

  private void qActionPerformed(ActionEvent evt)
  {
    JFileChooser fc = new JFileChooser();
    fc.setMultiSelectionEnabled(true);
    fc.setFileSelectionMode(2);
    fc.setDialogTitle("Otwórz");
    fc.setCurrentDirectory(new File(getClass().getResource("").getPath() + "../../"));
    int result = fc.showOpenDialog(null);
    if (result == 0)
    {
      this.tf1.setText(fc.getSelectedFile().getPath());
    }
  }

  private void b2ActionPerformed(ActionEvent evt) {
    this.tf2.setText(getClass().getResource("").getPath());
  }

  private void tActionPerformed(ActionEvent evt) {
    File file = new File(this.tf3.getText());
    if (file.isDirectory()) {
      this.ta.setText("");
      String[] s = file.list();
      for (int i = 0; i < s.length; i++) this.ta.append(s[i] + "\n");
    }
  }

  private void b3ActionPerformed(ActionEvent evt)
  {
    Pattern wzorzec = Pattern.compile("\\$\\$[0-9]+\\$");
    Matcher m = wzorzec.matcher(this.tf3.getText());

    if (m.find()) {
      String index = m.group();
      index = index.substring(2, index.length() - 1);
      int i = Integer.parseInt(index);
      this.ta.setText("index: " + i); } else {
      this.ta.append("no match\n");
    }
  }

  public static void main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException ex) {
      Logger.getLogger(Path.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      Logger.getLogger(Path.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      Logger.getLogger(Path.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex) {
      Logger.getLogger(Path.class.getName()).log(Level.SEVERE, null, ex);
    }
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Path().setVisible(true);
      }
    });
  }
}