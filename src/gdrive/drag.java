package gdrive;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class drag extends JFrame
{
  private JPanel panel;

  public drag()
  {
    initComponents();

    this.panel.setTransferHandler(null);
  }

  private void initComponents()
  {
    this.panel = new JPanel();

    setDefaultCloseOperation(3);

    this.panel.setLayout(new FlowLayout(0));

    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.panel, -1, 400, 32767));

    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.panel, -1, 300, 32767));

    pack();
  }

  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new drag().setVisible(true);
      }
    });
  }
}