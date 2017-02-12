package gdrive;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Log extends JFrame
{
  private JScrollPane jScrollPane1;
  private JTextArea jTextArea1;

  public Log()
  {
    initComponents();
  }
  public void add(String tekst) {
    this.jTextArea1.append(tekst + "\n");
  }

  private void initComponents()
  {
    this.jScrollPane1 = new JScrollPane();
    this.jTextArea1 = new JTextArea();

    setDefaultCloseOperation(2);
    setTitle("Log");

    this.jTextArea1.setColumns(20);
    this.jTextArea1.setLineWrap(true);
    this.jTextArea1.setRows(5);
    this.jScrollPane1.setViewportView(this.jTextArea1);

    getContentPane().add(this.jScrollPane1, "Center");

    pack();
  }
}