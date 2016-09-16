/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;



import gui.BgPanel;
import gui.SimpleDialog;
import gui.ImageRes;
import gui.GUI;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import fotik.IConf;
import gui.TextPanel;


/**
 *
 * Okienko dialogowe z informacją o programie
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class AboutDialog extends SimpleDialog {
    
    
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    * @param title Nagłówek okna
    */ 
   public AboutDialog(GUI frame, String title) {
       
     super(frame, title);
     super.showDialog(340, 215); 
            
   } 
   
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public AboutDialog(GUI frame) {
       
     this(frame, IConf.APP_NAME + " - o programie");
            
   }    
    
   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected void getContent()  {   
     
      JPanel mainPanel = new BgPanel("bg_dialog.jpg");
      
      JPanel p = new JPanel();
      p.setOpaque(false);
      p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
       
      JLabel ico = new JLabel(ImageRes.getIcon("icon_large.png"));
      ico.setBorder(new EmptyBorder(12, 5, 5, 35));
      p.add(ico);
      
      JPanel p2 = new JPanel(new GridLayout(1,1));
      p2.setOpaque(false);
      
      // pole do umieszczenia tekstu
      TextPanel tx = new TextPanel();
      
      tx.setText(IConf.APP_NAME, true);
      tx.setText(" - wersja: " + IConf.APP_VERSION, false);
      tx.setText("\n\nPrzegl\u0105darka zdj\u0119\u0107 z funkcjami \n"
                + "umo\u017cliwiaj\u0105cymi poprawienie\njako\u015bci starych fotografii", false);
      
      p2.add(tx);
      p.add(p2);
            
      mainPanel.add(p);
      
      p = new JPanel(new FlowLayout());
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(0, 50, 0, 80));
      tx = new TextPanel();      
      tx.setText("Autor: Maciej Kawecki 2016", false);
      p.add(tx);
      mainPanel.add(p);                  
      
      JButton b = new CloseButton(" OK ");
      
      p = new JPanel(new FlowLayout());
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(5, 10, 0, 0)); 
      b.setAlignmentX(Component.CENTER_ALIGNMENT);
      
      p.add(b);
      mainPanel.add(p);
      
      add(mainPanel);
      
      
   }
    
}

