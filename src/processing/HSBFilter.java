/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */

package processing;


import gui.loader.IProgress;
import java.awt.Color;
import java.awt.image.BufferedImage;


/**
 * Odszumiający filtr medianowy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class HSBFilter {
    
    /** Współczynniki przesunięcia kanałów H S B */
    private final float hOffset, sOffset, bOffset;
    /** Przetwarzany obraz */
    private final BufferedImage image;
    /** Interfejs paska postępu */
    private final IProgress progress;
    
    /**
     * Konstruktor
     * @param image Przetwarzany obraz
     * @param hOffset Przesunięcie odcienia
     * @param sOffset Przesunięcie nasycenia
     * @param bOffset Przesunięcie jasności
     * @param progress Interfejs paska postępu
     */
    public HSBFilter(BufferedImage image, float hOffset, float sOffset, float bOffset, IProgress progress) {
        
       this.hOffset = hOffset;
       this.sOffset = sOffset;
       this.bOffset = bOffset;
       this.image = image;
       this.progress = progress;
        
    }
    
    public HSBFilter(BufferedImage image, float hOffset, float sOffset, float bOffset) {
        
       this(image, hOffset, sOffset, bOffset, null);
        
    }

    
   
    /**
     * Przefiltrowanie obrazu
     * @return Przefiltrowany obraz
     */
    public BufferedImage filter() {
        
        BufferedImage dstImage = 
                new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        int total = image.getWidth() * image.getHeight();
        
        for (int k = 0; k < image.getHeight(); k++) {
          for (int j = 0; j < image.getWidth(); j++) {
                
            Color color = new Color(image.getRGB(j, k));
            float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                                    
            hsb[0] += hOffset/255.0f; 
            if (hsb[0]>1.0f) hsb[0] = 1.0f; 
            if (hsb[0]<0) hsb[0] = 0f;
            
            hsb[1] += sOffset/255.0f; 
            if (hsb[1]>1.0f) hsb[1] = 1.0f; 
            if (hsb[1]<0) hsb[1] = 0f;
            
            hsb[2] += bOffset/255.0f; 
            if (hsb[2]>1.0f) hsb[2] = 1.0f;
            if (hsb[2]<0) hsb[2] = 0f;
            
            // ustalenie nowego koloru piksela
            dstImage.setRGB(j, k, Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));            
                            
          }
          
          // zmiana wartości paska postępu
          if (progress!=null && 
            !progress.setProgress((int)(((float)(k*image.getWidth())/(float)total)*100))) return null;
          
        }
        
        return dstImage;
        
    }
    
       
    
}
