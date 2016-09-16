/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.menu;

import gui.GUI;
import gui.dialogs.PreviewOriginalDialog;
import gui.dialogs.ZoomDialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


/**
 *
 * Menu "Edycja" aplikacji
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class MenuEdit extends JMenu {
    
  /** Elementy menu */  
  private final JMenuItem undoItem, redoItem, zoomItem, origItem;
  
  
  /**
   * Konstruktor 
   * @param frame Referencja do GUI
   */    
  public MenuEdit(final GUI frame) {
       
    super("Edycja");
    setMnemonic(KeyEvent.VK_E);
    
    undoItem = new JMenuItem("Cofnij");
    undoItem.setMnemonic(KeyEvent.VK_C);
    undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
    add(undoItem);    
    undoItem.setEnabled(false);
    undoItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
          
      frame.getPhotoPanel().photoUndo();      
       
    }
    });       
    
    
    
    redoItem = new JMenuItem("Pon\u00f3w");
    redoItem.setMnemonic(KeyEvent.VK_P);
    redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
    add(redoItem);    
    redoItem.setEnabled(false);
    redoItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
          
      frame.getPhotoPanel().photoRedo();      
       
    }
    });           
    
    
    origItem = new JMenuItem("Oryginalny obraz");    
    origItem.setMnemonic(KeyEvent.VK_O);
    origItem.setEnabled(false);
    add(origItem);
    origItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
       
        new PreviewOriginalDialog(frame);
          
      }
    });               
   
    
    zoomItem = new JMenuItem("Powi\u0119kszenie");
    zoomItem.setPreferredSize(new Dimension(180, 20));
    zoomItem.setMnemonic(KeyEvent.VK_W);
    zoomItem.setEnabled(false);
    add(zoomItem);
    zoomItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
       
        new ZoomDialog(frame);
          
      }
    });                    
    
   } 
   
   
   @Override
   public void setEnabled(boolean enabled) {
           
     zoomItem.setEnabled(enabled); 
       
   }
   
   
   
   /** 
    * Włączenie/wyłączenie opcji "Cofnij"
    * @param enabled True jeżeli włączone
    */
   public void enableUndo(boolean enabled) {
       
     undoItem.setEnabled(enabled);     
     origItem.setEnabled(enabled);
       
   } 
   
   /** 
    * Włączenie/wyłączenie opcji "Ponów"
    * @param enabled True jeżeli włączone
    */
   public void enableRedo(boolean enabled) {
       
     redoItem.setEnabled(enabled);
     
   }
       
    
}