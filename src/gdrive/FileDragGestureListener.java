package gdrive;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.PrintStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import sun.awt.shell.ShellFolder;

class FileDragGestureListener extends DragSourceAdapter
  implements DragGestureListener
{
  JTextArea text;
  java.io.File temp;
  DragGestureEvent dge;
  Cursor cursor;
  File file;

  public FileDragGestureListener(File file)
  {
    this.file = file;
  }

  public void dragGestureRecognized(DragGestureEvent evt) {
    this.dge = evt;
    try
    {
      java.io.File proxy_temp = java.io.File.createTempFile("tempdir", ".dir", null);
      this.temp = new java.io.File(proxy_temp.getParent(), this.file.filename + this.file.type);
      proxy_temp.delete();
      Main.dragged_file = this.file;
      Main.dragged_temp = this.temp;

      FileOutputStream out = new FileOutputStream(this.temp);

      out.close();

      ShellFolder sf = ShellFolder.getShellFolder(this.temp);
      Icon icn = new ImageIcon(sf.getIcon(true));
      Toolkit tk = Toolkit.getDefaultToolkit();
      Dimension dim = tk.getBestCursorSize(icn.getIconWidth(), icn.getIconHeight());

      BufferedImage buff = new BufferedImage(dim.width, dim.height, 2);
      icn.paintIcon(this.file, buff.getGraphics(), 0, 0);

      if (DragSource.isDragImageSupported()) { System.out.println("ds ");
        evt.startDrag(DragSource.DefaultCopyDrop, buff, new Point(0, 0), new TextFileTransferable(this.temp), this); } else {
        System.out.println("dse ");
        this.cursor = tk.createCustomCursor(buff, new Point(0, 0), "billy");
        evt.startDrag(this.cursor, null, new Point(0, 0), new TextFileTransferable(this.temp), this);
      }
    }
    catch (Exception e) {
    }
  }

  public void dragEnter(DragSourceDragEvent evt) {
    System.out.println("dragEnter");

    DragSourceContext ctx = evt.getDragSourceContext();

    ctx.setCursor(this.cursor);
  }

  public void dragExit(DragSourceEvent evt)
  {
    System.out.println("dragExit");
    DragSourceContext ctx = evt.getDragSourceContext();
    ctx.setCursor(DragSource.DefaultCopyNoDrop);
  }

  public void dragOver(DragSourceDragEvent dsde)
  {
    super.dragOver(dsde);
  }

  public void dragDropEnd(DragSourceDropEvent dsde)
  {
    System.out.println("dragDropEnd");
    this.temp.delete();

    super.dragDropEnd(dsde);
  }

  public void dropActionChanged(DragSourceDragEvent dsde) {
    System.out.println("dropActionChanged");
    super.dropActionChanged(dsde);
  }

  public void dragMouseMoved(DragSourceDragEvent dsde) {
    System.out.println("dragMousemoved");
  }
}