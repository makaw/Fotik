/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import javax.swing.JPanel;


/**
 *
 * Panel z obrazkowym tłem
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class BgPanel extends JPanel {
    
  /** Nazwa pliku z obrazkiem tła */  
  private final String imgName; 

  /**
   * Konstruktor
   * @param imgName Nazwa pliku
   * @param layoutManager Manager rozmieszczenia komponentów
   */
  public BgPanel(String imgName, LayoutManager layoutManager) {
    
    super(layoutManager);  
    this.imgName = imgName;
      
  }
  
  
  /**
   * Konstruktor
   * @param imgName Nazwa pliku  
   */
  public BgPanel(String imgName) {
      
     this(imgName, new FlowLayout());  
      
  }
  

  
  @Override
  public void paintComponent(Graphics g)  {
    
    Graphics2D g2D = (Graphics2D)g;   
    // rysowanie tla
    Image bg = ImageRes.getImage(imgName);
    if (bg != null)
      g2D.drawImage(ImageRes.getImage(imgName), 0, 0, this);
   
  }
     
    
    
}
