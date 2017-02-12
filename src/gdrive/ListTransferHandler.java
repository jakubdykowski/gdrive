package gdrive;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

public class ListTransferHandler extends TransferHandler
{
  private int[] indices = null;
  private int addIndex = -1;
  private int addCount = 0;

  public void exportAsDrag(JComponent comp, InputEvent e, int action)
  {
    System.out.println("exportAsDrag");
    super.exportAsDrag(comp, e, action);
  }

  public boolean canImport(TransferHandler.TransferSupport info)
  {
    if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
      System.out.println("can't import-unsupported");
      return false;
    }
    return true;
  }

  protected Transferable createTransferable(JComponent c)
  {
    System.out.println("createTransferable");
    return new StringSelection(exportString(c));
  }

  public int getSourceActions(JComponent c)
  {
    System.out.println("getSourcesActions");
    return 3;
  }

  public boolean importData(TransferHandler.TransferSupport info)
  {
    if (!info.isDrop()) { System.out.println("that's no drop");
      return false;
    }
    Transferable t = info.getTransferable();
    List li = null;
    try {
      li = (List)t.getTransferData(DataFlavor.javaFileListFlavor);
    } catch (UnsupportedFlavorException ex) {
      Logger.getLogger(ListTransferHandler.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(ListTransferHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    File[] file = new File[li.size()];
    for (int i = 0; i < li.size(); i++) {
      file[i] = ((File)li.get(i));
    }

    Main.upload = new Upload(file);
    Main.upload.start();
    System.out.println("koniec importData");
    return true;
  }

  protected void exportDone(JComponent c, Transferable data, int action)
  {
    System.out.println("exportDone");
    cleanup(c, action == 2);
  }

  protected String exportString(JComponent c)
  {
    System.out.println("exportString");
    JList list = (JList)c;
    this.indices = list.getSelectedIndices();
    Object[] values = list.getSelectedValues();

    StringBuffer buff = new StringBuffer();

    for (int i = 0; i < values.length; i++) {
      Object val = values[i];
      buff.append(val == null ? "" : val.toString());
      if (i != values.length - 1) {
        buff.append("\n");
      }
    }

    return buff.toString();
  }

  protected void importString(JComponent c, String str)
  {
    System.out.println("importString");
    JList target = (JList)c;
    DefaultListModel listModel = (DefaultListModel)target.getModel();
    int index = target.getSelectedIndex();

    if ((this.indices != null) && (index >= this.indices[0] - 1) && (index <= this.indices[(this.indices.length - 1)]))
    {
      this.indices = null;
      return;
    }

    int max = listModel.getSize();
    if (index < 0) {
      index = max;
    } else {
      index++;
      if (index > max) {
        index = max;
      }
    }
    this.addIndex = index;
    String[] values = str.split("\n");
    this.addCount = values.length;
    for (int i = 0; i < values.length; i++)
      listModel.add(index++, values[i]);
  }

  protected void cleanup(JComponent c, boolean remove)
  {
    System.out.println("cleanup");
    if ((remove) && (this.indices != null)) {
      JList source = (JList)c;
      DefaultListModel model = (DefaultListModel)source.getModel();

      if (this.addCount > 0) {
        for (int i = 0; i < this.indices.length; i++) {
          if (this.indices[i] > this.addIndex) {
            this.indices[i] += this.addCount;
          }
        }
      }
      for (int i = this.indices.length - 1; i >= 0; i--) {
        model.remove(this.indices[i]);
      }
    }
    this.indices = null;
    this.addCount = 0;
    this.addIndex = -1;
  }
}