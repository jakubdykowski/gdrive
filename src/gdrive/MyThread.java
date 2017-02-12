package gdrive;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyThread extends Thread
{
  private int st = -1;

  public void run()
  {
    if (this.st > -1) try {
        sleep(this.st);
      } catch (InterruptedException ex) {
        Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
      }
  }

  public void setSleepTime(int milisec) {
    if (milisec > -1) this.st = milisec;
  }
}