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
 * Filtr liniowy: wyostrzający 3x3
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Sharp3x3 implements ILinearFilter {

   /** Maska filtru */ 
   private static final float[] mask = new float[] 
                           { 0.f, -1.f, 0.f,
                            -1.f, 5.0f, -1.f,
                            0.f, -1.f, 0.f};
   
   
    
   @Override
   public Kernel getMask() {
   
     return new Kernel(3,3, mask);
       
   }
    
   
   @Override
   public Float getDefaultParam() {
      return null;  
   }
   
   
}
