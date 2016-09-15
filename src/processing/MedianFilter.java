/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */

package processing;


import gui.loader.IProgress;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;


/**
 * Odszumiający filtr medianowy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class MedianFilter {
    
    /** Domyślny rozmiar maski filtra */
    public static final int defaultSize = 3;
    
    /** Rozmiar maski filtra (bok kwadratu) */ 
    private final int size; 
    /** Przetwarzany obraz */
    protected final BufferedImage image;
    /** Interfejs paska postępu */
    protected final IProgress progress;
    
    /**
     * Konstruktor
     * @param image Przetwarzany obraz
     * @param size Rozmiar maski filtra
     * @param progress Interfejs paska postępu
     */
    public MedianFilter(BufferedImage image, int size, IProgress progress) {
        
       if (!isSizeCorrect(size)) size = defaultSize;
       this.size = size;
       this.image = image;
       this.progress = progress;
        
    }
    
    public MedianFilter(BufferedImage image, int size) {
        
       this(image, size, null);  
        
    }
 
       
    /**
     * Wyznaczenie mediany wartości tablicy
     * @param a Tablica wejściowa
     * @return Mediana
     */
    private int median(int[] a) {
        
      Arrays.sort(a);
      int mid = a.length/2;
      
      return (a.length%2 == 1) ?  a[mid] : (a[mid-1] + a[mid])/2;         
        
    }
    
    
    protected int getMedianColor(int x, int y) {
        
      int[] nArray = getNeighbourhoodArray(x, y);
      int[] red = new int[nArray.length];
      int[] green = new int[nArray.length];
      int[] blue = new int[nArray.length];
          
      // rozklad na kanaly RGB  
      for (int c = 0; c < nArray.length; c++) {
         red[c] = new Color(nArray[c]).getRed();
         green[c] = new Color(nArray[c]).getGreen();
         blue[c] = new Color(nArray[c]).getBlue();
      }
      
      return new Color(median(red), median(green), median(blue)).getRGB();        
        
    }
    
    
    /**
     * Tablica sąsiedztwa punktu
     * @param x Wsp, x punktu
     * @param y Wsp. y punktu
     * @return Tablica sąsiedztwa
     */
    protected int[] getNeighbourhoodArray(int x, int y) {
        
        int xmin = x-size/2;
        int xmax = x+size/2;
        int ymin = y-size/2;
        int ymax = y+size/2;
                
        if (xmin < 0)  xmin = 0;
        if (xmax > (image.getWidth()-1)) xmax = image.getWidth()-1;
        if (ymin < 0) ymin = 0;
        if (ymax > (image.getHeight()-1))  ymax = image.getHeight()-1;
                
        int nsize = (xmax-xmin+1)*(ymax-ymin+1);
        int[] nArray = new int[nsize];
        int k = 0;
        for (int i=xmin; i<=xmax; i++)
          for (int j=ymin; j<=ymax; j++) {
             nArray[k] = image.getRGB(i, j);  
             k++;
          }
        return nArray;
        
    }
    
    /**
     * Przefiltrowanie obrazu
     * @return Przefiltrowany obraz
     */
    public BufferedImage filter() {
        
        BufferedImage dstImage = 
                new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        int total = image.getWidth() * image.getHeight();
        
        for (int j = 0; j < image.getHeight(); j++) {
          for (int i = 0; i < image.getWidth(); i++) {                
                        
            // ustalenie koloru piksela jako mediany
            dstImage.setRGB(i, j, getMedianColor(i, j));            
                            
          }
          
          // zmiana wartości paska postępu
          if (!progress.setProgress((int)(((float)(j*image.getWidth())/(float)total)*100))) return null;
          
        }
        
        return dstImage;
        
    }
    
    
   
    /**
     * Sprawdzenie poprawności rozmiaru filtra
     * @param size Rozmiar filtra
     * @return True jeżeli poprawny
     */
    public static final boolean isSizeCorrect(int size) {
        
      return size%2!=0 && size>=3;  
        
    }
        
    
}
