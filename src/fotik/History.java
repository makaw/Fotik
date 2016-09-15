/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package fotik;

/**
 *
 * Historia zmian (implementacja stosu)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <T> Klasa wersjonowanego obiektu (BufferedImage, Complex ...)
 * 
 */
public class History<T> {
    
 
    
  /** Wewn. klasa - indeks punktu obrazu (kanał i współrzędne) */
  public static class PointIndex {
      
    /** Atrybuty: nr kanału i współrzędne ekranowe */  
    public int x, y, channel;  
    
    public PointIndex(int x, int y, int channel) {
      this.x = x;  
      this.y = y;  
      this.channel = channel;  
    }
      
  }
  
    
  /** Wewn. klasa - jedna z wersji obiektu */  
  private class Version {
      
     /** Obraz */ 
     private final T object;
     /** Referencja do poprzedniej wersji */
     private final Version previous;     
     /** Czy został zmieniony rozmiar  */
     private final boolean sizeChanged;
          
     Version(T object, Version previous, boolean sizeChanged) {
       this.object = object;  
       this.previous = previous;
       this.sizeChanged = sizeChanged;
     }

      
  }  
  
  /** Ostatnia wersja (obiekt ze szczytu stosu) */
  private Version top; 
  
  /** Oryginał */
  private T original;
  
  /** Ilość elementów */
  private int size = 0;
  
  
  /**
   * Domyślny pusty konstruktor
   */
  public History() {}
  
  
  /**
   * Konstruktor do kopiowania
   * @param history Kopiowany obiekt
   */
  private History(History<T> history) {
      
     this.top = history.top; 
     
  }

  
  /**
   * Dodanie wersji (położenie na stosie)
   * @param object Wersjonowany obiekt
   * @param sizeChanged True jeżeli był zmieniony rozmiar (np. obrazu)
   */
  public void push(T object, boolean sizeChanged) {

    if (original == null) original = object;  
    top = new Version(object, top, sizeChanged); 
    size++;
    checkSize();
    
  }      
  
  
  /**
   * Dodanie wersji (położenie na stosie)
   * @param object Obiekt
   */
  public void push(T object) {
      
    push(object, false);
      
  }        
  
  
  
  /**
   * Pobranie poprzedniej wersji obiektu (zdjęcie ze stosu)
   * @return Poprzednia wersja  
   */
  public T pop() {
         
    T tmp = top.object;
    top = top.previous;    
    size--;
    
    return tmp;      
      
  }

  
  /**
   * Czy zmieniono rozmiar w ostatniej wersji
   * @return True jeżeli zmieniono rozmiar
   */
  public boolean isSizeChanged() {
      
    return top.sizeChanged;  
      
  }
  
  
  
  /**
   * Czy stos jest pusty
   * @return True jeżeli pusty
   */
  public boolean isEmpty() {
      
    return (top == null);  
      
  }
      
  
  /**
   * Zwrócenie oryginalnego obiektu
   * @return Oryginalny obiekt
   */
  public T getOriginal() {

    return original;   
      
  }
  
  
  /**
   * Kontrola i w razie potrzeby przycięcie stosu
   */
  private void checkSize() {
      
    if (size<=IConf.MAX_UNDOS) return;
     
    History<T> tmp = new History<>();
    int n = 0;
    while (++n < IConf.MAX_UNDOS && top != null) tmp.push(pop());
    while (top != null) pop();
    while (tmp.top != null) push(tmp.pop());
    
  }
  
  
    
}
