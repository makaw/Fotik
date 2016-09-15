/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;


import gui.GUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import fotik.IConf;


/**
 *
 * Okienko dialogowe dowolnego powiększenia obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class ZoomDialog extends PromptDialog {          
          
   /** Maksymalne powiększenie (%) */
   private final static int maxZoom = 3000; 
   
    
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public ZoomDialog(GUI frame) {
        
      super(frame, IConf.APP_NAME + " - dowolne powi\u0119kszenie widoku", "Powi\u0119kszenie: ", 
             String.valueOf((int)(frame.getPhotoPanel().getPhoto().getZoom())));  
            
   } 
   
   
   @Override
   protected ActionListener getButtonOkActionListener() {
       
     return new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
           try {  
             int zoom = Integer.parseInt(((JTextField)answerField).getText());
             if (zoom<1 || zoom>maxZoom) throw new NumberFormatException();
             frame.setZoomSliderFreeValue(zoom);
             frame.getPhotoPanel().setZoom(zoom);
             frame.setZoomSliderFreeValue(zoom);
             
             dispose();
           }
           catch (NumberFormatException ex) {
             InfoDialog.errorDialog(frame, "Wprowadzono nieprawid\u0142owe dane (zakres 1-"
                                    + String.valueOf(maxZoom) + " %).");               
           }
           
           
         }
      };  
       
       
   }

   
   @Override
   protected String getSymbol() {
       
     return "%";  
       
   }
    
    
}
