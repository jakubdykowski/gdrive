package gdrive;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintStream;

class MyPropertyChangeListener
  implements PropertyChangeListener
{
  public void propertyChange(PropertyChangeEvent pce)
  {
    System.out.println("droplocation changerd");
    System.out.println("old " + pce.getOldValue());
    System.out.println("new " + pce.getNewValue());
  }
}