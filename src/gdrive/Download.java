package gdrive;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Download extends Thread
{
  private int STATUS = 0;
  public File[] files;
  public java.io.File outputfile;
  public String output;
  FileOutputStream out;
  InputStream in;

  public Download(File[] file, java.io.File outputdirectory)
  {
    this.files = file; this.outputfile = outputdirectory;
  }

  public void run() {
    Main.window.statusBar.setVisible(true);
    Main.window.progressbar.setIndeterminate(true);
    System.out.println("showing statusbar" + Main.window.statusBar.isVisible());

    setPriority(10);
    this.output = this.outputfile.getAbsolutePath();
    this.output = this.output.replaceAll("\\\\", "/");

    Main.get(this.files, this.output);
  }

  public int getStatus()
  {
    return this.STATUS;
  }
}