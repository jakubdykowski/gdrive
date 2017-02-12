package gdrive;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class randomcode
{
  private static final String SMTP_HOST_NAME = "smtp.gmail.com";
  private static final String SMTP_PORT = "465";
  private static final String SMTP_AUTH_USER = "gdrive1502@gmail.com";
  private static final String SMTP_AUTH_PWD = "19691426";
  private static final String SMTP_FROM_ADDRESS = "gdrive1502@gmail.com";

  public static void main(String[] args)
  {
    randomcode r = new randomcode();
    try {
      r.sendMail("jakub.dykowski@gmail.com", "test", "cześć");
    } catch (Exception ex) {
      Logger.getLogger(randomcode.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void sendMail(String email, String subject, String text)
    throws Exception
  {
    String[] emailList = new String[1];
    emailList[0] = email;
    sendMail(emailList, subject, text);
  }

  public void sendMail(String[] emailList, String subject, String text)
    throws Exception
  {
    boolean debug = true;

    Properties props = new Properties();

    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.auth", "true");
    props.put("mail.debug", "true");
    props.put("mail.smtp.user", "gdrive1502@gmail.com");
    props.put("mail.smtp.password", "19691426");
    props.put("mail.smtp.port", "993");

    Authenticator auth = new SMTPAuthenticator();//null

    Session session = Session.getDefaultInstance(props, auth);

    session.setDebug(debug);

    Message msg = new MimeMessage(session);

    InternetAddress addressFrom = new InternetAddress("gdrive1502@gmail.com");
    msg.setFrom(addressFrom);

    InternetAddress[] addressTo = new InternetAddress[emailList.length];

    for (int i = 0; i < emailList.length; i++)
    {
      addressTo[i] = new InternetAddress(emailList[i]);
    }

    msg.setRecipients(Message.RecipientType.TO, addressTo);

    msg.setSubject(subject);
    msg.setContent(text, "text/plain");

    Transport.send(msg);
  }

  private class SMTPAuthenticator extends Authenticator {
    private SMTPAuthenticator() {
    }

        @Override
    public PasswordAuthentication getPasswordAuthentication() {
      String username = "gdrive1502@gmail.com";
      String password = "19691426";
      return new PasswordAuthentication(username, password);
    }
  }
}