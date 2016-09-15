/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01-02/2016
 */
package gui.photoview;



import fotik.IConf;
import gui.dialogs.ConfirmDialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;


/**
 * Obsługa myszy podczas edycji obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class MouseHandler extends MouseAdapter {

   /** Interfejs okna z wyświetlanym obrazem */
   private final IPhotoFrame photoFrame;
   /** Startowy punkt zaznaczenia */
   private Point start = new Point();
   /** Czy obsługa maski jest włączona i widoczna */
   private boolean maskEnabled = false;
   /** Wybrane narzędzie */
   private MouseMode mode;
   /** Punkt końcowy dla lokalnego odszumiania i smużenia */
   private Point tmpEnd;
   /** Bufor danych dla smużenia */
   //double[][][] smudgeBuffer; 
   private final Tools tools;
   
   
   /**
    * Konstruktor
    * @param photoFrame Interfejs okna z wyświetlanym obrazem
    */
   public MouseHandler(IPhotoFrame photoFrame) {
     
     this.photoFrame = photoFrame;
     tools = new Tools(photoFrame);
     
   }

   
   
   public void setMode(MouseMode mode) {
      this.mode = mode;
   }

   
   public void setMaskEnabled(boolean maskEnabled) {
      this.maskEnabled = maskEnabled;
   }      
   
   
   @Override
   public void mouseMoved(MouseEvent me) {
     
      if ((!maskEnabled || !mode.isAddMaskMode())
              && mode!=MouseMode.CROP && mode!=MouseMode.CLONE && mode!=MouseMode.BLUR && mode!=MouseMode.SMUDGE) return;  
       
      start = me.getPoint();
      
   }

   
   @Override
   public void mouseDragged(MouseEvent me) {

      if ((!maskEnabled || !mode.isAddMaskMode())
              && mode!=MouseMode.CROP && mode!=MouseMode.CLONE && mode!=MouseMode.BLUR && mode!=MouseMode.SMUDGE) return;  
       
      Point end = me.getPoint();
      Photo photo = photoFrame.getPhotoPanel().getPhoto();  
      
      // zaznaczenia na warstwie maski
      if (mode==MouseMode.ADD_RECTANGLE)
        photo.getMask().setCurrentSelection(new Rectangle(start, new Dimension(end.x-start.x, end.y-start.y)));
      
      else if (mode==MouseMode.ADD_ELLIPSE)
        photo.getMask().setCurrentSelection(new Ellipse2D.Double(start.x, start.y, end.x-start.x, end.y-start.y));
      
      // lokalne odszumianie
      else if (mode==MouseMode.BLUR) tmpEnd = new Point(end.x, end.y);
                
      // lokalne smużenie
      else if (mode==MouseMode.SMUDGE) {
                    
         tmpEnd = new Point(end.x, end.y);        
         Point r = Tools.getScaledPoint(new Point(tmpEnd.x, tmpEnd.y), 10000.0/photo.getZoom());  
         tools.initSmudge(r.x, r.y);         
         
      }
      
      // zaznaczenie do kadrowania
      else if (mode==MouseMode.CROP) 
        photo.setCurrentCropSelection(new Rectangle(start, new Dimension(end.x-start.x, end.y-start.y)));
      
      // zaznaczenie do klonowania
      else if (mode==MouseMode.CLONE) {
        if (end.x-start.x>IConf.MAX_CLONE_THUMB_SIZE) end.x = start.x + IConf.MAX_CLONE_THUMB_SIZE;
        if (end.y-start.y>IConf.MAX_CLONE_THUMB_SIZE) end.y = start.y + IConf.MAX_CLONE_THUMB_SIZE;
        photo.setCurrentCloneSelection(new Rectangle(start, new Dimension(end.x-start.x, end.y-start.y)));
      }
      
      photo.revalidate();
      photo.repaint();
      
   }
   
   
   @Override
   public void mouseReleased(MouseEvent me) {
       
     if ((!maskEnabled || !mode.isAddMaskMode())
             && mode!=MouseMode.CROP && mode!=MouseMode.CLONE && mode!=MouseMode.BLUR && mode!=MouseMode.SMUDGE) return;  
     
     Photo photo = photoFrame.getPhotoPanel().getPhoto();  
     
     // wykadrowanie obrazu
     if (mode==MouseMode.CROP && photo.getCurrentCropSelection()!=null && !photo.getCurrentCropSelection().isEmpty()) {
       
        boolean res = new ConfirmDialog(photoFrame.getMainFrame(), "Czy na pewno wykadrowa\u0107 obraz ?").isConfirmed();
        if (res) {            
          Rectangle r = (Rectangle) Mask.getScaledShape(photo.getCurrentCropSelection(), 10000.0/photo.getZoom()); 
          int w = r.width;  int h = r.height;
          if (r.x+w >= photo.getImage().getWidth()) w = photo.getImage().getWidth() - r.x - 1;
          if (r.y+h >= photo.getImage().getHeight()) h = photo.getImage().getHeight() - r.y - 1;
          photoFrame.getPhotoPanel().changeImage(photo.getImage().getSubimage(r.x, r.y, w, h));  
          photoFrame.getMainFrame().setDefaultToolSelected();
        }
        
        photo.setCurrentCropSelection(null);
         
     }   
     
     // lokalne odszumianie 
     else if (mode==MouseMode.BLUR && tmpEnd != null) {
         
       Point r1 = Tools.getScaledPoint(new Point(start.x, start.y), 10000.0/photo.getZoom());
       Point r2 = Tools.getScaledPoint(new Point(tmpEnd.x, tmpEnd.y), 10000.0/photo.getZoom());
       photoFrame.getPhotoPanel().changeImage(tools.blur(r1.x, r1.y, r2.x, r2.y), false);  
       tmpEnd = null;
         
     }
     
     // smużenie
     else if (mode==MouseMode.SMUDGE && tmpEnd != null) {
         
       Point r1 = Tools.getScaledPoint(new Point(start.x, start.y), 10000.0/photo.getZoom());
       Point r2 = Tools.getScaledPoint(new Point(tmpEnd.x, tmpEnd.y), 10000.0/photo.getZoom());       
       photoFrame.getPhotoPanel().changeImage(tools.smudge(r1.x, r1.y, r2.x, r2.y), false);                    
       tmpEnd = null;         
         
     }
     
     // klonowanie
     else if (mode==MouseMode.CLONE && photo.getCurrentCloneSelection()!=null
             && !photo.getCurrentCloneSelection().isEmpty()) {
         
       Rectangle r = (Rectangle) Mask.getScaledShape(photo.getCurrentCloneSelection(), 10000.0/photo.getZoom());    
       tools.setCloneImg(r.x, r.y, r.width, r.height);
       photo.setCurrentCloneSelection(null);
       
     }
     
     // dodanie obszaru na warstwie maski
     else
       photo.getMask().addCurrentSelection(photo.getZoom());
     
     photo.revalidate();
     photo.repaint();
      
   }
   
   
   @Override
   public void mousePressed(MouseEvent me) {

     //System.err.println(mode + " / " + this.toString());
       
     if ((!maskEnabled || mode!=MouseMode.REMOVE) && mode!=MouseMode.CLONE_PUT) return;  
     
     Photo photo = photoFrame.getPhotoPanel().getPhoto();  
     
     // usunięcie zaznaczenia z warstwy maski
     if (mode==MouseMode.REMOVE)
       photo.getMask().removeSelection(me.getX(), me.getY(), photo.getZoom());
     
     // klonowanie - stemplowanie :)
     else if (mode==MouseMode.CLONE_PUT) {
         
       Rectangle r = (Rectangle)Mask.getScaledShape(new Rectangle(me.getX(), me.getY(), 1, 1), 10000.0/photo.getZoom());
       photoFrame.getPhotoPanel().changeImage(tools.cloneStamp(r.x, r.y), false);
         
     }
         
         
     photo.revalidate();
     photo.repaint();
      
   }   

   
    
}
