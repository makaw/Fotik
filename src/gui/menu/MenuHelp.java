/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.menu;


import gui.GUI;
import gui.dialogs.AboutDialog;
import gui.dialogs.ManualDialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


/**
 *
 * Menu "Pomoc" 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class MenuHelp extends JMenu {
    
   /**
    * Konstruktor 
    * @param frame Referencja do GUI 
    */    
   public MenuHelp(final GUI frame) {
       
    super("Pomoc");
    setMnemonic(KeyEvent.VK_C);
    
    JMenuItem menuItem = new JMenuItem("Opis");
    menuItem.setPreferredSize(new Dimension(140, 20));
    menuItem.setMnemonic(KeyEvent.VK_O);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        
        new ManualDialog(frame);  
          
      }
    });        
        
    
    menuItem = new JMenuItem("O programie");
    menuItem.setMnemonic(KeyEvent.VK_P);
    add(menuItem);
    menuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
     
        new AboutDialog(frame);
          
      }
    });        
    
   } 
    
    
}
