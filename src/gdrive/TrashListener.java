package gdrive;

import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;

class TrashListener
  implements MessageCountListener
{
  public void messagesAdded(MessageCountEvent mce)
  {
    Main.log.add("dodano plik do kosza");
  }

  public void messagesRemoved(MessageCountEvent mce) {
    Main.log.add("usunięto plik z kosza");
  }
}