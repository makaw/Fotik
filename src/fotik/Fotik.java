/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package fotik;

import gui.GUI;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

/**
 *
 * Klasa główna zawierająca metodę main() - inicjalizacja, wywołanie interfejsu graficznego
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public final class Fotik {
  
    
  /**
   * Konstruktor klasy głównej, inicjalizacja obiektów, w tym bezpieczne wywołanie 
   * interfejsu graficznego
   * @throws InterruptedException Błąd przy wywołaniu GUI
   * @throws InvocationTargetException Błąd przy wywołaniu GUI
   */
  private Fotik() throws InterruptedException, InvocationTargetException {
      
    // bezpieczne wywołanie interfejsu graficznego  
    SwingUtilities.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        new GUI();
      }
    });    
        
  }   
  
  
  /**
   * Zakończenie działania programu
   */
  public static void quitApp() {

     System.exit(0);
      
  }    
    
    
  /** 
   * Metoda main wołana przez system w trakcie uruchamiania aplikacji:
   * Ustawia temat LookAndFeel GUI, wywołuje konstruktor, który
   * uruchamia interfejs graficzny..
   * @param args Argumenty przekazane do aplikacji.
   */
  @SuppressWarnings("CallToPrintStackTrace")
  public static void main(final String[] args) {
      
     GUI.setLookAndFeel();
     GUI.setLocalePL();

     try {
         
        new Fotik();
        
     } catch (InterruptedException | InvocationTargetException e) {
         
        System.err.println("Problem podczas wywo\u0142ania interfejsu graficznego: "+e);
        e.printStackTrace();
        
     }
        
  }
    
}
