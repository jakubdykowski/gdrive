///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package qapps.gdrive.event;
//
//import java.util.EventListener;
//import java.util.EventObject;
//import javax.swing.event.EventListenerList;
//
///**
// *
// * @author root
// */
//public class EventUtilities {
//
//    public  static <T extends EventObject> void  fire(
//            Class<? extends EventListener> l,
//            Class<T> e,
//            T evt,
//            EventListenerList list) {
//        Object[] listeners = list.getListenerList();
//        for(int i = 0; i < listeners.length; i += 2) {
//            if(listeners[i] instanceof ErrorListener) {
//                ErrorListener errorListener = (ErrorListener) listeners[i + 1];
//                errorListener.error(e.cast(evt));
//            }
//        }
//    }
//}
