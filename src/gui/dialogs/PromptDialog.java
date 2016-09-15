/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;

import gui.GUI;
import gui.SimpleDialog;
import gui.photoview.IPhotoFrame;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


/**
 *
 * Okienko dialogowe z pytaniem o dane
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class PromptDialog extends SimpleDialog {
    
   /** Pytanie */
   private final String question;
   /** Odpowiedź użytkownika */
   protected Object answer;
   /** Komponent do wpisania odpowiedzi */
   protected Component answerField;
   /** Interfejs okna z obrazem (opcjonalnie) */
   protected final IPhotoFrame photoFrame;
   
  /**
    * Konstruktor, wyświetlenie okienka
    * @param photoFrame Interfejs okna z obrazem
    * @param title Tytuł okna
    * @param question Pytanie do wyświetlenia w okienku
    * @param answer Domyślna odpowiedź
    * @param height Wysokosc okienka (px)
    */    
   public PromptDialog(IPhotoFrame photoFrame, String title, String question, String answer, int height) {
       
     super(photoFrame.getMainFrame(), title);
     this.question = question;
     this.answer = answer;
     this.photoFrame = photoFrame;
     super.showDialog(300, height); 
            
   }          
   
  /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    * @param title Tytuł okna
    * @param question Pytanie do wyświetlenia w okienku
    * @param answer Domyślna odpowiedź
    * @param height Wysokosc okienka (px)
    */    
   public PromptDialog(GUI frame, String title, String question, String answer, int height) {
       
     super(frame, title);
     this.question = question;
     this.answer = answer;
     this.photoFrame = null;
     super.showDialog(300, height); 
            
   }       
   
   
   public PromptDialog(GUI frame, String title, String question, String answer) {
       
     this(frame, title, question, answer, 140);
     
   }   
   
   
   public PromptDialog(GUI frame, String title, String question) {
       
     this(frame, title, question, null, 140);
     
   }      
   
   

   /**
    * Metoda pobierająca odpowiedź użytkownika
    * @return Odpowiedź użytkownika 
    */ 
   public Object getAnswer() {
       
      return answer;
       
   }
   
   
   
  /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected final void getContent()  {   

      JPanel p = new JPanel();
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(5, 5, 5, 5));
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      
      JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
      p2.setOpaque(false);
      
      JLabel txt = new JLabel(question);
      txt.setFont(GUI.normalFont);
      p2.add(txt);
                 
      setAnswerField();
      p2.add(answerField);
      
      if (getSymbol() != null) {
        txt = new JLabel(getSymbol());
        txt.setFont(GUI.normalFont);
        p2.add(txt);  
      }
      
      p.setBorder(new EmptyBorder(10, 20, 0, 0));
      p.add(p2);            
      
      JButton buttonOk = new JButton(" OK ");
      buttonOk.setFocusPainted(false);
      buttonOk.addActionListener(getButtonOkActionListener());
      
      
      JButton buttonClose = new JButton("Anuluj");
      buttonClose.setFocusPainted(false);
      
      buttonClose.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
             answer = null;
             dispose();
          }
      });
      
      p2 = new JPanel(new FlowLayout());
      p2.setOpaque(false);
      p2.setBorder(new EmptyBorder(0, 0, 5, 0)); 
      buttonClose.setAlignmentX(Component.CENTER_ALIGNMENT);
      p2.add(buttonOk);
      p2.add(new JLabel(" "));
      p2.add(buttonClose);
      
      p.add(p2);
      add(p);
      
   }   

   
   protected ActionListener getButtonOkActionListener() {
       
     return new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) { 
            answer = ((JTextField)answerField).getText();
            dispose();
         }
      };      
              
   }
   
   
   protected void setAnswerField() {
       
     answerField = new JTextField(5);
     ((JTextField)answerField).setText((String)answer);  
     answerField.setPreferredSize(new Dimension(150, 30));
       
   }
   
   
   protected String getSymbol() {
       
     return null;
       
   }
   
    
}

