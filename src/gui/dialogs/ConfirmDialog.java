/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;

import gui.GUI;
import gui.ImageRes;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


/**
 *
 * Okienko dialogowe z prośbą o potwierdzenie
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class ConfirmDialog extends SimpleInfoDialog {
    
   /** Odpowiedź użytkownika: true jeżeli potwierdził, false jeżeli nie */ 
   private boolean confirmed;
   
   
  /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    * @param question Pytanie do wyświetlenia w okienku
    * @param height Wysokosc okienka (px)
    */    
   public ConfirmDialog(GUI frame, String question, int height) {
       
     super(frame, question, height, ImageRes.getIcon("question.png"));
     confirmed = false;     
     super.showDialog(); 
            
   } 
   
   
   public ConfirmDialog(GUI frame, String question) {
       
      this(frame, question, 130);  
       
   }
    
   /**
    * Dolny panel z przyciskami
    * @return Dolny panel
    */
   @Override
   protected JPanel buttonPanel() {
       
      // przygotowanie przycisków Tak/Nie
      JButton buttonYes = new JButton("Tak");
      buttonYes.setFocusPainted(false);
      buttonYes.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) { 
            confirmed = true;
            dispose();
         }
      });
       
      JButton buttonNo = new CloseButton("Nie");
      
      JPanel p = new JPanel(new FlowLayout());
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(0, 40, 0, 0)); 
      p.add(buttonYes);
      p.add(buttonNo);
      
      return p;
            
   }
    

   /**
    * Metoda pobierająca odpowiedź użytkownika
    * @return Odpowiedź użytkownika: true jeżeli potwierdził, false jeżeli nie
    */ 
   public boolean isConfirmed() {
       
      return confirmed; 
       
   }
   
    
}

