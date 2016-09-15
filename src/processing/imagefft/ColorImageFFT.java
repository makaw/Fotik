/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing.imagefft;

import addon.CIELab;
import gui.loader.IProgress;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Szybka transformata Fouriera 2D obrazu (kolorowego)
 * (wykorzystuje przekształcenie FFT - klasa z pakietu addon)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class ColorImageFFT extends ImageFFT {

    
  /**
   * Konstruktor
   * @param image Przetwarzany obraz
   * @param progress Interfejs okna z paskiem postępu
   */
  public ColorImageFFT(BufferedImage image, IProgress progress) {
      
     super(image, progress, true);  
      
  }    
  
  public ColorImageFFT(ImageFFT f) {
     
     super(f);  
      
  }
  
  /**
   * Wyodrębnienie kanałów i konwersja z RGB do Lab 
   * @param rgbValue Wartość RGB
   * @return Przekonwertowana wartość
   */
  @Override
  protected float[] convertFromRGB(int rgbValue) {
          
    Color cl = new Color(rgbValue);  
    float[] rgb = new float[]{cl.getRed()/255.0f, cl.getGreen()/255.0f, cl.getBlue()/255.0f};
    return CIELab.getInstance().fromRGB(rgb);   
    
  }
  
  /**
   * Połączenie kanałów i konwersja z Lab do RGB
   * @param val Składowe koloru (skala log.)
   * @return Wartość RGB
   */
  @Override
  protected int convertToRGB(float[] val) {
          
    float[] rgb = CIELab.getInstance().toRGB(val);
    return (new Color(rgb[0], rgb[1], rgb[2])).getRGB();
    
  }  
  
  /**
   * Zamiana amplitudy na skalę logarytmiczną odpowiednią dla zakresu Lab
   * @param amp Amplituda
   * @param maxAmp Maksymalna amplituda
   * @param channel Kanał
   * @return Amplituda i skali logarytmicznej
   */  
  @Override
  protected double ampToLogScaleColor(double amp, double maxAmp, int channel) {
      
    double c;  
    if (channel==0) c = 100.0/Math.log(1.0+maxAmp);
    else c = 255.0/Math.log(1.0+maxAmp);
    double val = c* Math.log(1.0+amp);
    // w Lab kanały(składowe) odp. za chrominancję mają zakres -127 do 128
    if (channel>0) val = 127.0 - val;  
    return val; 
      
  }
    
}
