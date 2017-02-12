package gdrive;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Loading extends Thread
{
  String dot1 = ".  "; String dot2 = " . "; String dot3 = "  ."; String dot4 = "   ";
  private int STATUS = 99;
  static int sleeptime = 30;
  static int x = 475; static int y = 232;
  private static BufferedImage[] img;

  public void run()
  {
    boolean b = false;

    setPriority(1);
    while (true) {
      if (Main.IN_PROGRESS) {
        b = true;
        try {
          for (int i = 24; i > 0; i--) {
            Main.window.getGraphics().drawImage(img[(i - 1)], x, y, null);
            if (!Main.IN_PROGRESS) break;
            sleep(sleeptime);
          }
        }
        catch (InterruptedException ex) {
          Main.log.add(ex.toString());
        }continue;
      }

      if (b) Main.window.repaint(); try
      {
        sleep(500L);
      } catch (InterruptedException ex) {
        Logger.getLogger(Loading.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public static void setWindowSize(int width, int height) {
    x = width - 120;
    y = height - 120;
  }
  public void setProgress(int indicator) {
    synchronized (this) { this.STATUS = indicator; }
  }

  static void init() {
    img = new BufferedImage[24];
    for (int i = 24; i > 0; i--)
      try {
        img[(i - 1)] = ImageIO.read(Loading.class.getResource("/gdrive/img/l" + i + ".jpg"));
      } catch (IOException ex) {
        Main.log.add(ex.toString());
      }
  }
}