/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui;

import processing.Histogram;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;


/**
 *
 * Wykres histogramowy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class HistogramChart extends JPanel {

   /** Szerokość pojedynczego słupka histogramu */
   protected static final float MIN_BAR_WIDTH = 2.0f;
   /** Histogram (dane) */
   private Histogram histogram;
   /** Kolor słupków */
   private Color barColor;
   

   /**
    * Konstruktor
    * @param histogram Histogram (dane)
    * @param barColor Kolor słupków
    */
   public HistogramChart(Histogram histogram, Color barColor) {
       
      super(); 
      this.histogram = histogram;
      this.barColor = barColor;
      setPreferredSize(new Dimension((int)(255.0f * MIN_BAR_WIDTH) + 11, 150));
      setMaximumSize(getPreferredSize());
      
   }

   /**
    * Dynamiczna zmiana histogramu
    * @param histogram Nowy histogram (dane)
    */
   public void setHistogram(Histogram histogram) {
      this.histogram = histogram;
      revalidate();
      repaint();
   }      


   public void setBarColor(Color barColor) {
      this.barColor = barColor;
   }


   @Override
   protected void paintComponent(Graphics g) {
       
      super.paintComponent(g);
      
      if (histogram == null) return;
      
      int xOffset = 5;
      int yOffset = 5;
      int width = getWidth() - 1 - (xOffset * 2);
      int height = getHeight() - 1 - (yOffset * 2);
      Graphics2D g2d = (Graphics2D) g.create();
                
      g2d.setColor(Color.WHITE);
      g2d.fillRect(xOffset, yOffset, width, height);
      g2d.setColor(Color.DARK_GRAY);
      g2d.drawRect(xOffset, yOffset, width, height);
                
      int barWidth = (int)Math.max(MIN_BAR_WIDTH,  Math.floor((float) width/255.0f));
      float maxValue = (float) histogram.getMaxValue();
                
      int pos = xOffset;
      for (int i=0;i<255;i++)  {
        
        int barHeight = Math.round(((float)histogram.getValue(i) / maxValue) * height);
        g2d.setColor(new Color(i, i, i));
        Rectangle2D bar = new Rectangle2D.Float(pos, height + yOffset - barHeight, barWidth, barHeight);
        g2d.fill(bar);
        g2d.setColor(barColor);
        g2d.draw(bar);
        pos += barWidth;   
          
      }      
      
      g2d.dispose();
        
   }
      
   
}

