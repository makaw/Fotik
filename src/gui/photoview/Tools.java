/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01-02/2016
 */
package gui.photoview;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import processing.LinearFilterFactory;
import processing.LinearFilterType;
import processing.ProcessedImage;


/**
 * Lokalne operacje na obrazie (narzędzia)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Tools {
    
  /** Bufor dla lokalnego smużenia */
  private double[][][] smudgeBuffer;  
  /** Interfejs okna z przetwarzanym obrazem */
  private final IPhotoFrame photoFrame;
  /** Fragment obrazu wybrany do klonowania */
  private BufferedImage cloneImg;
  
  
  /**
   * Konstruktor
   * @param photoFrame Interfejs okna z przetwarzanym obrazem
   */
  public Tools(IPhotoFrame photoFrame) {
      
    this.photoFrame = photoFrame;  
      
  }
  
  /**
   * Lokalne odszumianie / rozmywanie
   * @param x Wsp. x punktu startowego
   * @param y Wsp. y punktu startowego
   * @param x2 Wsp. x punktu końcowego
   * @param y2 Wsp. y punktu końcowego
   * @return Obraz po wykonaniu lokalnego odszumienia
   */
  public BufferedImage blur(int x, int y, int x2, int y2) {        
       
    BufferedImage img = photoFrame.getPhotoPanel().getPhoto().getImage().getCopy();
    int radius = photoFrame.getLocalBlurSize();
      
    int sx = x < x2 ? 1 : -1;
    int sy = y < y2 ? 1 : -1;       
       
    for (int i=x; sx>0 ? i<x2 : i>x2; i+=sx*radius) 
      for (int j=y; sy>0 ? j<y2 : j>x2; j+=sy*radius) {  
       
        try {  
             
          Ellipse2D.Double circle = new Ellipse2D.Double(i, j, radius, radius);           

          BufferedImage b = new ProcessedImage(img.getSubimage(i, j, radius, radius)).
              getLFilteredCopy(LinearFilterFactory.getFilter(LinearFilterType.NR_GAUSSIAN));
             
          Graphics2D g2 = (Graphics2D)img.getGraphics();  
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2.setClip(circle);
          g2.drawImage(b, i, j, null);     
          g2.setClip(null);
          g2.dispose();
           
        }
        catch (Exception e) { 
         // System.err.println(e); 
        }
                  
      }

    return img;
                
  }
    
    
  /**
   * Inicjalizacja bufora lokalnego smużenia
   * @param x Wsp. x punktu startowego
   * @param y Wsp. y punktu startowego
   */
  public void initSmudge(int x, int y) {
        
    BufferedImage img = photoFrame.getPhotoPanel().getPhoto().getImage().getCopy();
    int size = photoFrame.getLocalSmudgeSize();
    int maxSize = photoFrame.getMaxLocalSmudgeSize();
    
    smudgeBuffer = new double[3][maxSize][maxSize];
            
    for (int i=0; i<size; i++)
     for (int j=0; j<size; j++) {
      
        int nx = x + i - size/2;
        int ny = y + j - size/2;                
        
        try {
          Color color = new Color(img.getRGB(nx,ny));
          smudgeBuffer[0][i][j] = color.getRed();
          smudgeBuffer[1][i][j] = color.getGreen();
          smudgeBuffer[2][i][j] = color.getBlue();
        }
        // poza zakresem: -1 do bufora
        catch (Exception e) {
           smudgeBuffer[0][i][j] = -1;  
        }
     }
      
  }
    
  
  /**
   * Wykonanie lokalnego smużenia
   * @param x1 Początkowa wsp. x
   * @param y1 Początkowa wsp. y
   * @param x2 Końcowa wsp. x
   * @param y2 Końcowa wsp. y
   * @return Obraz po wykonaniu lokalnego smużenia
   */
  public BufferedImage smudge(int x1, int y1, int x2, int y2) {
        
    if (smudgeBuffer == null) return null;   
      
    BufferedImage img = photoFrame.getPhotoPanel().getPhoto().getImage().getCopy();
    int size = photoFrame.getLocalSmudgeSize();
      
    int dist = Math.max(Math.abs(x2-x1),Math.abs(y2-y1));
    double dx = (double)(x2-x1)/dist;
    double dy = (double)(y2-y1)/dist;
    
    for (int d=1; d<=dist; d++) {
                
      int x = (int)Math.round(x1 + dx*d);
      int y = (int)Math.round(y1 + dy*d);
           
      for (int i=0; i<size; i++)
        for (int j=0; j<size; j++) {
              
          int nx = x + i - size/2;
          int ny = y + j - size/2;
          
          if (smudgeBuffer[0][i][j] != -1) try {
            
            Color currColor = new Color(img.getRGB(nx,ny));
            int red = (int)(currColor.getRed()*0.7 + smudgeBuffer[0][i][j]*0.3);
            int green = (int)(currColor.getGreen()*0.7 + smudgeBuffer[1][i][j]*0.3);
            int blue = (int)(currColor.getBlue()*0.7 + smudgeBuffer[2][i][j]*0.3);
            int color = new Color(red, green, blue).getRGB();
            
            img.setRGB(nx,ny,color);
            smudgeBuffer[0][i][j] = currColor.getRed()*0.3 + smudgeBuffer[0][i][j]*0.7;
            smudgeBuffer[1][i][j] = currColor.getGreen()*0.3 + smudgeBuffer[1][i][j]*0.7;
            smudgeBuffer[2][i][j] = currColor.getBlue()*0.3 + smudgeBuffer[2][i][j]*0.7;
                     
          }
          
          catch (Exception e) {}
                  
        }               
            
    }      
      
    smudgeBuffer = null;
    return img;
             
  }
    
  
  /**
   * Pobranie fragmentu obrazu do klonowania
   * @param x Wsp. x całego obrazu
   * @param y Wsp. y całego obrazu
   * @param width Szerokość fragmentu
   * @param height Wysokość fragmentu
   */
  public void setCloneImg(int x, int y, int width, int height) {
            
    try {
      cloneImg = photoFrame.getPhotoPanel().getPhoto().getImage().getSubimage(x, y, width, height);
      photoFrame.setCloneThumb(new ImageIcon(cloneImg));
    }
    catch (Exception e) {}
      
  }
  
  
  /**
   * Nałożenie "stempla" (klonowanie 2 faza)
   * @param x Wsp. x oryg. obrazu
   * @param y Wsp. y oryg. obrazu
   * @return Obraz z nałożonym "stemplem"
   */
  public BufferedImage cloneStamp(int x, int y) {
      
     if (cloneImg == null) return null;
     
     BufferedImage img = photoFrame.getPhotoPanel().getPhoto().getImage().getCopy();
     
     Graphics2D g2 = (Graphics2D)img.getGraphics();  
     g2.drawImage(cloneImg, x, y, null);     
     g2.dispose(); 
      
     return img;
      
  }
  
  
  /**
   * Metoda zwraca punkt o przeskalowanych do widoku 100% współrzędnych
   * @param point punkt do przeskalowania
   * @param zoom Aktualne powiększenie widoku
   * @return  Punkt o przeskalowanych do widoku 100% współrzędnych
   */
  public static Point getScaledPoint(Point point, double zoom) {
     
    return new Point((int)((double)point.x*(zoom/100.0)), (int)((double)point.y*(zoom/100.0)));
        
  }

    
    
  
    
}
