/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.loader;


/**
 *
 * Interfejs klas wywołujących operacje wykonywane z paskiem postępu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public interface IProgressInvoker {
    
  /** Rozpoczęcie wykonywania operacji
   * @param progress Interfejs paska postępu
   */  
  void start(IProgress progress);  
    
}
