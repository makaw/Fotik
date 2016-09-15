/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;

import gui.GUI;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import fotik.IConf;


/**
 *
 * Okienko dialogowe rotacji obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class RotateDialog extends PromptDialog {
    
   /** Wewn. klasa - elementy listy wyboru */
   private class Degrees {
             
      private final int deg;         
      
      public Degrees(int deg) {
         this.deg = deg;   
      }
      
      @Override
      public String toString() {          
         return (deg>0 ? "+" : "") + " " + deg + "\u00b0";             
      }
              
   }
    
   /** Lista wyboru */
   private JComboBox<Degrees> rotBox; 
      
   
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public RotateDialog(GUI frame) {
        
     super(frame, IConf.APP_NAME + " - rotacja", "Rotacja: ");
            
   } 
   
   
   @Override
   protected ActionListener getButtonOkActionListener() {
       
     return new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
                      
           int deg = ((Degrees)(rotBox.getSelectedItem())).deg;  
           frame.getPhotoPanel().changeImage(frame.getPhotoPanel().getPhoto().getImage().getRotatedCopy(deg));
           dispose();
           
         }
      };     
       
       
   }
   

   @Override
   protected void setAnswerField() {
       
     // lista wyboru
     rotBox = new JComboBox<>();
     rotBox.setFont(GUI.normalFont);
     rotBox.setPreferredSize(new Dimension(200, 30));
     rotBox.addItem(new Degrees(-90));
     rotBox.addItem(new Degrees(90));
     rotBox.addItem(new Degrees(180));  
     rotBox.setSelectedIndex(0);
     answerField = rotBox;
       
   }
    
    
}
