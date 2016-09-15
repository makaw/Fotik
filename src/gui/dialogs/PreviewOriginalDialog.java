/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;

import fotik.IConf;
import gui.ImageRes;
import gui.photoview.IPhotoFrame;
import processing.ProcessedImage;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;


/**
 *
 * Okienko dialogowe z podglądem oryginalnego obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class PreviewOriginalDialog extends PreviewDialog {
   

   /**
    * Konstruktor, wyświetlenie okienka
    * @param photoFrame Interfejs okna z panelem obrazu
    */ 
   public PreviewOriginalDialog(IPhotoFrame photoFrame) {
        
     super(photoFrame.getMainFrame(), IConf.APP_NAME + " - podgl\u0105d oryginalnego obrazu", 
             new ProcessedImage(photoFrame.getPhotoPanel().getOrigImage().getImage()));     
            
   } 
   

   
   
   /**
    * Dodatkowy komponent do paska przycisków
    * @return Przycisk przywrócenia oryginalnego obrazu
    */   
   @Override
   protected Component getAdditionalComponent() {
       
 
     JButton undoButton = new JButton("Przywr\u00f3\u0107 oryg.", ImageRes.getIcon("icons/undo.png"));
     undoButton.setToolTipText("Przywr\u00f3\u0107 oryginalny obraz");
     undoButton.setFocusPainted(false);
     undoButton.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
             
             boolean res = new ConfirmDialog(frame, 
                     "Czy na pewno chcesz cofn\u0105\u0107 wszystkie zmiany ?").isConfirmed();
             if (res) {                 
               frame.getPhotoPanel().setInitImage(photoPanel.getPhoto().getImage());
               dispose();
             }
          }

     });
     
     return undoButton;
              
   }
   
}
