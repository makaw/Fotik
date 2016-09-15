/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.photoview;


import gui.GUI;
import java.awt.BasicStroke;
import java.awt.Color;
import processing.ProcessedImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;


/**
 * Wyświetlany obraz (zdjęcie) z obsługą przewijania
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Photo extends JLabel implements Scrollable {

    /** Przezroczystość warstwy maski (wartość kanału alpha) */
    private static final int maskLayerAlpha = 90;
    
    /** Maksymalna jednostka przewinięcia */
    private int maxUnitInc;
    /** Obraz */
    private ProcessedImage image;
    /** Powiększenie obrazu */
    private double zoom = GUI.defaultZoom;
    /** Warstwa maski */
    private final Mask mask;
    /** Czy widoczna warstwa maski */
    private boolean maskLayerVisible = false;
    /** Bieżące zaznaczenie (w trakcie kadrowania) */
    private Rectangle currentCropSelection;
    /** Bieżące zaznaczenie (w trakcie klonowania) */
    private Rectangle currentCloneSelection;
    
    /**
     * Konstruktor
     * @param img Obraz do wyświetlenia
     */
    public Photo(ImageIcon img) {
        
        super(img);
        image = new ProcessedImage(img.getImage());
        setPreferredSize();
        setAutoscrolls(true); 
        mask = new Mask(image.getWidth(), image.getHeight());
        
    }    
  
    
    public ProcessedImage getImage() {
        return image;
    }
    

    public void setImage(Image image) {
        this.image = new ProcessedImage(image);
    }    
    
    
    /**
     * Tworzy obraz z fragmentów zdefiniowanych przez maskę
     * @return "Maskowany" obraz
     */
    public ProcessedImage getMaskImage() {
        
       if (mask.isEmpty() || !maskLayerVisible) return image;        
       
       BufferedImage maskImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
       Graphics2D g2 = (Graphics2D)maskImage.getGraphics();       
       
       for (Shape s: mask) {
    
         RectangularShape r = (RectangularShape)s; 
         g2.setClip(r);
         
         try {
           g2.drawImage(image.getSubimage((int)r.getMinX(), (int)r.getMinY(), (int)r.getWidth(), (int)r.getHeight()),
                 (int)r.getMinX(), (int)r.getMinY(), null);     
         }
         catch (Exception e) {
           // System.err.println(e);  
         }
 
         g2.setClip(null);

       }
        
       g2.dispose();
           
       return new ProcessedImage(maskImage);
        
    }
    
    
    /**
     * Nakłada przetworzone fragmenty na oryginalny obraz
     * @param maskImage Przetworzone fragmenty obrazu
     * @return Obraz z nałożonymi fragmentami
     */
    public BufferedImage applyMaskImage(BufferedImage maskImage) {
     
      if (mask.isEmpty() || !maskLayerVisible) return maskImage;  
        
      BufferedImage imgCopy = image.getCopy();
      Graphics2D g2 = (Graphics2D)imgCopy.getGraphics();        
      
      for (Shape s: mask) {
           
         RectangularShape r = (RectangularShape)s; 
         g2.setClip(r);
         
         // +1 i -2,  żeby pominąć krawędzie powstałe po przefiltrowaniu
         try {
           g2.drawImage(maskImage.getSubimage((int)r.getMinX()+1, (int)r.getMinY()+1, (int)r.getWidth()-2, (int)r.getHeight()-2),
                   (int)r.getMinX()+1, (int)r.getMinY()+1, null);   
         }
         catch (Exception e) {}
         
         g2.setClip(null); 

      }
      
      g2.dispose();            
      
      return imgCopy;
        
    }
    
    
    @Override
    public Dimension getPreferredScrollableViewportSize() {
       return getPreferredSize();
    }
    

    @Override
    public int getScrollableUnitIncrement(Rectangle rect, int orient, int dir) {
    
      int currPos = (orient == SwingConstants.HORIZONTAL) ?  rect.x : rect.y;

      if (dir < 0) {            
         int newPos = currPos - (currPos / maxUnitInc) * maxUnitInc;
         return (newPos == 0) ? maxUnitInc : newPos;           
      } 
        
      else  return ((currPos / maxUnitInc) + 1) * maxUnitInc - currPos;        
        
    }
    

    @Override
    public int getScrollableBlockIncrement(Rectangle rect, int orient, int dir) {
        
      return (orient == SwingConstants.HORIZONTAL)  ? rect.width - maxUnitInc : rect.height - maxUnitInc;
        
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    
    public void setMaxUnitInc(int maxUnitInc) {
        this.maxUnitInc = maxUnitInc;
    }    

    /**
     * Ustawienie nowego powiększenia obrazu
     * @param zoom Powiększenie w %
     */
    public void setZoom(double zoom) {
        
        this.zoom = zoom;        
        setPreferredSize();
        
        revalidate();
        repaint();
    }

    public double getZoom() {
        return zoom;
    }

    
    public void setCurrentCropSelection(Rectangle currentCropSelection) {
        this.currentCropSelection = currentCropSelection;
    }

    public Rectangle getCurrentCropSelection() {
        return currentCropSelection;
    }

    public Rectangle getCurrentCloneSelection() {
        return currentCloneSelection;
    }

    public void setCurrentCloneSelection(Rectangle currentCloneSelection) {
        this.currentCloneSelection = currentCloneSelection;
    }
    
    
    /**
     * Ustawienie rozmiaru komponentu 
     */
    private void setPreferredSize() {
        
      double scale = zoom/100.0d;  
      setPreferredSize(new Dimension((int)(image.getWidth()*scale), 
              (int)(image.getHeight()*scale)));        
        
    }

    
    public boolean isMaskLayerVisible() {
        return maskLayerVisible;
    }

    public void setMaskLayerVisible(boolean maskLayerVisible) {
        this.maskLayerVisible = maskLayerVisible;
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
    
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D)g;
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                          RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
      g.setColor(getBackground());
      g.fillRect(0, 0, getWidth(), getHeight());   
      
      // przeskalowanie do aktualnego powiększenia
      double scale = zoom/100.0d;
      AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
      at.scale(scale, scale);
      g2.drawRenderedImage(image, at);
      
      // nałożenie warstwy maski
      if (maskLayerVisible) {
          
        g2.setColor(new Color(200, 255, 255, maskLayerAlpha));
        g2.fillRect(0, 0, getWidth(), getHeight()); 
        
        // bieżące zaznaczenie
        if (mask.getCurrentSelection() != null) {
          g2.setColor(new Color(200, 0, 0, 200));
          g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
          g2.draw(mask.getCurrentSelection());
        }
        
        // wybrane obszary
        for (Shape shape: mask) {
            
          Shape r = Mask.getScaledShape(shape, zoom);
          g2.setColor(new Color(0, 0, 0, 150));
          g2.fill(r);
          g2.setColor(new Color(50, 50, 50, 150));
          g2.setStroke(new BasicStroke());
          g2.draw(r);          
            
        }
        
      }         
      
      // zaznaczenie do kadrowania
      else if (currentCropSelection != null && !currentCropSelection.isEmpty()) {
          
         g2.setColor(new Color(200, 0, 0, 200));
         g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
         g2.draw(currentCropSelection); 
          
      }
      
      // zaznaczenie do klonowania
      else if (currentCloneSelection != null && !currentCloneSelection.isEmpty()) {
          
         g2.setColor(new Color(0, 0x33, 0x66, 200));
         g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
         g2.draw(currentCloneSelection); 
          
      }      
        
    }    
    

    public Mask getMask() {
        return mask;
    }
    
    
    
    
}
