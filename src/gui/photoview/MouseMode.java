/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01-02/2016
 */
package gui.photoview;

import java.awt.Cursor;
import java.awt.Toolkit;


/**
 * Tryby narzędzia (maska: zaznaczenie prostokątne, zaznaczenie eliptyczne, usuwanie, 
 * obraz: podgląd, kadrowanie, klonowanie)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public enum MouseMode {
    
  ADD_RECTANGLE("add_rect"), ADD_ELLIPSE("add_ellipse"), REMOVE("remove"), 
  VIEW("view"), CROP("crop"), CLONE("clone"), CLONE_PUT("clone_put"), BLUR("blur"), SMUDGE("smudge");
  
  /** Tekstowa "komenda" */
  private final String command;
  
  /**
   * Konstruktor
   * @param command Tekstowa "komenda" 
   */
  MouseMode(String command) {
    this.command = command;  
  }
  
  
  /**
   * Zwraca tryb na podstawie tekstowej "komendy"
   * @param command Tekstowa "komenda"
   * @return Odpowiedni tryb
   */
  public static MouseMode fromString(String command) {
     
    for(MouseMode mode : values()) 
        if (mode.command.equals(command)) return mode;
    
    return null;
    
  }  
  
  
   
  @Override
  public String toString() {
      
     return command;
      
  }  
  
  /**
   * Nazwa do wyświetlenia na pasku narzędzi
   * @return Nazwa do wyświetlenia 
   */
  public String getName() {
      
    switch (this) {
        
      default:        return "";
      case CROP:      return "kadrowanie";
      case CLONE_PUT: return "klonowanie (stempel)";
      case CLONE:     return "klonowanie (wyb\u00f3r)";
      case BLUR:      return  "odszumianie";
      case SMUDGE:    return "smu\u017cenie";
        
    }  
      
      
  }
  
  
  /**
   * Czy jest włączone zaznaczanie na masce
   * @return True jeżeli zaznaczanie na masce
   */
  public boolean isAddMaskMode() {
      
     return (this == ADD_RECTANGLE || this == ADD_ELLIPSE); 
      
  }
  
  
  /**
   * Kursor odpowiedni dla danego trybu
   * @return Kursor dla danego trybu
   */
  public Cursor getCursor() {
      
     switch (this) {
         
         default:
         case VIEW:             return new Cursor(Cursor.DEFAULT_CURSOR);
         case CROP:        
         case CLONE:
         case ADD_RECTANGLE:    
         case ADD_ELLIPSE:      return new Cursor(Cursor.CROSSHAIR_CURSOR);
         case BLUR:
         case SMUDGE:
         case CLONE_PUT:
         case REMOVE:           return new Cursor(Cursor.HAND_CURSOR); 
             
     } 
      
      
  }
    
}
