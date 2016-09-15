/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01-02/2016
 */
package processing.imagefft;

import addon.Complex;
import fotik.History;
import gui.loader.IProgress;
import gui.photoview.Mask;
import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import processing.ProcessedImage;


/**
 * Operacje na widmie obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class ImageFFTOp {
   
  /** Domyślny promień filtra dolnoprzepustowego */
  public static final int defaultLowPassRadius = 50;  
    
  /** Historia zmian danych FFT */
  private final History<HashMap<History.PointIndex, Complex>> fftHistory;
  /** Dane FFT do opcji "Ponów" */
  private final HashMap<History.PointIndex, Complex> fftRedo;
  
  /** Transformata (dane) */
  private final Complex[][][] fft;  
  /** Parametry transformaty (wielkość tablicy, ilość kanałów) */
  private final int width2, height2, N;  
  /** Łączna ilość punktów */
  private final int total;
  
  /**
   * Konstruktor
   * @param fft Dane transformaty (rozdzielone kanały)
   * @param width2 Ilość kolumn
   * @param height2 Ilość wierszy
   * @param N Ilość kanałów
   */
  public ImageFFTOp(Complex[][][] fft, int width2, int height2, int N) {
      
    this.fft = fft;
    this.width2 = width2;
    this.height2 = height2;
    this.N = N;
    
    total = width2*height2;
    
    fftHistory = new History<>();
    fftRedo = new HashMap<>();
      
  }
  

  /**
   * Usuwanie regularnych wzorów, czyli usuwanie niepożądanych częstotliwości z widma obrazu
   * @param image Wejściowy obraz widma
   * @param radius Promień pomijanego otoczenia centralnego elementu
   * @param axisPad Odległość od osi centralnego elementu
   * @param threshold Próg jasności
   * @param contrastGained True jeżeli wzmocnić kontrast obrazu widma
   * @param progress Interfejs okna z paskiem postępu
   * @return Obraz widma po usunięciu niepożądanych częstotliwości
   */
  public BufferedImage removePatterns(BufferedImage image, int radius, int axisPad,
          int threshold, boolean contrastGained, IProgress progress) {
          
    BufferedImage spectImgGained = contrastGained ? new ProcessedImage(image).getBCAdjustedCopy(10.0f, 1.3f) : 
              new ProcessedImage(image).getCopy();
    BufferedImage spectImg = new ProcessedImage(image).getCopy();
    
    float xcenter = (float)Math.floor(width2/2.0);
    float ycenter = (float)Math.floor(height2/2.0);
    
    int cnt = 0;
          
    HashMap<History.PointIndex, Complex> changes = new HashMap<>();
    
    for (int i=0;i<width2;i++) {
      for (int j=0;j<height2;j++) {
        if (Math.sqrt(Math.pow((ycenter-j), 2)+Math.pow((xcenter-i), 2))>radius 
            && (i>xcenter+axisPad || i<xcenter-axisPad) && (j>ycenter+axisPad || j<ycenter-axisPad)) {
          
           Color rgb = new Color(spectImgGained.getRGB(i, j));
           int bright = (int)(Color.RGBtoHSB(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), null)[2]*255.0f);
           //if (rgb.getRed() > threshold && rgb.getGreen() > threshold && rgb.getBlue() > threshold) {
           if (bright > threshold) {
               
             for (int c=0; c<N; c++) {
               changes.put(new History.PointIndex(i, j, c), fft[c][i][j]);
               fft[c][i][j] = new Complex(0,0);       
             }
            
             spectImg.setRGB(i, j, 0);
             cnt++;
               
           } 
        }
      }
    
      // zmiana wartości paska postępu
      if (!progress.setProgress((int)(((float)(i*image.getHeight())/(float)total)*100))) return null;
      
    }
    
    if (cnt == 0) return null;
              
    fftHistory.push(changes);
    
    return spectImg;
        
  }    
  
  /**
   * Usuwanie z widma obrazu częstotliwości oznaczonych na masce
   * @param image Wejściowy obraz widma
   * @param mask Maska
   * @return Obraz widma po usunięciu niepożądanych częstotliwości
   */  
  public BufferedImage removeMasked(BufferedImage image, Mask mask) {      
      
    BufferedImage img = new ProcessedImage(image).getCopy();  
    HashMap<History.PointIndex, Complex> changes = new HashMap<>();
    
    for (Shape shape: mask) {
        
       RectangularShape r = (RectangularShape)shape;
       for (int i = (int)r.getMinX(); i < (int)r.getMaxX(); i++) 
         for (int j = (int)r.getMinY(); j < (int)r.getMaxY(); j++) {
             
            if (shape.contains(new Point(i, j))) {
                
               for (int c=0; c<N; c++) {
                 changes.put(new History.PointIndex(i, j, c), fft[c][i][j]);
                 fft[c][i][j] = new Complex(0,0);       
               }
            
               img.setRGB(i, j, 0); 
                
            }
            
             
       }

    }
    
    fftHistory.push(changes);
    
    return img;
      
  }
  
  

  /**
   * Usuwanie z widma obrazu częstotliwości oznaczonych na masce
   * @param image Wejściowy obraz widma
   * @param radius Promień filtra
   * @param progress Interfejs okna z paskiem postępu
   * @return Obraz widma nałożeniu filtra
   */  
  public BufferedImage lowPassFilter(BufferedImage image, int radius, IProgress progress) {
      
   BufferedImage img = new ProcessedImage(image).getCopy();     
   HashMap<History.PointIndex, Complex> changes = new HashMap<>();
    
   int xcenter = (int)Math.floor(width2/2.0);
   int ycenter = (int)Math.floor(height2/2.0);
   double y = height2/2.0 - radius;
   double x = width2/2.0 - radius;   
   int totalN = total * N;

    for (int c=0; c<N; c++) 
      for (int i=0; i<width2; i++) {
        for (int j=0; j<height2; j++) {
          
            if (Math.sqrt(Math.pow((ycenter-j)/(ycenter-y),2) + Math.pow((xcenter-i)/(xcenter-x),2)) > 1) {
         
              changes.put(new History.PointIndex(i, j, c), fft[c][i][j]);
              img.setRGB(i, j, 0);
              fft[c][i][j] = new Complex(0, 0);

           }
            
        }
        
        // zmiana wartości paska postępu
        if (!progress.setProgress((int)(((float)(c*total + (i*image.getHeight()))/(float)totalN)*100))) return null;
          
      }
    
    fftHistory.push(changes);
    
    return img;
    
  }        
  
  
  /**
   * Cofnięcie zmian c danych FFT
   */
  public void undo() {
      
    HashMap<History.PointIndex, Complex> change = fftHistory.pop();
    fftRedo.clear();
    
    Iterator it = change.entrySet().iterator();
    while (it.hasNext()) {
     
        Map.Entry entry = (Map.Entry)it.next();
        History.PointIndex index = (History.PointIndex)entry.getKey();
        Complex c = (Complex)entry.getValue();
        fft[index.channel][index.x][index.y] = new Complex(c.re(), c.im());
        fftRedo.put(index, new Complex(0, 0));
        
    }      
      
  }
  
  
  /**
   * Ponowienie cofniętych zmian c danych FFT
   */
  public void redo() {
     
    HashMap<History.PointIndex, Complex> change = new HashMap<>();
    Iterator it = fftRedo.entrySet().iterator();
    while (it.hasNext()) {
     
        Map.Entry entry = (Map.Entry)it.next();
        History.PointIndex index = (History.PointIndex)entry.getKey();
        Complex c = fft[index.channel][index.x][index.y];
        change.put(index, new Complex(c.re(), c.im()));
        fft[index.channel][index.x][index.y] = new Complex(0, 0);
        
    }        
    
    fftRedo.clear();
    fftHistory.push(change);
    
  }
  
      
    
    
}
