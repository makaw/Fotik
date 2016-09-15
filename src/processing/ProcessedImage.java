/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing;

import gui.loader.IProgress;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import javax.swing.SwingConstants;

/**
 * Reprezentacja przetwarzanego obrazu - operacje na obrazie
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class ProcessedImage extends BufferedImage {

  /**
   * Konstruktor
   * @param img Przetwarzany obraz
   */
  public ProcessedImage(Image img) {

    super(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

    Graphics2D g2 = this.createGraphics();
    g2.drawImage(img, 0, 0, null);
    g2.dispose();

  }
    
  
  /**
   * Metoda zwraca współczynnik proporcji obrazu
   * @return Współczynnik proporcji obrazu
   */
  public double getRatio() {
      
    return (getHeight()>0)  ? (double)getWidth()/ (double)getHeight() : 0;
      
  }
  
  /**
   * Utworzenie kopii obrazu
   * @return Kopia obrazu
   */
  public BufferedImage getCopy() {
      
    ColorModel cm = getColorModel();
    boolean isAlphaPremultiplied = isAlphaPremultiplied();
    WritableRaster raster = copyData(getRaster().createCompatibleWritableRaster());
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    
  }  
  
  /**
   * Utworzenie kopii obrazu w odcieniach szarości
   * @return Kopia obrazu skonwertowana do odcieni szarości
   */  
  public BufferedImage getGrayCopy() {
      
    BufferedImage bw = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    Graphics2D g2 = bw.createGraphics();
    g2.drawImage(this, 0, 0, null);
    g2.dispose();
      
    return bw;
      
  }  
      

  /**
   * Rotacja obrazu
   * @param degrees Ilość stopni
   * @return Obraz po wykonaniu rotacji
   */
  public BufferedImage getRotatedCopy(int degrees) {
    
    double radians = Math.toRadians(degrees);
    // obliczenie nowych wymiarow obrazu  
    AffineTransform transform = new AffineTransform();
    transform.rotate(radians);
    Rectangle rotBounds = transform.createTransformedShape(new Rectangle(getWidth(), getHeight())).getBounds();
    // wykonanie translacji i rotacji
    transform = new AffineTransform();
    transform.translate(-rotBounds.x, -rotBounds.y);
    transform.rotate(radians);
      
    BufferedImage rotImage = new BufferedImage(rotBounds.width, rotBounds.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = rotImage.createGraphics();
    g2.drawImage(this, transform, null);
    g2.dispose();
   
    return rotImage;
        
  }
  
  
  /**
   * Odbicie obrazu
   * @param orient kierunek odbicia (pionowo, poziomo)
   * @return Obraz po wykonaniu odbicia
   */  
  public BufferedImage getFlippedCopy(int orient) {
      
    AffineTransform tx;  
      
    if (orient==SwingConstants.HORIZONTAL) {
      tx = AffineTransform.getScaleInstance(-1, 1);
      tx.translate(-getWidth(), 0);
    }
    else {
      tx = AffineTransform.getScaleInstance(1, -1);
      tx.translate(0, -getHeight());
    }
    
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
    return op.filter(this, null);
      
  }
  
  
  
  /**
   * Skalowanie obrazu
   * @param width Nowa szerokość (px)
   * @param height Nowa wysokość (px)
   * @param interpolation Rodzaj zastosowanej interpolacji
   * @return Obraz po wykonaniu skalowania
   */
  public BufferedImage getScaledCopy(int width, int height, int interpolation) {

    AffineTransform transform = AffineTransform.getScaleInstance((double)width/getWidth(), 
            (double)height/getHeight());
    AffineTransformOp atOp = new AffineTransformOp(transform, interpolation);
    return atOp.filter(this, new BufferedImage(width, height, getType()));
    
  } 
  
  /**
   * Skalowanie obrazu (domyślna interpolacja)
   * @param width Nowa szerokość (px)
   * @param height Nowa wysokość (px)
   * @return Obraz po wykonaniu skalowania
   */  
  public BufferedImage getScaledCopy(int width, int height) {
      
    return getScaledCopy(width, height, AffineTransformOp.TYPE_BILINEAR);  
      
  }
  
  
  /**
   * Miniatura - skalowanie obrazu z zachowaniem proporcji
   * @param width Nowa szerokość (px)
   * @param maxHeight Maksymalna nowa szerokość (px)
   * @return Miniatura obrazu
   */
  public BufferedImage getPScaledCopy(int width, int maxHeight) {

    int height = (int)(width/getRatio());
    if (height>maxHeight) {
        height = maxHeight;
        width = (int)(height*getRatio());
    } 
      
    return getScaledCopy(width, height);
    
  }   
    

  /**
   * Nałożenie filtra liniowego
   * @param filter Filtr
   * @return Obraz po nałożeniu filtra
   */
  public BufferedImage getLFilteredCopy(ILinearFilter filter) {
               
    return new ConvolveOp(filter.getMask(), ConvolveOp.EDGE_NO_OP, null).filter(this, null);
      
  }    
  
  
  /**
   * Nałożenie filtra "Inteligentnego rozmycia" (algorytm Smart Blur)
   * @param blurFilter Typ liniowego filtra odszumiającego
   * @param sensitivity Czułość filtra "Smart blur"
   * @param size Rozmiar kolejno rozmywanych fragmentów obrazu
   * @param progress Interfejs okna z paskiem postępu
   * @return Obraz po nałożeniu filtra
   */
  public BufferedImage getSmartBlurredCopy(LinearFilterType blurFilter, double sensitivity, 
          int size, IProgress progress) {
      
    return new SmartBlurFilter(this, LinearFilterFactory.getFilter(blurFilter), 
            sensitivity, size, progress).filter();      
      
  }
  
  /**
   * Nałożenie filtra medianowego
   * @param size Rozmiar filtra
   * @param progress
   * @return Obraz po nałożeniu filtra
   */
  public BufferedImage getMedianFilteredCopy(int size, IProgress progress) {

    return new MedianFilter(this, size, progress).filter();
            
  }
     
  
  /**
   * Nałożenie adaptacyjnego filtra medianowego
   * @param medianSize Rozmiar maski filtra medianowego
   * @param threshold Próg dla kwalifikowania krawędzi
   * @param edgeParam Parametr filtra wykrywania krawędzi
   * @param inverseEdgeDetection Czy odwrócić wykryte krawędzie
     * @param noiseRemoveEdgeDetection Czy 
   * @param progress Interfejs okna z paskiem postępu
   * @return Obraz po nałożeniu filtra
   */
  public BufferedImage getAdaptiveMedianFilteredCopy(int medianSize, int threshold, float edgeParam, 
          boolean inverseEdgeDetection, boolean noiseRemoveEdgeDetection, IProgress progress) {

    return new AdaptiveMedianFilter(this, medianSize, threshold, edgeParam,
            inverseEdgeDetection, noiseRemoveEdgeDetection, progress).filter();
            
  }
  
  
  /**
   * Wykrywanie krawędzi 
   * @param edgeParam Parametr filtra wykrywającego krawędzie
   * @param noiseRemove True jeżeli wcześniej wykonać odszumienie filtrem gaussowskim
   * @return Obraz wykrytych krawędzi
   */
  public BufferedImage getEdgeImage(float edgeParam, boolean noiseRemove) {
 
    BufferedImage img = noiseRemove ? new ConvolveOp(LinearFilterFactory.getFilter(LinearFilterType.NR_GAUSSIAN).getMask(),
             ConvolveOp.EDGE_NO_OP, null).filter(this, null) : this;
    
    return new ConvolveOp(LinearFilterFactory.getFilter(LinearFilterType.EDGE_DETECT, edgeParam).getMask(),
            ConvolveOp.EDGE_NO_OP, null).filter(img, null);
    
  }

  
  /**
   * Rozciągnięcie histogramu
   * @param histogram Aktualny histogram
   * @param threshold Próg (minimalna ilość punktów na poziomie)
   * @return Obraz po rozciągnięciu histogramu
   */
  public BufferedImage getHStretchedCopy(Histogram histogram, int threshold) {

    BufferedImage img = getCopy();
    histogram.stretch(img, threshold);
    return img;

  }
  
  
  /**
   * Wyrównanie histogramu
   * @param histogram Aktualny histogram
   * @return Obraz po rozciągnięciu histogramu
   */
  public BufferedImage getHEqualizedCopy(Histogram histogram) {

    BufferedImage img = getCopy();
    histogram.equalize(img);   
    return img;

  }  
  
  
  /**
   * Korekcja gamma
   * @param gamma Parametr
   * @param channel Przetwarzany kanał histogramu
   * @return Obraz po korekcji gamma
   */
  public BufferedImage getGammaCorrectedCopy(float gamma, HistogramChannel channel) {

    BufferedImage img = getCopy();
    
        // tablica "look up"  
    float[] lut = new float[256];
    float g = 1.0f/gamma;
    for(int i=0;i<256;i++) lut[i] = (float)(Math.pow((float)i/255.0f, g));
 
    Histogram.applyLUT(img, lut, channel);
    
    return img;

  }    
  
  /**
   * Obraz po korekcji jasności i kontrastu, wersja statyczna
   * @param srcImage Obraz źródłowy
   * @param offset Przesunięcie jasności
   * @param scaleFactor Skala kontrastu
   * @return Obrazk po korekcji
   */
  public static BufferedImage getBCAdjustedCopy(BufferedImage srcImage, float offset, float scaleFactor) {
      
     BufferedImage dest = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null), 
             BufferedImage.TYPE_INT_RGB);
     Graphics2D g2 = dest.createGraphics();
     g2.drawImage(srcImage, 0, 0, null);
     g2.dispose();
     
     return new RescaleOp(scaleFactor, offset, null).filter(dest, dest);      
      
  }
  
  
  /**
   * Obraz po korekcji jasności i kontrastu 
   * @param offset Przesunięcie jasności
   * @param scaleFactor Skala kontrastu
   * @return Obraz po korekcji
   */  
  public BufferedImage getBCAdjustedCopy(float offset, float scaleFactor) {
      
     return getBCAdjustedCopy(this, offset, scaleFactor);
      
  }
  
  /**
   * Obraz po korekcji HSB
   * @param hOffset Przesunięcie odcienia
   * @param sOffset Przesunięcie nasycenia
   * @param bOffset Przesunięcie jasności
   * @param progress Interfejs okna z paskiem postępu
   * @return Obraz po korekcji
   */  
  public BufferedImage getHSBAdjustedCopy(float hOffset, float sOffset, float bOffset, IProgress progress) {
      
     return new HSBFilter(this, hOffset, sOffset, bOffset, progress).filter();
      
  }
  
  /**
   * Obraz po korekcji HSB
   * @param hOffset Przesunięcie odcienia
   * @param sOffset Przesunięcie nasycenia
   * @param bOffset Przesunięcie jasności
   * @return Obraz po korekcji
   */    
  public BufferedImage getHSBAdjustedCopy(float hOffset, float sOffset, float bOffset) {
      
     return new HSBFilter(this, hOffset, sOffset, bOffset).filter();
      
  }  
  
  
  /**
   * Obraz po automatycznym balansie bieli
   * @param progress Interfejs okna z paskiem postępu
   * @return Obraz po korekcji
   */    
  public BufferedImage getWhiteAdjustedCopy(IProgress progress) {
      
    return new WhiteBalanceFilter(getCopy(), progress).filter();
      
  }
  
  
  /**
   * Zwraca obraz pokolorowany na czerwono (tło do edycji maski)
   * @return Pokolorowany obraz
   */
  public BufferedImage getRedColorizedImage() {
      
    return (new ProcessedImage(getGrayCopy()).getHSBAdjustedCopy(-127, 127, 50));              
      
  }
    
  
    
}
