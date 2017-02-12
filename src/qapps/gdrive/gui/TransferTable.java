/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.gui;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author root
 */
public class TransferTable extends JTable {

    public TransferTable() {
        super();
    }

    public TransferTable(TableModel dm) {
        super(dm);
//        this.getColumnModel().getColumn(TransferTableModel.Column.PROGRESS.ordinal()).setCellRenderer(new ProgressRenderer());
    }

    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
            if(dataModel instanceof TransferTableModel) {
//                this.getColumnModel().getColumn(TransferTableModel.Column.PROGRESS.ordinal()).setCellRenderer(new ProgressRenderer());
            }
    }
}
