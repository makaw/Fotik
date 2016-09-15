/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.menu;

import gui.GUI;
import gui.dialogs.ConfirmDialog;
import gui.photoview.PhotoIO;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


/**
 *
 * Menu "Pliki" aplikacji
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class MenuFile extends JMenu {
    
  /** Elementy menu */
  private final JMenuItem saveFileItem, saveFileAsItem, closeFileItem;
  
  /**
   * Konstruktor 
   * @param frame Referencja do GUI
   */    
  public MenuFile(final GUI frame) {
       
    super("Plik");
    setMnemonic(KeyEvent.VK_P);
    
    JMenuItem menuItem = new JMenuItem("Otw\u00f3rz plik");
    menuItem.setPreferredSize(new Dimension(160, 20));
    menuItem.setMnemonic(KeyEvent.VK_O);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
    add(menuItem);    
    menuItem.addActionListener(new ActionListener() {
    @Override
      public void actionPerformed(final ActionEvent e) {
         PhotoIO.loadImageFromFile(frame);
      }
    });          
    
    saveFileItem = new JMenuItem("Zapisz plik");
    saveFileItem.setPreferredSize(new Dimension(160, 20));
    saveFileItem.setMnemonic(KeyEvent.VK_S);
    saveFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
    saveFileItem.setEnabled(false);
    add(saveFileItem);    
    saveFileItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {           
          PhotoIO.saveImageToFile(frame);
      }
            
    });       
    
    saveFileAsItem = new JMenuItem("Zapisz jako");
    saveFileAsItem.setPreferredSize(new Dimension(160, 20));
    saveFileAsItem.setMnemonic(KeyEvent.VK_A);
    saveFileAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
    saveFileAsItem.setEnabled(false);
    add(saveFileAsItem);    
    saveFileAsItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
         PhotoIO.saveImageAsFile(frame);
       }
    });           
    
    
    closeFileItem = new JMenuItem("Zamknij plik");
    closeFileItem.setPreferredSize(new Dimension(160, 20));
    closeFileItem.setMnemonic(KeyEvent.VK_Z);
    closeFileItem.setEnabled(false);
    add(closeFileItem);    
    closeFileItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
           
       boolean res = frame.getPhotoPanel().isChanged() ? 
               new ConfirmDialog(frame, "Czy na pewno zamkn\u0105\u0107 plik ?").isConfirmed() : true;
       if (res) frame.closeImageFile();
         
     }
    });       
       
      

        
    addSeparator();        
    
    
    menuItem = new JMenuItem("Koniec");
    menuItem.setMnemonic(KeyEvent.VK_K);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(final ActionEvent e) {
           
        boolean res = new ConfirmDialog(frame, "Czy na pewno zako\u0144czy\u0107 ?").isConfirmed();
        if (res) frame.quitApp();
          
       }
    });   
    
  } 
  

  @Override
  public void setEnabled(boolean enabled) {
            
     saveFileAsItem.setEnabled(enabled);
     closeFileItem.setEnabled(enabled);
      
  }
   
    
  public void enableSave(boolean enabled) {
      
     saveFileItem.setEnabled(enabled);  
      
  }
  
}
