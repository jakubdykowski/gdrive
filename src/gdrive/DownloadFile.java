package gdrive;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

class DownloadFile extends SwingWorker<Void, Integer>
  implements PropertyChangeListener
{
  private File file;
  private String outputdirectory;

  public DownloadFile(File file, String outputPath)
  {
    this.file = file;
    this.outputdirectory = outputPath;
  }
  protected Void doInBackground() {
    java.io.File temp = new java.io.File(this.outputdirectory, "/" + this.file.getFileName());
    Main.window.currentDownload.setText("pobieranie " + this.file.getFileName());
    FileOutputStream out = null;
    BufferedOutputStream bout = null;
    BufferedInputStream bin = null;
    int dialogResult;
    if ((temp.exists()) && (!this.file.isPart())) {
      dialogResult = JOptionPane.showConfirmDialog(Main.window, "Plik " + temp.getName() + " już istnieje." + "\n" + "Czy chcesz zastąpić istniejący plik?", "Zastąp", 2, 3);

      if (dialogResult == 0) temp.delete();
    } else {
      dialogResult = 0;
    }if ((dialogResult == 0) &&
      (this.file.isFile()))
    {
      boolean USELESS_HEADER;
      if (this.file.getFileName().contains(".txt")) USELESS_HEADER = false; else USELESS_HEADER = true;
      if ((!this.file.isMultpart()) && (!this.file.isPart())) {
        try {
          setProgress(0);

          Multipart mp = (Multipart)this.file.msg.getContent();
          Part part = mp.getBodyPart(0);
          bin = new BufferedInputStream(part.getInputStream());
          bout = new BufferedOutputStream(new FileOutputStream(temp));
          int size = part.getSize();
          if (USELESS_HEADER) size = (int)(size * 0.731D);
          Main.log.add("downloading file " + temp.getAbsolutePath() + " |" + size + " bytes");
          byte[] buffer = new byte[10240];

          int allBytes = 0;
          int bytesRead;
          while ((bytesRead = bin.read(buffer)) != -1) {
            allBytes += bytesRead;
            int p = new Integer(allBytes * 100 / size).intValue();
            bout.write(buffer, 0, bytesRead);
            setProgress(p);
          }
          setProgress(100);
          bout.close(); bin.close();
          Main.log.add("file downloaded " + allBytes + " bytes");
        } catch (IOException ex) {
          Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
          Main.log.add(ex.toString());
          try { out.close(); bin.close(); } catch (IOException ioe) { Main.log.add(ioe.toString());
          }
          return null;
        } catch (MessagingException ex) {
          Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
          Main.log.add(ex.toString());
          try { out.close(); bin.close(); } catch (IOException ioe) { Main.log.add(ioe.toString());
          }
          return null;
        }
      }
      else if (this.file.isMultpart()) {
        try {
          setProgress(0);
          bout = new BufferedOutputStream(new FileOutputStream(temp));
          Main.log.add("downloading multipart " + temp.getAbsolutePath());
          try {
            int size = 0;
            for (int i = 0; i < this.file.getPartsNumber(); i++) {
              Multipart mp = null;
              Object ref;
              if (((ref = this.file.getPart(i).getContent()) instanceof Multipart)) {
                mp = (Multipart)ref;
                Part part = mp.getBodyPart(0);
                size += part.getSize();
              }
            }
            for (int i = 0; i < this.file.getPartsNumber(); i++) {
              Multipart mp = null;
              Object ref;
              if (((ref = this.file.getPart(i).getContent()) instanceof Multipart)) {
                mp = (Multipart)ref;
              }
              else {
                return null;
              }
              Part part = mp.getBodyPart(0);
              bin = new BufferedInputStream(part.getInputStream());

              Main.log.add("   downloading part[" + i + "]: file size " + size + " bytes");
              byte[] buf = new byte[1048576];

              int allBytes = 0;
              int bytesRead;
              while ((bytesRead = bin.read(buf)) != -1) {
                allBytes++;
                out.write(buf, 0, bytesRead);
                setProgress(getProgress() + bytesRead * 100 / size);
              }
            }
            setProgress(100); } catch (IOException ioe) {
            Main.log.add(ioe.toString()); } catch (MessagingException me) {
            Main.log.add(me.toString()); } catch (Exception e) {
            Main.log.add(e.toString());
            try { out.close(); } catch (IOException ioe) { Main.log.add(ioe.toString());
            }
            return null;
          }

          out.close();
          Main.log.add("file downloaded");
        } catch (IOException ex) {
          Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
          Main.log.add(ex.toString());
          try { out.close(); } catch (IOException ioe) { Main.log.add(ioe.toString());
          }

        }

      }

    }

    return null;
  }

  protected void process(List<Integer> chunks)
  {
    super.process(chunks);
  }

  protected void done()
  {
    super.done();
  }

  public void propertyChange(PropertyChangeEvent evt) {
    if ("progress".equals(evt.getPropertyName()))
      Main.window.progressbar.setValue(getProgress());
  }
}