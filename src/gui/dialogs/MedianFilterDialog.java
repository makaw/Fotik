/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;


import gui.loader.Loader;
import gui.GUI;
import gui.loader.IProgress;
import gui.loader.IProgressInvoker;
import processing.MedianFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import fotik.IConf;
import java.awt.image.BufferedImage;


/**
 *
 * Okienko dialogowe filtra medianowego
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class MedianFilterDialog extends PromptDialog implements IProgressInvoker {
    
   /** Rozmiar maski filtra */
   private int size;    
       
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public MedianFilterDialog(GUI frame) {
        
     super(frame, IConf.APP_NAME + " - medianowy filtr odszumiaj\u0105cy", "Wielko\u015b\u0107 maski filtra: ", 
             String.valueOf(MedianFilter.defaultSize));
            
   } 
      
   
   @Override
   protected ActionListener getButtonOkActionListener() {
       
     return new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
           size = 0;  
           
           try {  
             size = Integer.parseInt(((JTextField)answerField).getText());           
             if (!MedianFilter.isSizeCorrect(size)) throw new NumberFormatException();
           }
           catch (NumberFormatException ex) {
             InfoDialog.errorDialog(frame, "Wprowadzono nieprawid\u0142owe dane (rozmiar to 3 lub wi\u0119cej, "
                     + "tylko liczby nieparzyste).");
             return;
           }
           
           new Loader(frame, MedianFilterDialog.this, true).load();
           dispose();
           
         }
      };
              
   }
      
   
   @Override
   public void start(IProgress progress) {
     
     BufferedImage img = frame.getPhotoPanel().getPhoto().getMaskImage().getMedianFilteredCopy(size, progress);
     frame.getPhotoPanel().changeImage(frame.getPhotoPanel().getPhoto().applyMaskImage(img), false);     
       
   }
    
    
}
