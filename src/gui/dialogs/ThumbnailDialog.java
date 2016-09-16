/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;


import gui.GUI;
import gui.SimpleDialog;
import processing.ProcessedImage;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 *
 * Ogólny szablon okienka dialogowego z miniaturą obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class ThumbnailDialog extends SimpleDialog {

    
   /** Obraz źródłowy */
   protected final ProcessedImage image;
   /** Miniatura (obraz) */
   protected final BufferedImage thumbImage;
   /** Miniatura (komponent) */
   protected final JLabel thumbnail;
  
   
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej i wyświetlenie okienka
    * @param frame Referencja do GUI
    * @param title Nagłówek okna
    * @param height Wysokość okna (px)
    */ 
   protected ThumbnailDialog(GUI frame, String title, int height) {
        
     super(frame, title);

     image = frame.getPhotoPanel().getPhoto().getImage();
     thumbImage = image.getPScaledCopy(180, 150);
     thumbnail = new JLabel(new ImageIcon(thumbImage));
     
     super.showDialog(500, height); 
            
   } 
   
   
   protected ThumbnailDialog(GUI frame, String title) {
       
     this(frame, title, 270);  
       
   }
   
   
   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected final void getContent()  {   

      JPanel p0 = new JPanel();
      p0.setOpaque(false);
      p0.setBorder(new EmptyBorder(5, 5, 5, 5));
      p0.setLayout(new BoxLayout(p0, BoxLayout.Y_AXIS));
       
      JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
      p.setOpaque(false);
           
      thumbnail.setBorder(new LineBorder(Color.DARK_GRAY, 1));
      p.add(thumbnail);
      
      
      p.add(getFieldsPanel());           
      p0.add(p);
      
      p0.add(getButtonsPanel());
      add(p0);
      
   }   
   
   /**
    * Metoda wyświetlająca pola do zmiany wartości
    * @return Panel z suwakami wartości
    */   
   protected abstract JPanel getFieldsPanel();
   
   /**
    * Metoda wyświetlająca przyciski
    * @return Panel z przyciskami
    */    
   protected abstract JPanel getButtonsPanel();
       
       
   
}

