package gdrive;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class MainAbout extends JFrame
{
  private JLabel jLabel3;
  private JLabel jLabel4;
  private JLabel jLabel5;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLayeredPane jLayeredPane1;
  private JLabel qapps;

  public MainAbout()
  {
    initComponents();
  }

  private void initComponents()
  {
    this.jLayeredPane1 = new JLayeredPane();
    this.jLabel5 = new JLabel();
    this.jLabel4 = new JLabel();
    this.jLabel6 = new JLabel();
    this.jLabel3 = new JLabel();
    this.jLabel7 = new JLabel();
    this.jLabel8 = new JLabel();
    this.qapps = new JLabel();

    setDefaultCloseOperation(2);
    setTitle("O programie");
    setAlwaysOnTop(true);
    setBackground(new Color(0, 204, 255));
    setResizable(false);

    this.jLayeredPane1.setBackground(new Color(0, 204, 204));
    this.jLayeredPane1.setPreferredSize(new Dimension(300, 200));

    this.jLabel5.setFont(new Font("Tahoma", 1, 11));
    this.jLabel5.setText("Kontakt:");
    this.jLabel5.setBounds(20, 60, 50, 14);
    this.jLayeredPane1.add(this.jLabel5, JLayeredPane.DEFAULT_LAYER);

    this.jLabel4.setFont(new Font("Tahoma", 1, 11));
    this.jLabel4.setText("Autor:");
    this.jLabel4.setBounds(20, 20, 40, 14);
    this.jLayeredPane1.add(this.jLabel4, JLayeredPane.DEFAULT_LAYER);

    this.jLabel6.setFont(new Font("Tahoma", 1, 11));
    this.jLabel6.setText("Homepage:");
    this.jLabel6.setBounds(20, 40, 70, 14);
    this.jLayeredPane1.add(this.jLabel6, JLayeredPane.DEFAULT_LAYER);

    this.jLabel3.setText("admin@qapps.cba.pl");
    this.jLabel3.setBounds(90, 60, 140, 14);
    this.jLayeredPane1.add(this.jLabel3, JLayeredPane.DEFAULT_LAYER);

    this.jLabel7.setText("Jakub Dykowski");
    this.jLabel7.setBounds(90, 20, 120, 14);
    this.jLayeredPane1.add(this.jLabel7, JLayeredPane.DEFAULT_LAYER);

    this.jLabel8.setText("www.qapps.cba.pl");
    this.jLabel8.setBounds(90, 40, 120, 14);
    this.jLayeredPane1.add(this.jLabel8, JLayeredPane.DEFAULT_LAYER);

    this.qapps.setIcon(new ImageIcon(getClass().getResource("/gdrive/img/qapps_logo.png")));
    this.qapps.setBounds(0, 90, 290, 110);
    this.jLayeredPane1.add(this.qapps, JLayeredPane.DEFAULT_LAYER);

    getContentPane().add(this.jLayeredPane1, "Center");

    pack();
  }
}