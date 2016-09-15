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
 * Filtr liniowy: odszumiający parametryczny ogólny
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class NoiseRemoveParametrised implements ILinearFilter {

   /** Domyślny parametr */ 
   private static final float defaultParam = 2.0f; 
    
   /** Maska filtru */ 
   private float[] mask;
   /** Mnożnik współczynników */
   private final float ro;   
   
   
   public NoiseRemoveParametrised(Object param) {
       
      float p;
      try {
        p = (float)param;  
      }
      catch (ClassCastException | NullPointerException e) {
        p = defaultParam;
      }
      
      ro = (float)Math.pow(1/(p+2.0f), 2);
      mask = new float[] { 1.0f,  p,    1.0f,
                           p,     p*p,  p,
                           1.0f,  p,    1.0f};
       
   }
   
    
   @Override
   public Kernel getMask() {
     for(int i = 0; i < mask.length; i++) {
        mask[i] = mask[i] * ro;
     }
     return new Kernel(3,3, mask);
   }
   
   
   @Override
   public Float getDefaultParam() {
      return defaultParam;  
   }
   
   
}
