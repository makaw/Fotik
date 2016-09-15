/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing.linearfilters;

import processing.ILinearFilter;
import java.awt.image.Kernel;

/**
 *
 * Filtr liniowy: odszumiający gaussowski σ=1
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class NoiseRemoveGaussian implements ILinearFilter {

   /** Maska filtru */ 
   private float[] mask = new float[] 
                           {1.0f,  4.0f,   7.0f, 4.0f, 1.0f,
                            4.0f, 16.0f, 26.0f, 16.0f, 4.0f,
                            7.0f, 26.0f, 41.0f, 26.0f, 7.0f,
                            4.0f, 16.0f, 26.0f, 16.0f, 4.0f, 
                            1.0f,  4.0f,  7.0f,  4.0f, 1.0f};
   
   /** Mnożnik współczynników */
   private static final float ro = 1/273.0f;
   
    
   @Override
   public Kernel getMask() {
     for(int i = 0; i < mask.length; i++) {
        mask[i] = mask[i] * ro;
     }
     return new Kernel(5, 5, mask);
   }
   
   
   @Override
   public Float getDefaultParam() {
      return null;  
   }
    
}
