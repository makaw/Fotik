/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.photoview;

import gui.GUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.SwingConstants;

    
/**
 * Podziałka przewijanego obszaru obrazka
 * Na podstawie:
 * https://docs.oracle.com/javase/tutorial/uiswing/components/scrollpane.html   
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
 public class PhotoRuler extends JComponent {
   
   /** Długość linii podziałki (px) */
   public static final int tickSize = 35;
   /** Co ile pikseli linia podziałki */
   public static final int pxMul = 50;
   /** Orientacja pozioma/pionowa */
   public final int orient;
   /** True jeżeli jednostka to cm, false jeżeli px */
   public boolean cm;
   /** Odstęp pomiędzy liniami podziałki */
   private double increment;
   /** Wielkość jednostki podziałki */
   private double units;
   /** Bieżące powiększenie obrazu */
   private final double zoom;
   /** Kolor tła podziałki */
   private final Color bgColor;

   /**
    * Konstruktor
    * @param zoom Powiększenie obrazu w %
    * @param orient Orientacja pozioma/pionowa
    * @param cm True jeżeli początkowa jednostka to cm, false jeżeli px
    * @param bgColor Kolor tła
    */
   public PhotoRuler(double zoom, int orient, boolean cm, Color bgColor) {
       
     this.zoom = zoom;  
     this.orient = orient;
     this.cm = cm;
     
     this.bgColor = bgColor;
     setIncrementAndUnits();
     
   }
   
   /**
    * Ustalenie jednostki podziałki
    * @param cm True jeżeli cm, false jeżeli px
    */
   public void setCm(boolean cm) {
       
     this.cm = cm;
     setIncrementAndUnits();
     repaint();
     
   }
   
   /**
    * Ustalenie jednostek
    */
   private void setIncrementAndUnits() {
       
     if (cm) {
        units = (int)(PhotoPanel.cmUnit * (zoom/100.0d));
        increment = units;
     } else {
        units = (int)(pxMul * (zoom/100.0d));
        increment = units / 2;
     }
     
     
   }

   public boolean isCm() {
     return this.cm;
   }

   public int getIncrement() {
     return (int)increment;
   }

   public void setPreferredHeight(int ph) {
     setPreferredSize(new Dimension(tickSize, ph));
   }

   public void setPreferredWidth(int pw) {
     setPreferredSize(new Dimension(pw, tickSize));
   }

   @Override
   protected void paintComponent(Graphics g) {
   
     Rectangle drawHere = g.getClipBounds();

     g.setColor(bgColor);
     g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);
     g.setFont(GUI.smallFont);
     g.setColor(Color.black);

     int end, start, tickLength;
     String text;
     
     double inc = increment/zoom*100.0d;
     
     if (orient == SwingConstants.HORIZONTAL) {
        start = (int)((drawHere.x / inc) * inc);
        end = (int)((((drawHere.x + drawHere.width) / inc) + 1) * inc);
     } else {
        start = (int)((drawHere.y / inc) * inc);
        end = (int)((((drawHere.y + drawHere.height) / inc) + 1) * inc);
     }
     
    
     if (start == 0) {
        text = Integer.toString(0) + (cm ? " cm" : " px");
        tickLength = 10;
        if (orient == SwingConstants.HORIZONTAL) {
          g.drawLine(0, tickSize-1, 0, tickSize-tickLength-1);
          g.drawString(text, 2, 21);
        } else {
          g.drawLine(tickSize-1, 0, tickSize-tickLength-1, 0);
          g.drawString(text, 5, 13);
        }
        start = (int)inc;
     }          
     
     int j = 1;
     for (int i = start; i < end; i += inc) {
     
       if (j % 4 == 0)  {
          tickLength = 10;
          text = Integer.toString((int)((isCm() ? i : i*pxMul)/units));
       } else {
          tickLength = 6;
          text = null;
       }

       if (tickLength != 0) {
          if (orient == SwingConstants.HORIZONTAL) {
             g.drawLine(i, tickSize-1, i, tickSize-tickLength-1);
             if (text != null) g.drawString(text, i-3, 21);
          } else {
             g.drawLine(tickSize-1, i, tickSize-tickLength-1, i);
             if (text != null) g.drawString(text, 5, i+9);
          }
       }
       
       j++;
       
     }
   }
   
}
 
   
   