///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package qapps.gdrive.gui;
//
//import java.awt.Component;
//import javax.swing.JProgressBar;
//import javax.swing.JTable;
//import javax.swing.table.TableCellRenderer;
//import qapps.gdrive.Application;
//
///**
// *
// * @author root
// */
//public class ProgressRenderer implements TableCellRenderer {
//
//    private final JProgressBar bar = new JProgressBar(0, 100);
//    
//    {
//        bar.setBorderPainted(false);
//        bar.setStringPainted(true);
//    }
//
//    @Override
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        if (value instanceof Integer) {
//            Integer v = (Integer) value;
//                bar.setString(null);
//                bar.setValue(v);
////            }
//        } else if(value instanceof String) {
//            bar.setString(value.toString());
//        }
//        return bar;
//    } 
//}
