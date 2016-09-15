/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing;

import addon.CIELab;
import gui.loader.IProgress;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * 
 * Automatyczny balans bieli
 * 
 * Na podstawie: 
 * http://pippin.gimp.org/image_processing/chapter-automaticadjustments.html#section-automaticwhitebalance
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class WhiteBalanceFilter {
  
    
  /** Przetwarzany obraz */
  private final BufferedImage image;
  /** Interfejs okna z paskiem postępu */
  private final IProgress progress;
    
  
  /**
   * Konstruktor
   * @param image Przetwarzany obraz
   * @param progress Interfejs okna z paskiem postępu
   */
  public WhiteBalanceFilter(BufferedImage image, IProgress progress) {
       
    this.image = image;   
    this.progress = progress;
    
  }
  
  /**
   * Przefiltrowanie obrazu
   * @return Przefiltrowany obraz
   */  
  public BufferedImage filter() {
            
    float avgSumA = 0f, avgSumB = 0f;
    int total = image.getWidth() * image.getHeight();
    int n = 0;
    
    BufferedImage dstImage = 
                new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
       
    for (int i=0;i<image.getWidth();i++) {
      for (int j=0;j<image.getHeight();j++) {
             
        Color cl = new Color(image.getRGB(i, j));
        float[] rgb = { cl.getRed()/255.0f, cl.getGreen()/255.0f, cl.getBlue()/255.0f };
        float[] lab = CIELab.getInstance().fromRGB(rgb);
        avgSumA+=lab[1]; avgSumB+=lab[2];  
        n++;
                  
      }
      
      if (progress != null && 
          !progress.setProgress((int)(((float)(i*image.getHeight())/((float)total*2.0f))*100))) return null;
      
    }
       
    avgSumA/=(float)n;
    avgSumB/=(float)n;
           
        
    for (int j=0;j<image.getHeight();j++) {
      for (int i=0;i<image.getWidth();i++) {
              
          Color cl = new Color(image.getRGB(i, j));
          float[] rgb = { cl.getRed()/255.0f, cl.getGreen()/255.0f, cl.getBlue()/255.0f };
          float[] lab = CIELab.getInstance().fromRGB(rgb);             
                         
          lab[1] += (-avgSumA) * (lab[0]/100.0f) * 1.1f;
          lab[2] += (-avgSumB) * (lab[0]/100.0f) * 1.1f;
                         
          rgb = CIELab.getInstance().toRGB(lab);
          try {
            dstImage.setRGB(i, j, (new Color(rgb[0], rgb[1], rgb[2])).getRGB());              
          }
          catch (Exception e) {
            //System.out.println(rgb[0] + " / " + rgb[1] + " / " + rgb[2]);
            //return;
          }
          
       } 
    
       if (progress != null && 
           !progress.setProgress(50+(int)(((float)(j*image.getWidth())/((float)total*2.0f))*100))) return null;      
      
    }
    
    return dstImage;
        
  }

  
  
  
}
