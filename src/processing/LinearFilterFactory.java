/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing;

import processing.linearfilters.*;


/**
 *
 * Fabryka filtrów liniowych (splotowych)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class LinearFilterFactory {
    
   /**
    * Statyczna metoda zwracająca obiekt konkretnej klasy (filtr liniowy)
    * @param type Typ filtru
    * @param param Parametry filtru
    * @return Interfejs wskazanego filtru
    */
   public static ILinearFilter getFilter(LinearFilterType type, Object param) {
         
     switch (type) {
         
         default:
                                 
         case NR_EVEN: return new NoiseRemoveEven();
         case NR_EVEN_9x9: return new NoiseRemoveEven9x9();
         case NR_UNEVEN: return new NoiseRemoveUneven();
         case NR_PARAMETRISED: return new NoiseRemoveParametrised(param);
         case NR_GAUSSIAN: return new NoiseRemoveGaussian();
         case SHARP_3x3: return new Sharp3x3();  
         case SHARP_HP: return new SharpHighPass(param);
         case EDGE_DETECT: return new EdgeDetectLaplace(param);

     }    
     
   }
   
   
   public static ILinearFilter getFilter(LinearFilterType type) {
       
     return getFilter(type, null);  
       
   }
   
   
    
}
