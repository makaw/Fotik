/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing;

   
/**
 *
 * Dostępne filtry liniowe - splotowe
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public enum LinearFilterType { 
       
   NR_EVEN(1), NR_EVEN_9x9(7), NR_UNEVEN(2), NR_PARAMETRISED(3), NR_GAUSSIAN(4), SHARP_3x3(5), SHARP_HP(6),  
   EDGE_DETECT(11);

   
   /** Wewn. kod */
   private final int code;
      
   LinearFilterType(int code) {
          
     this.code = code;  
          
   }
      
      
   @Override
   public String toString() {
          
      switch (this) {

        default:              return super.toString();        
        case NR_EVEN:         return "Odszumiaj\u0105cy u\u015bredniaj\u0105cy, r\u00f3wnomierny (3\u00d73)";
        case NR_UNEVEN:       return "Odszumiaj\u0105cy lekko nier\u00f3wnomierny";
        case NR_PARAMETRISED: return "Odszumiaj\u0105cy parametryczny og\u00f3lny";
        case NR_GAUSSIAN:     return "Odszumiaj\u0105cy gaussowski z \u03c3=1";
        case NR_EVEN_9x9:     return "Odszumiaj\u0105cy u\u015bredniaj\u0105cy, r\u00f3wnomierny (9\u00d79)";
        case SHARP_3x3:       return "Wyostrzaj\u0105cy (3\u00d73)";
        case SHARP_HP:        return "Wyostrzaj\u0105cy parametryczny (g\u00f3rnoprzepustowy)";
                       
      }            
          
   }
      

   public int getCode() {
       return code;
   }                        
   
   /**
    * Czy filtr ma być pokazany na liście w oknie dialogowym
    * @return True jeżeli ma być pokazany
    */
   public boolean isVisibleInList() {
       
     return code < 10; 
       
   }
   
   /**
    * Czy filtr jest odszumiający
    * @return True jeżeli odszumiający
    */
   public boolean isNoiseRemoving() {
       
     return super.toString().startsWith("NR_");
       
   }
   
   /**
    * Czy filtr jest parametryzowany
    * @return True jeżeli parametryzowany
    */
   public boolean isParametrised() {
       
     return code != 3 && code != 6;  
       
   }
   
   
};  


    