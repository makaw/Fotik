/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01-02/2016
 */
package gui.photoview;

import gui.GUI;
import javax.swing.ImageIcon;


/**
 * Interfejs okien zawierających przewijany panel z obrazem
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public interface IPhotoFrame {
    
  /**
   * Metoda zwraca ref. do panelu z obrazem
   * @return Ref. do panelu z obrazem
   */  
  PhotoPanel getPhotoPanel();  
  
  /**
   * Metoda zwraca ref. do głównego okna aplikacji
   * @return Ref. do głównego okna aplikacji
   */
  GUI getMainFrame();
  
  /**
   * Ustawienie dostępności opcji menu "Ponów"
   * @param enabled True jeżeli dostępne
   */
  void enableRedoMenuItem(boolean enabled);
  
  /**
   * Ustawienie dostępności opcji menu "Cofnij"
   * @param enabled True jeżeli dostępne
   */  
  void enableUndoMenuItem(boolean enabled);
  
  /**
   * Ustawienie widoczności warstwy maski
   * @param visible True jeżeli widoczna
   */  
  void setMaskVisible(boolean visible);
  
  /**
   * Ustawienie wartości suwaka powiększenia widoku
   * @param value Wartość powiększenia widoku (w %)
   */
  void setZoomSliderValue(int value);
  
  /**
   * Ustawienie dostępności elementów menu dot. obrazu
   * @param enabled True jeżeli dostępne
   */
  void enableImageMenuItems(boolean enabled);
  
  /**
   * Rozmiar obszaru lokalnego odszumiania (o ile dostępne)
   * @return Rozmiar obszaru lokalnego odszumiania
   */
  int getLocalBlurSize();
  
  /**
   * Rozmiar obszaru lokalnego smużenia (o ile dostępne)
   * @return Rozmiar obszaru lokalnego smużenia
   */
  int getLocalSmudgeSize();
  
  /**
   * Maksymalny rozmiar obszaru lokalnego smużenia (o ile dostępne)
   * @return Rozmiar obszaru lokalnego smużenia
   */
  int getMaxLocalSmudgeSize();  
  
  /**
   * Zmiana miniatury fragmentu obrazu do klonowania
   * @param thumb Miniatura fragmentu obrazu
   */
  void setCloneThumb(ImageIcon thumb);
  
}
