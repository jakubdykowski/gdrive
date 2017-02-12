package gdrive;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TaskManagerWindow extends JFrame
{
  private JScrollPane jScrollPane1;
  private JTextArea jTextArea1;

  public TaskManagerWindow()
  {
    initComponents();
    this.jTextArea1.setEditable(false);
  }

  private void initComponents()
  {
    this.jScrollPane1 = new JScrollPane();
    this.jTextArea1 = new JTextArea();

    setDefaultCloseOperation(3);
    setMinimumSize(new Dimension(640, 480));
    getContentPane().setLayout(new BoxLayout(getContentPane(), 2));

    this.jTextArea1.setColumns(20);
    this.jTextArea1.setFont(new Font("Microsoft Sans Serif", 0, 10));
    this.jTextArea1.setRows(5);
    this.jTextArea1.setPreferredSize(new Dimension(164, 69));
    this.jScrollPane1.setViewportView(this.jTextArea1);

    getContentPane().add(this.jScrollPane1);

    pack();
  }

  public static void main(String[] args)
    throws Exception
  {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        new TaskManagerWindow().setVisible(true);
      } } );
  }

  public void clear() {
    this.jTextArea1.setText("");
  }
  public void addLine(String line) {
    this.jTextArea1.append(line + "\n");
  }
}