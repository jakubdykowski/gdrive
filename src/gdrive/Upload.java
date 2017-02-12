package gdrive;

import java.io.PrintStream;
import javax.mail.Folder;

public class Upload extends Thread
{
  java.io.File[] files;
  private int STATUS;

  public Upload()
  {
  }

  public Upload(java.io.File[] f)
  {
    this.files = f;
  }

  public void run() {
    Main.IN_PROGRESS = true;

    Main.current_add_path = Main.current_dir.dir.getFullName();

    for (int i = 0; i < this.files.length; i++)
    {
      if ((this.files[i].exists()) && (!Main.window.exists(this.files[i].getName()))) {
        System.out.println("started uploading... |" + i);
        boolean result = Main.add(this.files[i], Main.current_add_path);
        if (result) continue; System.out.println(" failed ");
      }
    }
    Main.current_dir.updateDir();
    Main.current_dir.downloadSubFiles();
    Main.IN_PROGRESS = false;
    Main.window.display(Main.current_dir.getSubFiles());
  }
  public void setFiles(java.io.File[] file) {
    this.files = file;
  }
}