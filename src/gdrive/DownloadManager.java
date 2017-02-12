package gdrive;

import javax.swing.SwingWorker;

public class DownloadManager extends Thread
{
  private boolean KEEP_PROCESSING = true;
  private TaskManager connection;

  public DownloadManager()
  {
    this.connection = new TaskManager(1);
    this.connection.setRemoveFinished(false);
  }

  public void run() {
    TaskManagerWindow w = new TaskManagerWindow();
    w.setVisible(true);
    while (this.KEEP_PROCESSING)
    {
      Upload up;
      for (int i = 0; i < this.connection.getCount(); i++) {
        Object obj = this.connection.getTask(i);
        Download down;
        if ((obj instanceof Download))
          down = (Download)obj;
        else if ((obj instanceof Upload))
          up = (Upload)obj;
      }
    }
  }

  public synchronized void exit() {
    this.KEEP_PROCESSING = false;
  }
  public void add(SwingWorker t) {
    this.connection.add(t);
  }
}