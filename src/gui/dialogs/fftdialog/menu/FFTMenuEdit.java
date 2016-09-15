/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs.fftdialog.menu;


import gui.dialogs.fftdialog.FFTDialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;


/**
 *
 * Okno FFT: Menu "Edycja"
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class FFTMenuEdit extends JMenu {
    
  /** Elementy menu */  
  private final JMenuItem undoItem, redoItem;
  
  
  /**
   * Konstruktor 
   * @param fftDialog Ref. do okna FFT
   */    
  public FFTMenuEdit(final FFTDialog fftDialog) {
       
    super("Edycja");
    setMnemonic(KeyEvent.VK_E);   
    
    undoItem = new JMenuItem("Cofnij");
    undoItem.setMnemonic(KeyEvent.VK_C);
    undoItem.setPreferredSize(new Dimension(160, 20));
    undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
    add(undoItem);    
    undoItem.setEnabled(false);
    undoItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {

        fftDialog.undo();  
        
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
          
        fftDialog.redo();
       
      }
    });          
    
    
    //add(new JSeparator());
    
         
    JMenuItem centralAreaItem = new JMenuItem("Maska: dodaj centralny obszar");
    centralAreaItem.setMnemonic(KeyEvent.VK_M);
    //add(centralAreaItem);
    
    
   }       
   
   
   
   
   /** 
    * Włączenie/wyłączenie opcji "Cofnij"
    * @param enabled True jeżeli włączone
    */
   public void enableUndo(boolean enabled) {
       
     undoItem.setEnabled(enabled);     
       
   } 
   
   /** 
    * Włączenie/wyłączenie opcji "Ponów"
    * @param enabled True jeżeli włączone
    */
   public void enableRedo(boolean enabled) {
       
     redoItem.setEnabled(enabled);
     
   }

    
}