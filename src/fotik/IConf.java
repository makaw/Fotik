/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package fotik;

/**
 *
 * Interfejs - stałe globalne
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public interface IConf {
    
  /** Nazwa aplikacji */
  static final String APP_NAME = "Fotik";
  /** Wersja aplikacji */
  static final String APP_VERSION = "[09/2016]";
   
  /** Maksymalna ilość cofnięć - maks. rozmiar historii zmian */  
  static final int MAX_UNDOS = 10;   
  
  /** Maksymalny promień lokalnego rozmycia */
  static final int MAX_BLUR_RADIUS = 30;
  
  /** Maksymalna wielkość smugi - narzędzie smużenia */
  static final int MAX_SMUDGE_SIZE = 20;
  
  /** Maksymalna długość boku klonowanego fragmentu obrazu */
  static final int MAX_CLONE_THUMB_SIZE = 50;     
    
}