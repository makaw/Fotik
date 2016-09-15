/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;


import gui.GUI;
import gui.SliderField;
import gui.loader.IProgress;
import gui.loader.IProgressInvoker;
import gui.loader.Loader;
import processing.HSBFilter;
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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import fotik.IConf;

/**
 *
 * Okienko dialogowe z balansem HSB obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class HSBBalanceDialog extends ThumbnailDialog  implements IProgressInvoker {

   /** Suwak parametru odcienia */
   private SliderField hueField;
   /** Suwak parametru nasycenia */
   private SliderField saturationField;
   /** Suwak parametru jasności */
   private SliderField brightnessField;
   /** Przesunięcie odcienia */
   private float hOffset = 0f;
   /** Przesunięcie nasycenia */
   private float sOffset = 0f;
   /** Przesunięcie jasności */
   private float bOffset = 0f;
   
   
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public HSBBalanceDialog(GUI frame) {
        
     super(frame, IConf.APP_NAME + " - balans HSB (odcie\u0144, nasycenie, jasno\u015b\u0107)", 370);

   }
      
   
   /**
    * Metoda wyświetlająca zawartość okienka
    * @return  Panel z zaw.okienka
    */
   @Override
   protected JPanel getFieldsPanel() { 
      
      hueField = new SliderField("Odcie\u0144", 0, -127, 127, GUI.normalFont);
      hueField.setValue(0);       
      saturationField = new SliderField("Nasycenie", 0, -127, 127, GUI.normalFont);
      saturationField.setValue(0);       
      brightnessField = new SliderField("Jasno\u015b\u0107", 0, -127, 127, GUI.normalFont);
      brightnessField.setValue(0);

      
      JPanel p = new JPanel();
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(15, 0, 0, 0));
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      
      p.add(hueField);
      p.add(saturationField);
      p.add(brightnessField);
      
      // listenery
      hueField.addPropertyChangeListener(new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
        
           hOffset = (float)(double)evt.getNewValue();        
           SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  refresh();
               }
           });
                      
        }
      });     
      
      // listenery
      saturationField.addPropertyChangeListener(new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
        
           sOffset = (float)(double)evt.getNewValue();   
           SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  refresh();
               }
           });
                      
        }
      });            
      
      brightnessField.addPropertyChangeListener(new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
        
           bOffset = (float)(double)evt.getNewValue();         
           SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  refresh();
               }
           });
                      
        }
      });        
      
      return p;
      
   }
   
   
   
   @Override
   protected JPanel getButtonsPanel() {
           
      JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      bPanel.setOpaque(false);      
      
      JButton okButton = new JButton("Wykonaj");
      okButton.setFocusPainted(false);
      okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
           new Loader(frame, HSBBalanceDialog.this, true).load();                   
           dispose();       

         }
      });    
      
      bPanel.add(okButton);
      
      
      JButton resButton = new JButton("Przywróć");
      resButton.setFocusPainted(false);
      resButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
            hOffset = 0f;  hueField.setValue(0);
            sOffset = 0f;  saturationField.setValue(0);
            bOffset = 0f;  brightnessField.setValue(0);
            
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

     BufferedImage dest = new HSBFilter(thumbImage, hOffset, sOffset, bOffset).filter();             
          
     thumbnail.setIcon(new ImageIcon(dest));
     thumbnail.revalidate();
     thumbnail.repaint();     
     
    }  

    @Override
    public void start(IProgress progress) {
        
       frame.getPhotoPanel().changeImage
          (frame.getPhotoPanel().getPhoto().getImage().getHSBAdjustedCopy(hOffset, sOffset, bOffset, progress), false);  
       
    }
       
   
}

