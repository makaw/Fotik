/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing;

import java.awt.Color;

/**
 *
 * Dostępne kanały histogramu obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public enum HistogramChannel {
    
   ALL(0), RED(1), GREEN(2), BLUE(3);
    
    
  /** Wewn. kod */
   private final int code;
      
   HistogramChannel(int code) {
          
     this.code = code;  
          
   }
      
      
   @Override
   public String toString() {
          
      switch (this) {

        default:                   
        case ALL:         return "Globalna jasno\u015b\u0107 (HSB)";
        case RED:         return "Czerwony (RGB)";
        case GREEN:       return "Zielony (RGB)";
        case BLUE:        return "Niebieski (RGB)";
                       
      }            
          
   }
      

   public int getCode() {
       return code;
   }                       
   
   
   /**
    * Metoda zwraca składową koloru właściwą dla danego kanału
    * @param rgb Kolor (RGB)
    * @return Właściwa składowa
    */
   public int getValue(int rgb) {
       
      Color color = new Color(rgb);
       
      switch (this) {
          
        default:
        case ALL:   
            
            float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            return (int)(hsb[2] * 255.0f);
            
        case RED:     return color.getRed();
        case GREEN:   return color.getGreen();
        case BLUE:    return color.getBlue();
       
      }
   
   
    }
   
   
   /**
    * Przemnożenie właściwej składowej przez odpowiedni element tablicy LUT
    * @param rgb Kolor (RGB)
    * @return Kolor po przemnożeniu składowej przez LUT
    */
    public int getLUTValue(int rgb, float[] lut) {
        
      Color color = new Color(rgb);
       
      switch (this) {
          
        default:
        case ALL:   
          
           float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);  
           hsb[2] = lut[(int)(hsb[2]*255.0f)];
           return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);        
        
        case RED:
            
           return new Color((int)(lut[color.getRed()]*255.0f), color.getGreen(), color.getBlue()).getRGB();           
            
        case GREEN:
            
           return new Color(color.getRed(), (int)(lut[color.getGreen()]*255.0f),color.getBlue()).getRGB();
            
        case BLUE:
            
           return new Color(color.getRed(), color.getGreen(), (int)(lut[color.getBlue()]*255.0f)).getRGB(); 
          
      }
        
    }
    
    /**
     * Kolor do wyświetlenia słupków histogramu
     * @return Kolor słupków histogramu
     */
    public Color getColor() {
        
      switch (this) {
          
          default:
          case ALL:  return Color.DARK_GRAY;
          case RED:         return new Color(0x660000);
          case GREEN:       return new Color(0x006600);
          case BLUE:        return new Color(0x000066);      
          
      }  
        
        
    }

   
}