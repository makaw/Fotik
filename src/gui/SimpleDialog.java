/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import fotik.IConf;

/**
 *
 * Klasa abstrakcyjna wykorzystywana do budowy wszystkich okienek dialogowych. 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 * 
 */
@SuppressWarnings("serial")
public abstract class SimpleDialog extends JDialog {
   
   /** Referencja do GUI */
   protected final GUI frame;
   
    
   /**
    * Konstruktor (dla rozszerzających klas) 
    * @param frame Referencja do GUI
    * @param title Nagłowek okna
    */  
   protected SimpleDialog(GUI frame, String title) {
       
      super((JFrame)frame, true);
      this.frame = frame;
      setTitle(title);
      
   }
   
   
   /**
    * Konstruktor (dla rozszerzających klas)
    * @param frame Referencja do GUI
    */  
   protected SimpleDialog(GUI frame) {
       
      this(frame, IConf.APP_NAME);
      
   }   
   
   
   /**
    * Klasa wewn. - komponent przycisku zamykajacego okienko dialogowe
    */
   @SuppressWarnings("serial")
   protected class CloseButton extends JButton {
   
    
      public CloseButton(String title) {
       
        super(title);
        setFocusPainted(false);
        addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
           
           dispose();
          
         }
       });
                    
     } 
    

     public CloseButton() {
           
        this("Zamknij"); 
           
     }       
    
  }   
   
   
   /**
    * Klasa wewn. -  komponent przycisku zamykającego program
    */
   @SuppressWarnings("serial")
   protected class QuitButton extends JButton {
   
    
      public QuitButton(String title) {
       
        super(title);
        setFocusPainted(false);
        addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
           
           dispose();
           frame.quitApp();
          
         }
       });
                    
     } 
    

     public QuitButton() {
           
        this("Zako\u0144cz"); 
           
     }       
    
  }   
   
 
   
   /**
    * Metoda abstrakcyjna, zawartość poszczególnych okienek dialogowych
    */
   protected abstract void  getContent();
   
   
    
   /**
    * Metoda wywołująca okienko dialogowe, wspólna dla wszystkich okienek (klas dziedziczących)
    * @param width Szerokość okienka w pikselach
    * @param height Wysokość okienka w pikselach
    */
   protected void showDialog(int width, int height) {
       
     setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); 

     getRootPane().setBorder( BorderFactory.createLineBorder(new Color(0x57,0x7A,0xD5)) );
       
     getContent();  
       
     pack();
     setSize(width, height);
     setLocationRelativeTo((JFrame)frame);
     setResizable(false);
     setVisible(true); 

   }
   
  
        
   

}

