/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs.fftdialog.menu;

import gui.dialogs.fftdialog.FFTDialog;
import gui.loader.Loader;
import gui.photoview.PhotoIO;
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
 * Okno FFT: Menu "Transformata"
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class FFTMenuTransform extends JMenu {
    
    
  /**
   * Konstruktor
   * @param fftDialog Ref. do okna z widmem
   */  
  public FFTMenuTransform(final FFTDialog fftDialog) {
        
       super("Transformata");
       setMnemonic(KeyEvent.VK_T);   
       
       add(new FFTMenuOperations(fftDialog));
       
       JMenuItem previewItem = new JMenuItem("Podgl\u0105d IFFT");
       previewItem.setPreferredSize(new Dimension(160, 20));
       previewItem.setMnemonic(KeyEvent.VK_P);
       previewItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              fftDialog.setLoaderPreview(true);
              new Loader(fftDialog.getMainFrame(), fftDialog, true).load();
           }
       });
       add(previewItem);
       
       JMenuItem doItem = new JMenuItem("Zastosuj IFFT");
       doItem.setMnemonic(KeyEvent.VK_I);
       doItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
              fftDialog.setLoaderPreview(false);
              new Loader(fftDialog.getMainFrame(), fftDialog, true).load();
           }
       });
       add(doItem);
       
       add(new JSeparator());
       
       JMenuItem saveFileAsItem = new JMenuItem("Zapisz obraz");
       saveFileAsItem.setPreferredSize(new Dimension(180, 20));
       saveFileAsItem.setMnemonic(KeyEvent.VK_A);
       saveFileAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
       add(saveFileAsItem);    
       saveFileAsItem.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {
           PhotoIO.saveImageAsFile(fftDialog);
         }
       });                  
       
       JMenuItem closeItem = new JMenuItem("Zamknij");
       closeItem.setMnemonic(KeyEvent.VK_Z);
       closeItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
       closeItem.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
               fftDialog.dispose();
           }
       });
       add(closeItem);
         
   }    


  
    
          
    
}
