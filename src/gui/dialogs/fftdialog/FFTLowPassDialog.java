/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01-02/2016
 */
package gui.dialogs.fftdialog;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import fotik.IConf;
import gui.dialogs.InfoDialog;
import gui.dialogs.PromptDialog;
import gui.loader.IProgress;
import gui.loader.IProgressInvoker;
import gui.loader.Loader;
import javax.swing.JTextField;
import processing.imagefft.ImageFFTOp;


/**
 *
 * Okienko dialogowe filtra dolnoprzepustowego widma
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class FFTLowPassDialog extends PromptDialog implements IProgressInvoker {
    
   /** Promień filtra */
   private int radius;    
       
   /**
    * Konstruktor, wyświetlenie okienka
    * @param fftDialog Okno z obrazem widma
    */ 
   public FFTLowPassDialog(FFTDialog fftDialog) {
        
     super(fftDialog, IConf.APP_NAME + " - 2D FFT - \"idealny\" filtr dolnoprz.", "Promie\u0144 filtra: ", 
             String.valueOf(ImageFFTOp.defaultLowPassRadius), 140);
            
   } 
      
   
   @Override
   protected ActionListener getButtonOkActionListener() {
       
     return new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
           radius = 0;  
           
           try {  
             radius = Integer.parseInt(((JTextField)answerField).getText());           
             if (radius<1) throw new NumberFormatException();
           }
           catch (NumberFormatException ex) {
             InfoDialog.errorDialog(frame, "Wprowadzono nieprawid\u0142owe dane (tylko liczby ca\u0142kowite dodatnie).");
             return;
           }
           
           new Loader(frame, FFTLowPassDialog.this, true).load();
           dispose();
           
         }
      };
              
   }
      
   
   @Override
   public void start(IProgress progress) {
     
     FFTDialog fftDialog = (FFTDialog)photoFrame;  
     fftDialog.getPhotoPanel().changeImage(fftDialog.getFft().getFftOp().
             lowPassFilter(fftDialog.getPhotoPanel().getPhoto().getImage(), radius, progress), false);      
       
   }
    
    
}
