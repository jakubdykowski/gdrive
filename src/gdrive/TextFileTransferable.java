package gdrive;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

class TextFileTransferable
  implements Transferable
{
  File temp;

  public void dragEnter(DragSourceDragEvent evt)
  {
    System.out.println("dragEnter");

    DragSourceContext ctx = evt.getDragSourceContext();

    ctx.setCursor(DragSource.DefaultLinkDrop);
  }

  public void dragExit(DragSourceEvent evt) {
    System.out.println("dragExit");
    DragSourceContext ctx = evt.getDragSourceContext();
    ctx.setCursor(DragSource.DefaultCopyNoDrop);
  }
  public TextFileTransferable(File temp) throws IOException {
    System.out.println("TextFileTransferable");
    this.temp = temp;
  }

  public Object getTransferData(DataFlavor flavor) {
    System.out.println("getTransferData " + this.temp.getParent() + this.temp.getName() + " |" + this.temp.getAbsolutePath());

    List list = new ArrayList();
    list.add(this.temp);
    return list;
  }

  public DataFlavor[] getTransferDataFlavors()
  {
    System.out.println("getTransferDataFlavors");
    DataFlavor[] df = new DataFlavor[1];
    df[0] = DataFlavor.javaFileListFlavor;
    return df;
  }

  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
    System.out.println("isDataFlavorSupported");

    return flavor == DataFlavor.javaFileListFlavor;
  }
}