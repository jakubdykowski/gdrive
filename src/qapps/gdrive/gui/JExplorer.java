/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import qapps.gdrive.ExploreController;
import qapps.gdrive.mvc.Explorator;
import qapps.io.AbstractFile;

/**
 *
 * @author root
 */
public class JExplorer extends JList {

    public static Object[] data;

    static {
        data = new Object[100];
        for (int i = 0; i < 100; i++) {
            data[i] = RandomAbstractFile.generate();
        }
    }

    public JExplorer() {
        super();
    }

    public JExplorer(Object[] data) {
        super(data);
    }

    public JExplorer(ListModel dataModel) {
        super(dataModel);
    }

    {
        setLayoutOrientation(JList.HORIZONTAL_WRAP);
        setVisibleRowCount(0);
        this.setFixedCellHeight(80);
        this.setFixedCellWidth(80);
        this.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
            }
        });
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    AbstractFile file = (AbstractFile) JExplorer.this.getSelectedValue();
                    System.out.println("doubleclick: " + e.getButton() + "| " + file.getName());
                } else {
                    System.out.println("click: " + e.getButton());

                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("JExplorer example");
        frame.setSize(640, 400);
        JList comp = new JExplorer(data);
        comp.setBackground(Color.red);

        File folder = new File(JExplorer.class.getResource("../resources/icons").getFile());
        IconHandler iconHandler = new CachedIconManager(folder);

        comp.setCellRenderer(new FileRenderer(iconHandler, DefaultIconIdentifier.get()));

        JScrollPane sp = new JScrollPane(comp);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        frame.getContentPane().add(sp);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ExploreController ctrl = new ExploreController(comp, new Explorator());
    }
}
