/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing.imagefft;

import gui.loader.IProgress;
import java.awt.Color;
import java.awt.image.BufferedImage;


/**
 * Szybka transformata Fouriera 2D obrazu (w skali szarości)
 * (wykorzystuje przekształcenie FFT - klasa z pakietu addon)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GrayImageFFT extends ImageFFT {

    
  /**
   * Konstruktor
   * @param image Przetwarzany obraz
   * @param progress Interfejs okna z paskiem postępu
   */
  public GrayImageFFT(BufferedImage image, IProgress progress) {
      
     super(image, progress, false);  
      
  }    
  
  
  public GrayImageFFT(ImageFFT f) {
     
     super(f);  
      
  }  
  
  
  /**
   * Wyodrębnienie kanałów (wszystkie mają tą samą wartość)
   * @param rgbValue Wartość RGB
   * @return Przekonwertowana wartość
   */
  @Override
  protected float[] convertFromRGB(int rgbValue) {
              
    return new float[] {new Color(rgbValue).getRed()};
    
  }
  
  /**
   * Połączenie kanałów (przetwarzany był tylko jeden, wszystkie mają tą samą wartość)
   * @param val Składowe koloru (skala log.)
   * @return Wartość RGB
   */  
  @Override
  protected int convertToRGB(float[] val) {
    
    try {
    return (new Color((int)val[0], (int)val[0], (int)val[0])).getRGB();
    }
    catch (Exception e) {
      return Color.WHITE.getRGB();
        
    }
    
  }  
  
  
  /**
   * Zamiana amplitudy na skalę logarytmiczną odpowiednią dla zakresu RGB
   * @param amp Amplituda
   * @param maxAmp Maksymalna amplituda
   * @param channel Kanał
   * @return Amplituda i skali logarytmicznej
   */    
  @Override
  protected double ampToLogScaleColor(double amp, double maxAmp, int channel) {
      
     double c = 255.0/Math.log(1+maxAmp);
     double val = c* Math.log(1+amp);
     return val;
      
  }
    
}
