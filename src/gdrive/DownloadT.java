package gdrive;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class DownloadT extends Thread
{
  private int STATUS = 0;
  public File file;
  public java.io.File outputfile;
  public String output;
  FileOutputStream out;
  InputStream in;

  public DownloadT(File file, java.io.File outputdirectory)
  {
    this.file = file; this.outputfile = outputdirectory;
  }

  public void run() {
    Main.IN_PROGRESS = true;
    setPriority(10);
    this.output = this.outputfile.getAbsolutePath();
    this.output = this.output.replaceAll("\\\\", "/");
    System.out.println("downloading started");
    Main.get(this.file, this.output);
    System.out.println("downloading finished");
    Main.IN_PROGRESS = false;
  }

  public int getStatus()
  {
    return this.STATUS;
  }
}