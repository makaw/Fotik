/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing;

import java.awt.image.Kernel;

/**
 * 
 * Interfejs obiektu filtra liniowego (splotowego)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public interface ILinearFilter {    
    
  /**
   * Metoda zwraca maskę filtra
   * @return Maska filtra
   */  
  Kernel getMask();  
  
  /**
   * Metoda zwraca domyślną wartość parametru, null jeżeli brak parametru
   * @return Domyślna wartość parametru lub null
   */
  Float getDefaultParam();
  
    
}
