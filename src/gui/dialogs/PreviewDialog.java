/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;

import gui.BottomToolBar;
import gui.GUI;
import gui.SimpleDialog;
import gui.photoview.IPhotoFrame;
import gui.photoview.PhotoPanel;
import processing.ProcessedImage;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


/**
 *
 * Okienko dialogowe z podglądem oryginalnego obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class PreviewDialog extends SimpleDialog implements IPhotoFrame {
   
   private final BufferedImage image; 
   protected PhotoPanel photoPanel;

   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej i wyświetlenie okienka
    * @param frame Referencja do GUI
    * @param title Nagłówek okna
    * @param image Obraz do podglądu
    */ 
   public PreviewDialog(GUI frame, String title, BufferedImage image) {
        
     super(frame, title);
     this.image = image;
     super.showDialog(600, 500); 
            
   } 
   
   
   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected final void getContent()  {   
      
     JPanel p = new JPanel();
     p.setOpaque(false);
     p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
                
     JPanel p0 = new JPanel(new GridLayout(1,1));
     p0.setOpaque(false);
     photoPanel = new PhotoPanel(new Color(0xe9ebf2));
     photoPanel.setBackground(Color.WHITE);
     photoPanel.setPreferredSize(new Dimension(580, 410));               
     photoPanel.setInitImage(new ProcessedImage(image));      
     p0.add(photoPanel);
     p.add(p0);
    
     BottomToolBar toolBar = new BottomToolBar(this, false);
     toolBar.addElement(getAdditionalComponent());
     p.add(toolBar);     
      
     add(p);
 
   }      
   
   /**
    * Dodatkowy komponent do paska przycisków
    * @return null
    */
   protected Component getAdditionalComponent() {
       
      return null; 
       
   }
   

   @Override
   public PhotoPanel getPhotoPanel() {
     return photoPanel;
   }

   
   @Override
   public GUI getMainFrame() {
     return frame;
   }


  @Override
  public void enableUndoMenuItem(boolean enabled) {}
      
  @Override
  public void enableRedoMenuItem(boolean enabled) {}

  @Override
  public void setMaskVisible(boolean visible) {}

  @Override
  public void setZoomSliderValue(int value) {}

  @Override
  public void enableImageMenuItems(boolean enabled) {}
  
  @Override
  public int getLocalBlurSize() {
     return -1;
  }

  @Override
  public int getLocalSmudgeSize() {
     return -1;
  }  
   
  @Override
  public int getMaxLocalSmudgeSize() {
     return -1;
  }    
  
  @Override
  public void setCloneThumb(ImageIcon thumb) {}
   
     
  
}
