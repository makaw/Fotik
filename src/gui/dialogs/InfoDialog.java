/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;

import gui.ImageRes;
import gui.GUI;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


/**
 *
 * Informacyjne okienka dialogowe
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class InfoDialog extends SimpleInfoDialog {
        
    
   /** Klasa wewn. : typ okienka */ 
   protected enum DialogType { 
       
     INFO, WARNING, ERROR; 
   
     protected String getTitle() {
         
       switch (this) {
           
           default:
           case INFO: return "";
           case WARNING: return "ostrze\u017cenie";
           case ERROR: return "b\u0142\u0105d";
           
       }  

     }     
     
     protected String getHeader() {
         
       switch (this) {
           
           default:
           case INFO: return "";
           case WARNING: return "Uwaga:          ";
           case ERROR: return "Wyst\u0105pi\u0142 b\u0142\u0105d:";
           
       }  

     }     
     
     
     protected Icon getIcon() {
         
       switch (this) {
           
           default:
           case INFO: return ImageRes.getIcon("info.png");
           case WARNING: return ImageRes.getIcon("warning.png");
           case ERROR: return  ImageRes.getIcon("error.png");
           
       }           
         
     }
        
   } 
    
   
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    * @param type Typ okna
    * @param info Tresc komunikatu
    * @param height Wysokość okienka (px)
    * @param scroll Czy ma byc przewijanie info
    */ 
   protected InfoDialog(GUI frame, DialogType type, String info, int height, boolean scroll) {
       
     super(frame, type.getTitle(), type.getHeader(), info, height, type.getIcon(), scroll);    
            
   } 
   
   
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do  GUI
    * @param info Tresc komunikatu
    * @param height Wysokość okienka (px)
    */ 
   public InfoDialog(GUI frame, String info, int height) {
       
     this(frame, DialogType.INFO, info, height, false);
            
   }    
   
   
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    * @param type Typ okna
    * @param info Tresc komunikatu
    */ 
   public InfoDialog(GUI frame, DialogType type, String info) {
       
     this(frame, type, info, 200, false); 
            
   }    

   
   /**
    * Dolny panel z przyciskami
    * @return Dolny panel
    */
   @Override
   protected JPanel buttonPanel() {
       
     // przycisk zamykający okienko
      JButton okButton = new CloseButton(" OK ");
      
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      p.setOpaque(false);
      p.add(okButton);
      p.setBorder(new EmptyBorder(0, 0, 5, 0));
      
      return p;

   }
   
   
   /** 
    * Statyczna metoda wywołująca okienko z błędem
    * @param frame Ref. do GUI
    * @param text Komunikat błędu
    */
   public static void errorDialog(GUI frame, String text) {
       
      new InfoDialog(frame, DialogType.ERROR, text).showDialog();
       
   }
   
      
   /** 
    * Statyczna metoda wywołująca okienko z ostrzeżeniem
    * @param frame Ref. do GUI
    * @param text Komunikat ostrzeżenia
    */
   public static void warningDialog(GUI frame, String text) {
       
      new InfoDialog(frame, DialogType.WARNING, text).showDialog();
       
   }
   
   
   /**
    * Statyczna metoda wywołująca okienko z informacją
    * @param frame Ref. do GUI
    * @param text Komunikat błędu
    */
   public static void infoDialog(GUI frame, String text) {
       
      new InfoDialog(frame, DialogType.INFO, text).showDialog();
       
   }   
   
   
}

