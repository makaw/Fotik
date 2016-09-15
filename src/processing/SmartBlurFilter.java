/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01-02/2016
 */
package processing;


import gui.loader.IProgress;
import java.awt.Color;
import java.awt.image.BufferedImage;


/**
 * Odszumiający filtr "Smart Blur" (Inteligentne rozmycie)
 * 
 * Na podstawie:
 * http://asserttrue.blogspot.com/2010/08/implementing-smart-blur-in-java.html
 * 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class SmartBlurFilter {

   /** Domyślna czułość filtra */
   public static final double defaultSensitivity = 10.0;
   /** Domyślna wielkość rozmywanych fragmentów obrazu */
   public static final int defaultSize = 5;
      
   /** Czułość filtra */
   private final double sensitivity;
   /** Wielkość kolejno rozmywanych fragmentów obrazu */
   private final int size;
   /** Przetwarzany obraz */
   private final ProcessedImage image;
   /** Interfejs okna z paskiem postępu */
   private final IProgress progress;
   /** Wybrany filtr odszumiający / rozmywający */
   private final ILinearFilter blurFilter;
   
   
   /**
    * Konstruktor
    * @param image Przetwarzany obraz
    * @param blurFilter Wybrany filtr odszumiający / rozmywający
    * @param sensitivity Czułość filtra
    * @param size Rozmiar kolejno rozmywanych fragmentów obrazu
    * @param progress Interfejs okna z paskiem postępu
    */
   public SmartBlurFilter(ProcessedImage image, ILinearFilter blurFilter, 
           double sensitivity, int size, IProgress progress) {
       
     this.sensitivity = sensitivity;
     this.size = size;
     this.image = image;
     this.blurFilter = blurFilter;
     this.progress = progress;
     
   }   


   /**
    * Nałożenie filtra
    * @return Obraz z nałożonym filtrem
    */
   public BufferedImage filter() {      

     // kopia obrazu z nałożonym filtrem liniowym odszumiającym
     BufferedImage destImg = new ProcessedImage(image).getLFilteredCopy(blurFilter);    
     
     int total = image.getWidth() * image.getHeight();     
     
     for (int j = 0; j < image.getHeight(); j++) {
       for (int i = 0; i < image.getWidth(); i++) {                
                        
         double amount; 
         try {
           amount = rmse(image.getSubimage(i, j, size, size).getRGB(0, 0, size, size, null, 0, size));
         }
         // wyjście poza wymiary obrazu
         catch (Exception e) { continue; }
                   
         amount = amount > sensitivity ? 1.0 : amount / sensitivity;
         // interpolacja pomiędzy wartością RGB oryg.obrazu, a obrazu rozmytego
         destImg.setRGB(i, j, interpolate(destImg.getRGB(i, j), image.getRGB(i, j), amount));
                            
       }
          
       // zmiana wartości paska postępu
       if (!progress.setProgress((int)(((float)(j*image.getWidth())/(float)total)*100))) return null;
          
     }
       
     return destImg;
            
   }
   

   /**
    * Średnia kwadratowa błędów - sqrt(RMS)
    * @param pixels Tablica pikseli
    * @return Średnia kwadratowa błędów
    */
   private static double rmse(int [] pixels) {

     double average = 0;
     for (int i = 0; i < pixels.length; i++) average += (pixels[ i ] >> 8) & 255;     
     average /= pixels.length;

     double diffSum = 0;
     for ( int i = 0; i < pixels.length; i++ ) 
         diffSum += Math.pow(((pixels[ i ] >> 8) & 255) - average, 2);

     return Math.sqrt(diffSum / pixels.length);
       
   }

   
   
   
   /**
    * Liniowa interpolacja
    * @param a Wartość brzegowa
    * @param b Wartość brzegowa
    * @param amount Ilość wartości pomiędzy a i b
    * @return Interpolowana wartość
    */
   private static double interpolate(double a, double b, double amount) {
       
      return a + amount * ( b - a );
      
   }   
   
   
   /**
    * Liniowa interpolacja koloru
    * @param a Wartość brzegowa RGB
    * @param b Wartość brzegowa RGB
    * @param amount Ilość wartości pomiędzy a i b
    * @return Wartość RGB interpolowanego koloru
    */
   private static int interpolate(int a, int b, double amount) {
      
      Color ca = new Color(a);
      Color cb = new Color(b);
      
      int red = (int)SmartBlurFilter.interpolate((double)ca.getRed(), (double)cb.getRed(), amount);
      int green = (int)SmartBlurFilter.interpolate((double)ca.getGreen(), (double)cb.getGreen(), amount);
      int blue = (int)SmartBlurFilter.interpolate((double)ca.getBlue(), (double)cb.getBlue(), amount);
     
      return new Color(red, green, blue).getRGB();
      
   }
   
   
   
}



