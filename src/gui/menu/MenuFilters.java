/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.menu;


import gui.GUI;
import gui.dialogs.AdaptiveMedianFilterDialog;
import gui.dialogs.LinearFilterDialog;
import gui.dialogs.MedianFilterDialog;
import gui.dialogs.SmartBlurFilterDialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;



/**
 *
 * Menu "Filtry"  
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 * 
 */
public class MenuFilters extends JMenu {
    
   /** Elementy menu */ 
   private final JMenuItem linearItem, smartBlurItem, medianItem, adaptiveItem;
   
   /** Ref. do GUI */
   private final GUI frame;
    
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    */    
   public MenuFilters(GUI frame) {
       
     super("Filtry");
     setMnemonic(KeyEvent.VK_F);
    
     this.frame = frame;
    
     linearItem = new JMenuItem("Filtry liniowe (splotowe)");
     linearItem.setMnemonic(KeyEvent.VK_L);
     linearItem.setPreferredSize(new Dimension(160, 20));
     linearItem.setEnabled(false);
     add(linearItem);
    
     linearItem.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(final ActionEvent e) {               
       
        new LinearFilterDialog(MenuFilters.this.frame);
          
       }
     });   
     
     
     smartBlurItem = new JMenuItem("Filtr \"Smart blur\"");
     smartBlurItem.setMnemonic(KeyEvent.VK_B);
     smartBlurItem.setEnabled(false);
     add(smartBlurItem);
     
     smartBlurItem.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            
           new SmartBlurFilterDialog(MenuFilters.this.frame);
             
         }
         
     });
    
     medianItem = new JMenuItem("Filtr medianowy");
     medianItem.setMnemonic(KeyEvent.VK_M);     
     medianItem.setEnabled(false);
     add(medianItem);
    
     medianItem.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(final ActionEvent e) {               
        
          new MedianFilterDialog(MenuFilters.this.frame);
          
       }
     });   
                
    
    adaptiveItem = new JMenuItem("Adaptacyjny filtr medianowy");
    adaptiveItem.setMnemonic(KeyEvent.VK_A);
    adaptiveItem.setPreferredSize(new Dimension(220, 20));
    adaptiveItem.setEnabled(false);
    add(adaptiveItem);         
    
    adaptiveItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {               
        
        new AdaptiveMedianFilterDialog(MenuFilters.this.frame);
          
       }
    });       
          
    
   }
   
   
   @Override
   public void setEnabled(boolean enabled) {
      
     super.setEnabled(enabled);
     linearItem.setEnabled(enabled);
     smartBlurItem.setEnabled(enabled);
     medianItem.setEnabled(enabled);
     adaptiveItem.setEnabled(enabled);
      
  }

   
    
}
