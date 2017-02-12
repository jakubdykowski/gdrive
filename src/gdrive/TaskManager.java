package gdrive;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

public class TaskManager extends Thread
{
  private boolean REMOVE_FINISHED = true;
  private boolean AUTOMATICALLY_START_TASK = true;
  private int ACTIVE_COUNT;
  private int FREE_PLACES;
  private int MAX_TASK_NUMBER;
  private boolean KEEP_PROCESSING;
  private final ArrayList<SwingWorker> active_tasks;
  private final ArrayList<SwingWorker> finished_tasks;
  private final ArrayList<SwingWorker> paused_tasks;
  private final ArrayList<SwingWorker> interrupted_tasks;
  final ArrayList<SwingWorker> tasks;
  public static final int FRESH = 0;
  public static final int RUNNING = 1;
  public static final int FINISHED = 2;
  public static final int PAUSED = 3;

  public TaskManager()
  {
    this.tasks = new ArrayList();
    this.active_tasks = new ArrayList();
    this.finished_tasks = new ArrayList();
    this.paused_tasks = new ArrayList();
    this.interrupted_tasks = new ArrayList();
    this.ACTIVE_COUNT = 0;
    this.MAX_TASK_NUMBER = 0;
    this.AUTOMATICALLY_START_TASK = false;
    this.KEEP_PROCESSING = true;
    setPriority(1);
  }
  public TaskManager(int max_active_task_number) {
    this();
    if (max_active_task_number >= -1) this.MAX_TASK_NUMBER = max_active_task_number; else this.MAX_TASK_NUMBER = -1;
  }

  public static void main(String[] args) throws Exception
  {
  }

  public void run() {
    TaskManagerWindow w = new TaskManagerWindow(); w.setVisible(false);

    while (this.KEEP_PROCESSING) {
      int newCount = 0;
      this.ACTIVE_COUNT = 0;

      synchronized (this) {
        for (int i = 0; i < this.tasks.size(); i++)
        {
          SwingWorker task = (SwingWorker)this.tasks.get(i);
          if (task.getState() == SwingWorker.StateValue.STARTED) {
            this.ACTIVE_COUNT += 1;
          } else if (task.getState() == SwingWorker.StateValue.PENDING) {
            newCount++;
            if (((this.MAX_TASK_NUMBER - this.ACTIVE_COUNT - newCount >= 0) || (this.MAX_TASK_NUMBER == 0)) && (this.MAX_TASK_NUMBER > -1))
            {
              if (this.ACTIVE_COUNT > 0) Main.window.statusBar.setVisible(true);
              if (this.REMOVE_FINISHED) task.addPropertyChangeListener(new DeleteAfterComplete(this));
              task.addPropertyChangeListener(new MonitorProgressBar(Main.window.progressbar));
              task.execute();
              this.ACTIVE_COUNT += 1;
              newCount--;
            }
          } else if ((task.getState() != SwingWorker.StateValue.DONE) || (this.MAX_TASK_NUMBER < 0));
          w.addLine(task.toString() + ": " + task.getState().name());
        }
      }
      if (getActiveCount() == 0)
        Main.window.statusBar.setVisible(false);
      else Main.window.statusBar.setVisible(true);
      if (newCount > 0) Main.window.queue.setText("w kolejce: " + newCount + "   "); else
        Main.window.queue.setText("");
      try {
        sleep(200L);
      } catch (InterruptedException ex) {
        Logger.getLogger(TaskManager.class.getName()).log(Level.SEVERE, null, ex);
      }
      w.clear();
    }
  }

  public synchronized void setRemoveFinished(boolean on) {
    this.REMOVE_FINISHED = on;
  }
  public synchronized void setMaxActiveTaskNumber(int number) {
    if (number >= -1) this.MAX_TASK_NUMBER = number;
  }

  private boolean isRunning(SwingWorker task)
  {
    synchronized (this.active_tasks) {
      return (task.getState() == SwingWorker.StateValue.STARTED) && (!task.isDone());
    }
  }

  private void delete(SwingWorker task)
  {
    synchronized (this.tasks) {
      this.tasks.remove(this.tasks.indexOf(task));
      this.active_tasks.remove(this.active_tasks.indexOf(task));
      this.ACTIVE_COUNT -= 1;
    }
  }

  public synchronized void add(SwingWorker task) {
    if ((task.getState() == SwingWorker.StateValue.PENDING) || (this.MAX_TASK_NUMBER == 0))
      this.tasks.add(task);
  }

  public synchronized int getCount()
  {
    return this.tasks.size();
  }
  public synchronized int getActiveCount() {
    int count = 0;
    for (int i = 0; i < this.tasks.size(); i++) {
      if (((SwingWorker)this.tasks.get(i)).getState() != SwingWorker.StateValue.STARTED) continue; count++;
    }
    return count;
  }
  public synchronized int getQueueSize() {
    int count = 0;
    for (int i = 0; i < this.tasks.size(); i++) {
      if (((SwingWorker)this.tasks.get(i)).getState() != SwingWorker.StateValue.PENDING) continue; count++;
    }
    return count;
  }
  public synchronized int getPausedCount() {
    int count = 0;
    for (int i = 0; i < this.tasks.size(); i++);
    return count;
  }
  public synchronized int getFinishedCount() {
    int count = 0;
    for (int i = 0; i < this.tasks.size(); i++) {
      if (!((SwingWorker)this.tasks.get(i)).isDone()) continue; count++;
    }
    return count;
  }
  public synchronized SwingWorker getTask(int index) {
    if ((index >= 0) && (index < this.tasks.size())) {
      return (SwingWorker)this.tasks.get(index);
    }
    return null;
  }
  public synchronized void removeFinished(long id) {
    for (int i = 0; i < this.tasks.size(); i++) {
      if (!((SwingWorker)this.tasks.get(i)).isDone()) continue; this.tasks.remove(i);
    }
  }
  public synchronized void remove(SwingWorker task) {
    if (this.tasks.contains(task))
      this.tasks.remove(task);
  }

  public void exit()
  {
    this.KEEP_PROCESSING = false;
  }

  class DeleteAfterComplete
    implements PropertyChangeListener
  {
    private final TaskManager taskManager;

    public DeleteAfterComplete(TaskManager tm)
    {
      this.taskManager = tm;
    }
    public void propertyChange(PropertyChangeEvent evt) {
      if (((evt.getSource() instanceof SwingWorker)) &&
        ("state".equals(evt.getPropertyName())) && (((SwingWorker)evt.getSource()).getState() == SwingWorker.StateValue.DONE))
        synchronized (this.taskManager) {
          this.taskManager.remove((SwingWorker)evt.getSource());
        }
    }
  }

  class MonitorProgressBar
    implements PropertyChangeListener
  {
    private final JProgressBar progressBar;

    public MonitorProgressBar(JProgressBar progressBar)
    {
      this.progressBar = progressBar;
    }
    public void propertyChange(PropertyChangeEvent evt) {
      if ((evt.getSource() instanceof SwingWorker)) {
        SwingWorker task = (SwingWorker)evt.getSource();
        if ("state".equals(evt.getPropertyName()))
        {
          if ((SwingWorker.StateValue)evt.getNewValue() == SwingWorker.StateValue.STARTED)
          {
            this.progressBar.setIndeterminate(true);
          }
          if ((SwingWorker.StateValue)evt.getNewValue() == SwingWorker.StateValue.DONE) {
            this.progressBar.setIndeterminate(true);
            this.progressBar.setValue(0);
            this.progressBar.setString("");
          }
        }
        else if (("progress".equals(evt.getPropertyName())) &&
          (((Integer)evt.getNewValue()).intValue() != 0)) {
          this.progressBar.setString(null);
          this.progressBar.setIndeterminate(false);
          this.progressBar.setValue(((Integer)evt.getNewValue()).intValue());
        }
      }
    }
  }

  class MyTask extends Thread
  {
    private int FLAG;
    SwingWorker t;

    MyTask()
    {
    }

    public void setFlag(int flag)
    {
      this.FLAG = flag;
    }

    public int getFlag() {
      return this.FLAG;
    }
  }
}