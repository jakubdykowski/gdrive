/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qapps.gdrive.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;
import qapps.gdrive.TransferManagerDerecated;
import qapps.gdrive.event.TransferEvent;
import qapps.gdrive.event.TransferListener;
import qapps.gdrive.util.Transfer;

/**
 *
 * @author root
 */
public class TransferTableModel extends AbstractTableModel {

    private final TransferManagerDerecated manager;
    private final static String[] COLUMN_NAMES = {"Nazwa", "Status", "State", "Pobrano", "Postęp"};
    private ArrayList<Object[]> rows = new ArrayList<Object[]>();

    public TransferTableModel(TransferManagerDerecated transferManager) {
        super();
        this.manager = transferManager;
        this.manager.addTransferListener(new TransferLifetime());
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex)[columnIndex + 1];
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    public synchronized void add(Transfer transfer) {
        Object[] data = new Object[COLUMN_NAMES.length + 1];
        data[0] = transfer;
        data[1] = transfer.getName();
        data[2] = 0;
        data[3] = 0;
        data[4] = 0;
        data[5] = 0;
//        System.out.println("added " + Arrays.toString(data));
        transfer.addPropertyChangeListener(new ProgressUpdater(transfer));
        if (rows.add(data)) {
            fireTableRowsInserted(rows.size() - 1, rows.size() - 1);
        }
    }

    private void setValue(Transfer transfer, int column, Object value) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i)[0].equals(transfer)) {
                rows.get(i)[column + 1] = value;
                //fire change
                this.fireTableCellUpdated(i, column);
            }
        }
    }

    private synchronized Transfer remove(Transfer transfer) {
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i)[0].equals(transfer)) {
                Object result = rows.remove(i)[0];
                //fire change
                this.fireTableRowsDeleted(i, i);
                return (Transfer) result;
            }
        }
        return null;
    }

    private class TransferLifetime implements TransferListener {

        @Override
        public void transfer(TransferEvent evt) {
//            System.out.println("new Transfer");
            if (evt.isStarted()) {
                add(evt.getTransfer());
            } else if (evt.isFinished()) {
                remove(evt.getTransfer());
            }
        }
    }

    private class ProgressUpdater implements PropertyChangeListener {

        private final Transfer transfer;

        public ProgressUpdater(Transfer transfer) {
            this.transfer = transfer;
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            int column = -1;
            if (evt.getPropertyName().equals("progress")) {
                column = Column.ALREADY.ordinal();
                setValue(transfer, Column.PROGRESS.ordinal(), ((Integer)evt.getNewValue())*100/transfer.getSize());
            } else if (evt.getPropertyName().equals("status")) {
                column = Column.STATUS.ordinal();
            } else if (evt.getPropertyName().equals("state")) {
                column = Column.STATE.ordinal();
            } else {
                return;
            }
            setValue(transfer, column, evt.getNewValue());
        }
    }

    public enum Column {

        NAME, STATUS, STATE, ALREADY, PROGRESS
    }
}
