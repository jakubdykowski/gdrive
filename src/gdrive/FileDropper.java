package gdrive;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.dnd.DragSource;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import sun.awt.shell.ShellFolder;

public class FileDropper
{
  public static void main(String[] args)
    throws IOException
  {
    JFrame frame = new JFrame("Hack #65: Drag-and-Drop with Files");
    frame.setDefaultCloseOperation(3);

    ShellFolder sf = ShellFolder.getShellFolder(File.createTempFile("plikwykonywalny", ".png"));
    Icon icon = new ImageIcon(sf.getIcon(true));
    sf.delete();

    frame.getContentPane().setLayout(new BorderLayout());
    JTextArea text = new JTextArea();

    JLabel label = new JLabel("myfile.txt", icon, 0);
    JLabel label1 = new JLabel("plik_wykonywalny", icon, 0);
    DragSource ds = DragSource.getDefaultDragSource();

    frame.getContentPane().add("North", label);
    frame.getContentPane().add("South", label1);
    frame.getContentPane().add("Center", text);

    frame.pack();
    frame.setSize(400, 300);
    frame.setVisible(true);
  }
}