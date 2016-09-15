/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;

import gui.GUI;
import gui.SimpleDialog;
import java.io.IOException;
import fotik.IConf;
import gui.ImageRes;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;


/**
 *
 * Okienko dialogowe z opisem programu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class ManualDialog extends SimpleDialog {
    
   /**
    * Konstruktor,wyświetlenie okienka
    * @param frame Referencja do GUI
    */     
    public ManualDialog(GUI frame) {
     
      super(frame, IConf.APP_NAME + " - opis programu");      
      super.showDialog(500, 400);
        
    }    
   
   /**
    * Metoda wyświetlająca zawartość okienka
    */    
    @Override
    protected void getContent()  {
        
       setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
       
       JTextPane tx = new JTextPane();
       tx.setEditable(false);
       // pozycja kursora na poczatku pola tekstowego
       ((DefaultCaret)tx.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

       tx.setBackground(new Color(0xddecfa));       
   
       // wczytanie pliku html
       try {
            
           tx.setPage(getClass().getResource("/resources/manual.html"));
          
       } catch (IOException e) {
            
           System.err.println("Brak pliku /resources/manual.html");
          
       }

      
       // dodanie paska przewijania
       JScrollPane sc = new JScrollPane(tx, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
       add(sc);
       
       
       JButton b = new CloseButton("Zamknij");
       b.setIcon(ImageRes.getIcon("icons/clear.png"));
       JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
       p.setPreferredSize(new Dimension(500, 40));
       p.setBorder(new EmptyBorder(5, 0, 5, 0));
       sc.setPreferredSize(new Dimension(500, 350));
       p.add(b);
       add(p);       
       
      
    }
    
 
    
}
