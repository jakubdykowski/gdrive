package gdrive;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JList.DropLocation;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

public class BasicDnD extends JPanel
  implements ActionListener
{
  private static JFrame frame;
  private JTextArea textArea;
  private JTextField textField;
  private JList list;
  private JTable table;
  private JTree tree;
  private JColorChooser colorChooser;
  private JCheckBox toggleDnD;

  public BasicDnD()
  {
    super(new BorderLayout());
    JPanel leftPanel = createVerticalBoxPanel();
    JPanel rightPanel = createVerticalBoxPanel();

    DefaultTableModel tm = new DefaultTableModel();
    tm.addColumn("Column 0");
    tm.addColumn("Column 1");
    tm.addColumn("Column 2");
    tm.addColumn("Column 3");
    tm.addRow(new String[] { "Table 00", "Table 01", "Table 02", "Table 03" });
    tm.addRow(new String[] { "Table 10", "Table 11", "Table 12", "Table 13" });
    tm.addRow(new String[] { "Table 20", "Table 21", "Table 22", "Table 23" });
    tm.addRow(new String[] { "Table 30", "Table 31", "Table 32", "Table 33" });

    this.table = new JTable(tm);
    leftPanel.add(createPanelForComponent(this.table, "JTable"));

    this.colorChooser = new JColorChooser();
    leftPanel.add(createPanelForComponent(this.colorChooser, "JColorChooser"));

    this.textField = new JTextField(30);
    this.textField.setText("Favorite foods:\nPizza, Moussaka, Pot roast");
    rightPanel.add(createPanelForComponent(this.textField, "JTextField"));

    this.textArea = new JTextArea(5, 30);
    this.textArea.setText("Favorite shows:\nBuffy, Alias, Angel");
    JScrollPane scrollPane = new JScrollPane(this.textArea);
    rightPanel.add(createPanelForComponent(scrollPane, "JTextArea"));

    DefaultListModel listModel = new DefaultListModel();
    listModel.addElement("Martha Washington");
    listModel.addElement("Abigail Adams");
    listModel.addElement("Martha Randolph");
    listModel.addElement("Dolley Madison");
    listModel.addElement("Elizabeth Monroe");
    listModel.addElement("Louisa Adams");
    listModel.addElement("Emily Donelson");
    this.list = new JList(listModel);
    this.list.setVisibleRowCount(-1);
    this.list.getSelectionModel().setSelectionMode(2);

    this.list.setTransferHandler(new TransferHandler()
    {
      public boolean canImport(TransferHandler.TransferSupport info)
      {
        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
          return false;
        }

        JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();

        return dl.getIndex() != -1;
      }

      public boolean importData(TransferHandler.TransferSupport info)
      {
        if (!info.isDrop()) {
          return false;
        }

        if (!info.isDataFlavorSupported(DataFlavor.stringFlavor)) {
          BasicDnD.this.displayDropLocation("List doesn't accept a drop of this type.");
          return false; }
JList.DropLocation dl = (JList.DropLocation)info.getDropLocation();
        DefaultListModel listModel = (DefaultListModel)BasicDnD.this.list.getModel();
        int index = dl.getIndex();
        boolean insert = dl.isInsert();

        String value = (String)listModel.getElementAt(index);

        Transferable t = info.getTransferable();
        String data;
        try { data = (String)t.getTransferData(DataFlavor.stringFlavor);
        } catch (Exception e) {
          return false;
        }

        String dropValue = "\"" + data + "\" dropped ";
        if (dl.isInsert()) {
          if (dl.getIndex() == 0) {
            BasicDnD.this.displayDropLocation(dropValue + "at beginning of list");
          } else if (dl.getIndex() >= BasicDnD.this.list.getModel().getSize()) {
            BasicDnD.this.displayDropLocation(dropValue + "at end of list");
          } else {
            String value1 = (String)BasicDnD.this.list.getModel().getElementAt(dl.getIndex() - 1);
            String value2 = (String)BasicDnD.this.list.getModel().getElementAt(dl.getIndex());
            BasicDnD.this.displayDropLocation(dropValue + "between \"" + value1 + "\" and \"" + value2 + "\"");
          }
        }
        else BasicDnD.this.displayDropLocation(dropValue + "on top of " + "\"" + value + "\"");

        return false;
      }

      public int getSourceActions(JComponent c) {
        return 1;
      }

      protected Transferable createTransferable(JComponent c) {
        JList list = (JList)c;
        Object[] values = list.getSelectedValues();

        StringBuffer buff = new StringBuffer();

        for (int i = 0; i < values.length; i++) {
          Object val = values[i];
          buff.append(val == null ? "" : val.toString());
          if (i != values.length - 1) {
            buff.append("\n");
          }
        }
        return new StringSelection(buff.toString());
      }
    });
    this.list.setDropMode(DropMode.ON_OR_INSERT);

    JScrollPane listView = new JScrollPane(this.list);
    listView.setPreferredSize(new Dimension(300, 100));
    rightPanel.add(createPanelForComponent(listView, "JList"));

    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Mia Familia");
    DefaultMutableTreeNode sharon = new DefaultMutableTreeNode("Sharon");
    rootNode.add(sharon);
    DefaultMutableTreeNode maya = new DefaultMutableTreeNode("Maya");
    sharon.add(maya);
    DefaultMutableTreeNode anya = new DefaultMutableTreeNode("Anya");
    sharon.add(anya);
    sharon.add(new DefaultMutableTreeNode("Bongo"));
    maya.add(new DefaultMutableTreeNode("Muffin"));
    anya.add(new DefaultMutableTreeNode("Winky"));
    DefaultTreeModel model = new DefaultTreeModel(rootNode);
    this.tree = new JTree(model);
    this.tree.getSelectionModel().setSelectionMode(4);

    JScrollPane treeView = new JScrollPane(this.tree);
    treeView.setPreferredSize(new Dimension(300, 100));
    rightPanel.add(createPanelForComponent(treeView, "JTree"));

    this.toggleDnD = new JCheckBox("Turn on Drag and Drop");
    this.toggleDnD.setActionCommand("toggleDnD");
    this.toggleDnD.addActionListener(this);

    JSplitPane splitPane = new JSplitPane(1, leftPanel, rightPanel);

    splitPane.setOneTouchExpandable(true);

    add(splitPane, "Center");
    add(this.toggleDnD, "Last");
    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }

  protected JPanel createVerticalBoxPanel() {
    JPanel p = new JPanel();
    p.setLayout(new BoxLayout(p, 3));
    p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    return p;
  }

  public JPanel createPanelForComponent(JComponent comp, String title)
  {
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(comp, "Center");
    if (title != null) {
      panel.setBorder(BorderFactory.createTitledBorder(title));
    }
    return panel;
  }

  private void displayDropLocation(final String string) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        JOptionPane.showMessageDialog(null, string);
      } } );
  }

  public void actionPerformed(ActionEvent e) {
    if ("toggleDnD".equals(e.getActionCommand())) {
      boolean toggle = this.toggleDnD.isSelected();
      this.textArea.setDragEnabled(toggle);
      this.textField.setDragEnabled(toggle);
      this.list.setDragEnabled(toggle);
      this.table.setDragEnabled(toggle);
      this.tree.setDragEnabled(toggle);
      this.colorChooser.setDragEnabled(toggle);
    }
  }

  private static void createAndShowGUI()
  {
    frame = new JFrame("BasicDnD");
    frame.setDefaultCloseOperation(3);

    JComponent newContentPane = new BasicDnD();
    newContentPane.setOpaque(true);
    frame.setContentPane(newContentPane);

    frame.pack();
    frame.setVisible(true);
  }

  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run() {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        BasicDnD.createAndShowGUI();
      }
    });
  }
}