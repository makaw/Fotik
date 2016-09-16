/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing;

import java.awt.image.BufferedImage;


/**
 * Histogram obrazu (dane)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Histogram {
    
  /** Wybrany kanał */
  private HistogramChannel channel = HistogramChannel.ALL;
    
  /** Tablica ilości pikseli dla danego kanału */  
  private int[] hist;
  
  /** Badany obraz */
  private final BufferedImage image;
  
    
  /**
   * Konstruktor
   * @param image Badany obraz 
   */
  public Histogram(BufferedImage image) {              
      
    this.image = image;
    calcChannel();
    
  }
  
  /**
   * Wyliczenie wartości histogramu dla wybranego kanału
   */
  private void calcChannel() {
        
    hist = new int[256];  
    
    for(int i=0;i<image.getWidth();i++) 
      for(int j=0;j<image.getHeight();j++)  
       hist[channel.getValue(image.getRGB(i, j))]++;

  }
  
  
    /**
   * Przetworzenie wybranego kanału obrazu przy pomocy tablicy "Look Up"
   * @param image Przetwarzany obraz
   * @param lut Tablica LUT 256-elem.
   */
  public void applyLUT(BufferedImage image, float[] lut) {
      
    applyLUT(image, lut, channel);     
      
  }
  
  
  /**
   * Przetworzenie wybranego kanału obrazu przy pomocy tablicy "Look Up", 
   * statyczna wersja metody
   * @param image Przetwarzany obraz
   * @param lut Tablica LUT 256-elem.
   * @param channel Kanał obrazu
   */
  public static void applyLUT(BufferedImage image, float[] lut, HistogramChannel channel) {
      
    for (int j = 0; j < image.getHeight(); j++) 
      for (int i = 0; i < image.getWidth(); i++) 
        image.setRGB(i, j, channel.getLUTValue(image.getRGB(i,j), lut));                  
      
  }
      
  
  /**
   * Wyrównanie histogramu
   * @param image Kopia obrazu do wykonania operacji
   */
  public void equalize(BufferedImage image) {       
    
    // dystrybuanta
    float[] dist = new float[256];
    float total = image.getWidth()*image.getHeight();
    
    for (int i=0;i<256;i++) {
      for (int j=0;j<=i;j++) dist[i] += hist[j];
      dist[i]/=total;
    }
    
    // pierwsza niezerowa wartość dystrybuanty
    float d0 = 0;
    for (int i=0;i<256;i++) if (dist[i]!=0) { d0 = dist[i]; break; }
    
    // tablica "look up"
    float[] lut = new float[256];
    for (int i=0;i<256;i++) { 
       lut[i] = (dist[i]-d0 / (1.0f-d0));
       if (lut[i]<0) lut[i] = 0.0f;
       if (lut[i]>1.0f) lut[i] = 1.0f;
    }            
    
    applyLUT(image, lut);
    
  }
  
      
  /**
   * Rozciągnięcie histogramu
   * @param image Kopia obrazu do wykonania operacji
   * @param threshold Próg (minimalna ilość punktów na poziomie)
   */  
  public void stretch(BufferedImage image, int threshold) {
        
    // maksymalna i minimalna wartość
    int hMin = 0, hMax = 255;
    try {
      while (hist[hMin]<threshold) hMin++;
      while (hist[hMax]<threshold) hMax--;
    }
    catch (ArrayIndexOutOfBoundsException e) { return; }   
    
    // normalizacja
    float normHMin = hMin/255.0f,  normHMax = hMax/255.0f;
    
    // tablica "look up"
    float[] lut = new float[256];
    normHMax-=normHMin;
    for (int i=hMin; i<hMax;i++) {
      lut[i] = (((float)i/(float)hMax)-normHMin)/normHMax;
      if (lut[i]<0) lut[i] = 0.0f;
      if (lut[i]>1.0f) lut[i] = 1.0f;
    }
    
    applyLUT(image, lut);
    
  }
 
  
  /**
   * Metoda zwraca wartość dla wskazanej jasności
   * @param index jasność
   * @return Wartość dla wskazanej jasności
   */
  public int getValue(int index) {
      
    try {  
      return hist[index];  
    }
    catch (ArrayIndexOutOfBoundsException e) {
      return -1; 
    }
      
  } 
  

  
  /**
   * Metoda zwraca maksymalną wartość 
   * @return Maksymalna wartość histogramu
   */
  public int getMaxValue() {
      
    int maxValue = 0;  
    for (int i=0;i<255;i++)  maxValue = Math.max(maxValue, hist[i]); 
    return maxValue;
    
  }    

  
  /**
   * Zmiana kanału histogramu
   * @param channel Kanał histogramu
   */
  public void setChannel(HistogramChannel channel) {
      
    this.channel = channel;
    calcChannel();
    
  }
  
  
  
  
  
}
