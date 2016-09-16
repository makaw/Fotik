/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.photoview;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.HashSet;
import java.util.Iterator;


/**
 * Maska obrazu do filtrowania (przetworzone zostaną tylko oznaczone fragmenty obrazu)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class Mask extends HashSet<Shape> {
    
  /** Szerokość i wysokość obrazu */  
  private final int width, height;
  /** Bieżące zaznaczenie (w trakcie) */
  private Shape currentSelection;
  
  
  /**
   * Konstruktor
   * @param width Szerokość obrazu
   * @param height Wysokość obrazu
   */
  public Mask(int width, int height) {
      
    this.width = width;
    this.height = height;      
      
  }

  /**
   * Dodanie do maski bieżącego zaznaczenia
   * @param zoom Aktualne powiększenie widoku
   */
  public void addCurrentSelection(double zoom) {
      
    if (currentSelection == null) return;  
    
    
    if (currentSelection instanceof Rectangle) {
      Rectangle rect = (Rectangle)getScaledShape((Rectangle)currentSelection, 10000.0/zoom);    
      if (rect.x + rect.width > width) rect.width = width - rect.x;
      if (rect.y + rect.height > height) rect.height = height - rect.y;
      add(rect);
    }
    else if (currentSelection instanceof Ellipse2D) {
      Ellipse2D.Double e = (Ellipse2D.Double)getScaledShape((Ellipse2D.Double)currentSelection, 10000.0/zoom);    
      if (e.x + e.width > width) e.width = width - e.x;
      if (e.y + e.height > height) e.height = height - e.y;
      add(e);
    }
    currentSelection = null;
      
  }
  
  /**
   * Usunięcie wskazanego fragmentu maski
   * @param x Wsp. x
   * @param y Wsp. y
   * @param zoom Aktualne powiększenie widoku
   */
  public void removeSelection(int x, int y, double zoom) {
      
    Rectangle tmp = (Rectangle)getScaledShape(new Rectangle(x, y, 1, 1), 10000.0/zoom);  
    
    Iterator<Shape> iterator = this.iterator();

    while (iterator.hasNext()) {
        
       Shape s = iterator.next();
       if (s.contains(new Point(tmp.x, tmp.y))) iterator.remove();
        
    }
      
  }
  
  
  public Shape getCurrentSelection() {
    return currentSelection;
  }

  
  public void setCurrentSelection(Shape currentSelection) {
    this.currentSelection = currentSelection;
  }
  
  
  /**
   * Metoda zwraca przeskalowany do widoku 100% kształt (prostokąt lub elipsa)
   * @param shape Kształt do przeskalowania
   * @param zoom Aktualne powiększenie widoku
   * @return  Przeskalowany do widoku 100% kształt
   */
  public static Shape getScaledShape(Shape shape, double zoom) {
      
    if (shape instanceof Rectangle) {  
      Rectangle rect = (Rectangle)shape;  
      Rectangle r = new Rectangle();
      r.width = (int)((double)rect.width*(zoom/100.0));  
      r.height = (int)((double)rect.height*(zoom/100.0));  
      r.x = (int)((double)rect.x*(zoom/100.0));  
      r.y = (int)((double)rect.y*(zoom/100.0));  
      return r;
    }
    
    else if (shape instanceof Ellipse2D) {
        
      Ellipse2D.Double ellipse = (Ellipse2D.Double)shape;  
      Ellipse2D.Double e = new Ellipse2D.Double();
      e.width = (int)(ellipse.width*(zoom/100.0));  
      e.height = (int)(ellipse.height*(zoom/100.0));  
      e.x = (int)(ellipse.x*(zoom/100.0));  
      e.y = (int)(ellipse.y*(zoom/100.0));  
      return e;
        
    }
    
    else return null;
      
  }

    
    
    
}
