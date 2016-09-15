/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.loader;

/**
 *
 * Interfejs operacji wykonywanych z paskiem postępu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public interface IProgress {
    
  /**
   * Ustawienie wartosci procentowej dla paska postepu
   * @param value Wartosc procentowa paska postepu
   * @return False jeżeli przerwano wątek
   */
   boolean setProgress(int value);  
   
   
   
  /**
   * Zamknięcie okienka
   */
   void hideComponent();
    
}
