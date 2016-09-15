/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing.imagefft;

import addon.Complex;
import addon.FFT;
import gui.loader.IProgress;
import java.awt.image.BufferedImage;


/**
 * Szybka transformata Fouriera 2D obrazu
 * (wykorzystuje przekształcenie FFT - klasa z pakietu addon)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public abstract class ImageFFT {
        
    
  /** Przetwarzany obraz */
  private final BufferedImage image;
  /** Obraz widma */
  private BufferedImage spectrumImage;
  /** Obraz po odwrotnej transformacji */
  private BufferedImage inverseImage;
  /** Interfejs okna z paskiem postępu */
  private IProgress progress;
  /** Transformata (osobne kanały) */
  private Complex[][][] fft;
  /** Transformata odwrotna (osobne kanały) */
  private Complex[][][] ifft;  
  /** Punkt odniesienia dla wskaźnika postępu */
  private final float total;
  /** Waga wstępnych operacji dla wskaźnika postępu */
  private final float initW;
  /** Ilość kanałów */
  private final int N;
  /** Wielkości tablic dla transformaty */
  private final int width2, height2;
  /** Dostępne operacje na widmie obrazu */
  private ImageFFTOp fftOp;
  
  /**
   * Konstruktor dla klas dziedziczących
   * @param image Przetwarzany obraz
   * @param progress Interfejs okna z paskiem postępu
   * @param inColor True jeżeli obraz kolorowy (3 kanały)
   */
  public ImageFFT(BufferedImage image, IProgress progress, boolean inColor) {
      
    this.image = image;  
    this.progress = progress;
    total = image.getHeight() + image.getWidth();
    initW = total/(inColor ? 10.0f : 30.0f);
    N = inColor ? 3 : 1;
    width2 = (int)Math.pow(2, nextTwoPower(image.getWidth()));
    height2 = (int)Math.pow(2, nextTwoPower(image.getHeight()));
    spectrumImage = new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);        
    
  }
  
  /**
   * Konstruktor kopiujący
   * @param f Kopiowany obiekt
   */
  public ImageFFT(ImageFFT f) {
      
    this(f.image, null, f.N == 3);
    this.fft = f.fft.clone();
      
  }
  
  
  /**
   * Wyodrębnienie kanałów i konwersja (i razie potrzeby) z RGB 
   * @param rgbValue Wartość RGB
   * @return Przekonwertowana wartość
   */
  protected abstract float[] convertFromRGB(int rgbValue);
  
  /**
   * Połączenie kanałów i konwersja (i razie potrzeby) na RGB
   * @param val Składowe koloru (skala log.)
   * @return Wartość RGB
   */
  protected abstract int convertToRGB(float[] val);
  
  /**
   * Zamiana amplitudy na skalę logarytmiczną dla odpowiedniej przestrzeni kolorów
   * @param amp Amplituda
   * @param maxAmp Maksymalna amplituda
   * @param channel Kanał
   * @return Amplituda i skali logarytmicznej
   */
  protected abstract double ampToLogScaleColor(double amp, double maxAmp, int channel);
  
  
  /**
   * Przetworzenie obrazu, wyliczenie transformaty i przygotowanie widoku widma
   * @return False jeżeli anulowano
   */
  public boolean processImage() {    
      
    Complex[][][] values = new Complex[N][height2][width2];            
   
    for (int c=0;c<N;c++) {
      for (int j=0; j<height2;j++)               
        for (int i=0; i<width2;i++) {
          
          if (j<image.getHeight() && i<image.getWidth()) {             
            float[] lab = convertFromRGB(image.getRGB(i, j));
            values[c][j][i] = new Complex(Math.pow(-1, j+i) * lab[c], 0);            
          }
          else values[c][j][i] = new Complex(0, 0);
          
        }      
     
      if (progress!=null && 
           !progress.setProgress((int)(((c+1)*initW)/(total*N+(N*30)) * 100.0f))) return false;
      
    }
    
    fft = new Complex[N][][];
    
    for (int i=0;i<N;i++)
      if (!computeFFT2D(values[i], i)) return false;
    
    prepareView();
    
    fftOp = new ImageFFTOp(fft, width2, height2, N);
    
    return true;
    
  }
  
  /**
   * Odwrócenie transformacji
   * @return False jeżeli anulowano
   */
  public boolean inverseProcess() {    
  
    inverseImage = new BufferedImage(image.getWidth(), image.getHeight(), 
            (N==1) ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_INT_RGB);
      
    ifft = new Complex[N][][];
    
    for (int i=0;i<N;i++)
      if (!computeIFFT2D(fft[i].clone(), i)) return false;

    for (int j=0; j<image.getHeight();j++)               
      for (int i=0; i<image.getWidth();i++) {
      
         float[] lab = new float[N];
         for (int c=0;c<N;c++) lab[c] = (float)(Math.pow(-1, j+i) * ifft[c][j][i].re());
         inverseImage.setRGB(i, j, convertToRGB(lab)); 
              
    }          
    
    return true;
  
  }
  
  

  /**
   * Wyliczenie transformaty
   * @param input Tablica wejściowa (wartości zespolone)
   * @param channel Nr kanału LAB
   * @return False jeżeli przerwano
   */
  private boolean computeFFT2D(Complex[][] input, int channel) {
                       
     // przekształcenie kolumnami
     for (int i=0; i<height2;i++) {
          
       input[i] = FFT.fft(input[i]);
       if (progress!=null && i<image.getHeight() && 
           !progress.setProgress((int)((N*initW + channel*total + i)/(total*N+N*initW) * 100.0f))) return false;
          
      }
           
      // teraz kolumnami     
      fft[channel] = rotateArray(input);
      
      for (int i=0; i<width2;i++) {
          
        fft[channel][i] = FFT.fft(fft[channel][i]);
        if (progress!=null && i<image.getWidth() && 
           !progress.setProgress((int)((N*initW + channel*total + i+image.getHeight())/(total*N+N*initW) * 100.0f)))
              return false;          
      }      

      return true;
      
  }
   
  
  /**
   * Wyliczenie transformaty odwrotnej
   * @param input Tablica wejściowa (wartości zespolone)
   * @param channel Nr kanału LAB
   * @return False jeżeli przerwano
   */
  private boolean computeIFFT2D(Complex[][] input, int channel) {
                       
     float total2 = width2 + height2; 
      
     // przekształcenie kolumnami
     for (int i=0; i<width2;i++) {
          
       input[i] = FFT.ifft(input[i]);
       if (progress!=null && 
           !progress.setProgress((int)((channel*total2 + i)/(total2*N) * 100.0f))) return false;
          
      }
           
      // teraz wierszami    
      ifft[channel] = rotateArray(input);
      
      for (int i=0; i<height2;i++) {
          
        ifft[channel][i] = FFT.ifft(ifft[channel][i]);
        if (progress!=null &&  
           !progress.setProgress((int)((channel*total2+i+width2)/(total2*N) * 100.0f)))
              return false;          
      }      
      
        
      return true;
      
  }  

  
  
  /**
   * Przygotowanie wizualizacji widma
   */
  private void prepareView() {
      
    double[][][] mAmp = new double[N][width2][height2];
        
    // wyliczenie modułów i przejście do skali logarytmicznej
    for (int ch=0;ch<N;ch++) {  
      for (int i=0;i<width2;i++) 
        for (int j=0;j<height2;j++) 
          mAmp[ch][i][j] = fft[ch][i][j].abs();                 
      
      double maxAmp = 0;
      for (int i=0;i<width2;i++) 
        for (int j=0;j<height2;j++) maxAmp = Math.max(maxAmp, mAmp[ch][i][j]);      

      for (int i=0;i<width2;i++) 
        for (int j=0;j<height2;j++) {
          
          mAmp[ch][i][j] = ampToLogScaleColor(mAmp[ch][i][j], maxAmp, ch);
          
        }              
      
    }
    
    // rysowanie widma
    for (int i=0;i<width2;i++) 
      for (int j=0;j<height2;j++) {
            
         float[] val = new float[N];
         for (int ch=0;ch<N;ch++) val[ch] = (float)mAmp[ch][i][j];         
         spectrumImage.setRGB(i, j, convertToRGB(val));
         
      }    
    
      
  }    
  
    
      
  /**
   * Transpozycja macierzy o wartościach zespolonych (tablicy 2D)
   * @param m Tablica wejściowa
   * @return Transponowana tablica
   */      
  private static Complex[][] rotateArray(Complex[][] m) {
      
    Complex[][] rot = new Complex[m[0].length][m.length];
    for (int i=0;i<m.length;i++)
      for (int j=0; j<m[0].length; j++)
         rot[j][i] = m[i][j];
    return rot;  
 
  }
  
  /**
   * Pierwsza większa od wejściowej liczby potęga dwójki
   * @param a Liczba wejściowa
   * @return Pierwsza następna potęga dwójki
   */
  private static int nextTwoPower(int a) {
      
    return a == 0 ? 0 : 32 - Integer.numberOfLeadingZeros(a - 1);  
      
  }
 
  
  
  public BufferedImage getSpectrumImage() {
     return spectrumImage;
  }
  
  public BufferedImage getInverseImage() {
      return inverseImage;
  }

  public void setProgress(IProgress progress) {
      this.progress = progress;
  }

  public ImageFFTOp getFftOp() {
      return fftOp;
  }

  
  
    
}
