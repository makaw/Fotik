/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;


import gui.GUI;
import gui.SliderField;
import processing.ProcessedImage;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import fotik.IConf;


/**
 *
 * Okienko dialogowe z korekcją jasności i kontrastu obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class BrightnessContrastDialog extends ThumbnailDialog {
    
   /** Początkowy ("normalny") współczynnik jasności */ 
   private static final float initialOffset = 10.0f;
   /** Początkowy ("normalny") współczynnik kontrastu  */
   private static final float initialScaleFactor = 1.0f;
   
   /** Współczynnik jasności */
   private float offset;
   /** Współczynnik kontrastu */
   private float scaleFactor;
   /** Suwak do ustawienia jasności */
   private SliderField brightField;
   /** Suwak do ustawienia kontrastu */
   private SliderField contrastField;
   
   
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej i wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public BrightnessContrastDialog(GUI frame) {
        
     super(frame, IConf.APP_NAME + "- jasno\u015b\u0107/kontrast");

   }
      
   
   /**
    * Metoda wyświetlająca pola do zmiany wartości
    * @return Panel z suwakami wartości
    */
   @Override
   protected JPanel getFieldsPanel() { 
      
      brightField = new SliderField("Jasno\u015b\u0107", 0, -127, 127, GUI.normalFont);
      brightField.setValue(0);
      contrastField = new SliderField("Kontrast", 0, -127, 127, GUI.normalFont);
      contrastField.setValue(0);
      
      JPanel p = new JPanel();
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(15, 0, 0, 0));
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      
      p.add(brightField);
      p.add(contrastField);
      
      offset = initialOffset;
      scaleFactor = initialScaleFactor;
      
      // listenery
      brightField.addPropertyChangeListener(new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
        
           offset = initialOffset + (float)(double)evt.getNewValue()*2.0f;           
           if (offset>254) offset = 254.0f;
           if (offset<-254) offset = -254.0f;
           refresh();
           
        }
      });
      
      contrastField.addPropertyChangeListener(new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
        
           scaleFactor = initialScaleFactor + (float)(double)evt.getNewValue()*0.01f;
           if (scaleFactor>1.99f) scaleFactor = 1.99f;
           if (scaleFactor<0.01f) scaleFactor = 0.01f;
           refresh();
           
        }
      });                
      
      
      return p;
      
   }
   
   
   /**
    * Metoda wyświetlająca przyciski
    * @return Panel z przyciskami
    */   
   @Override
   protected JPanel getButtonsPanel() {
           
      JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      bPanel.setOpaque(false);      
      
      JButton okButton = new JButton("Wykonaj");
      okButton.setFocusPainted(false);
      okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
           frame.getPhotoPanel().changeImage(
                   frame.getPhotoPanel().getPhoto().getImage().getBCAdjustedCopy(offset, scaleFactor), false);           
           dispose();           
           
         }
      });    
      
      bPanel.add(okButton);
      
      
      JButton resButton = new JButton("Przywróć");
      resButton.setFocusPainted(false);
      resButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
           brightField.setValue(0);
           contrastField.setValue(0);
           offset = initialOffset;
           scaleFactor = initialScaleFactor;
           refresh();
             
         }
      });     
      
      bPanel.add(resButton);
      
      
      bPanel.add(new CloseButton());
      
      return bPanel;
      
   }
      

   /**
    * Odświeżenie miniatury obrazu
    */
   private void refresh() {
            

     BufferedImage dest = ProcessedImage.getBCAdjustedCopy(thumbImage, offset, scaleFactor);
     
     thumbnail.setIcon(new ImageIcon(dest));
     thumbnail.revalidate();
     thumbnail.repaint();     
     //System.out.println(offset + " / " + scaleFactor);
     
    }  
       
       
   
}

