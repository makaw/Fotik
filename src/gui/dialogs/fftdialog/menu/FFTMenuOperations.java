/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01-02/2016
 */
package gui.dialogs.fftdialog.menu;

import gui.dialogs.InfoDialog;
import gui.dialogs.fftdialog.FFTLowPassDialog;
import gui.dialogs.fftdialog.FFTDialog;
import gui.dialogs.fftdialog.FFTRemovePatternDialog;
import gui.photoview.Mask;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;


/**
 *
 * Okno FFT: Menu "Operacje"
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class FFTMenuOperations extends JMenu {
    
    
  /**
   * Konstruktor
   * @param fftDialog Ref. do okna z widmem
   */  
  public FFTMenuOperations(final FFTDialog fftDialog) {
       
     super("Operacje");
     setMnemonic(KeyEvent.VK_O);
     
     JMenuItem deleteMaskedItem = new JMenuItem("Usu\u0144 zamaskowane cz\u0119stotliwo\u015bci");
     deleteMaskedItem.setMnemonic(KeyEvent.VK_U);
     add(deleteMaskedItem);
     deleteMaskedItem.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
     
           Mask mask = fftDialog.getPhotoPanel().getPhoto().getMask();  
             
           if (mask.isEmpty()) 
             InfoDialog.errorDialog(fftDialog.getMainFrame(), "Nie zaznaczono \u017cadnych obszar\u00f3w na warstwie maski.");
           else
             fftDialog.getPhotoPanel().changeImage(fftDialog.getFft().getFftOp().removeMasked(
                  fftDialog.getPhotoPanel().getPhoto().getImage(), mask), false);
             
         }
     });
     
  
     JMenuItem regularItem = new JMenuItem("Wyszukaj i usu\u0144 regularne wzory");
     regularItem.setMnemonic(KeyEvent.VK_W);
     add(regularItem);
     regularItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {

        new FFTRemovePatternDialog(fftDialog);
       
      }
    });         
     
    JMenuItem lowPassItem = new JMenuItem("Filtr dolnoprzepustowy");
    lowPassItem.setMnemonic(KeyEvent.VK_F);
    add(lowPassItem);
    lowPassItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {

        new FFTLowPassDialog(fftDialog);
       
      }
    });              
     
  }
    
    
}
