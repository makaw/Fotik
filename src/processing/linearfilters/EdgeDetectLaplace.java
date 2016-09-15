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
 * Filtr liniowy: wykrywanie krawędzi - laplasjan
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class EdgeDetectLaplace implements ILinearFilter {


   /** Domyślny parametr */ 
   private static final float defaultParam = 2.0f; 
    
   /** Maska filtru */ 
   private float[] mask;
   /** Mnożnik współczynników */
   private final float ro;   
   
   
   public EdgeDetectLaplace(Object param) {
       
      float p;
      try {
        p = (float)param;  
      }
      catch (ClassCastException | NullPointerException e) {
        p = defaultParam;
      }
      
      ro = 1/12.0f;
      mask = new float[] { -p,    -2*p,   -p,
                           -2*p,  12*p,   -2*p,
                           -p,    -2*p,   -p};
       
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
