package gdrive;

import javax.mail.event.ConnectionEvent;
import javax.mail.event.ConnectionListener;
import javax.swing.JPanel;

class SessionListener
  implements ConnectionListener
{
  public void opened(ConnectionEvent ce)
  {
    Main.window.updateMenu(true);
    Main.window.panel.setTransferHandler(new ListTransferHandler());
  }

  public void disconnected(ConnectionEvent ce) {
    Main.log.add("disconected");
  }

  public void closed(ConnectionEvent ce) {
    Main.window.updateMenu(false);
    Main.window.panel.setTransferHandler(null);
  }
}