/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.photoview;

import gui.GUI;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.ImgFileDialog;
import gui.dialogs.InfoDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;


/**
 * "Statyczna" klasa obsługująca operacje I/O na obrazach
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class PhotoIO {
    
    
   private PhotoIO() {}
   
   
   /**
    * Wczytanie obrazu z pliku do obiektu
    * @param fileName Nazwa pliku
    * @return Obiekt obrazu
    * @throws java.io.IOException Błąd I/O podczas wczytywania pliku
    */
   static BufferedImage loadImageFromFile(GUI frame, String fileName) {
           
     try {
      // ImageIO.setUseCache(false);   
       return ImageIO.read(new File(fileName));
     }
     catch (Exception ex) {
       String err = (ex.getMessage() != null && !ex.getMessage().isEmpty()) ? ":\n" + ex.getMessage() : ".";  
       InfoDialog.errorDialog(frame, "Podczas pr\u00f3by odczytu pliku wyst\u0105pi\u0142 b\u0142\u0105d"+err);
       return null;
     }
     
   }
       
   
   /**
    * Obsługa wczytania obrazu z pliku
    * @param frame Ref. do GUI
    */
   public static void loadImageFromFile(GUI frame) {
       
     
     String fName = frame.getPhotoPanel().getFileName();        
     ImgFileDialog fd = new ImgFileDialog(frame, JFileChooser.OPEN_DIALOG, 
          fName.isEmpty() ? null : new File(fName).getParentFile());
     fd.showDialog();
        
     if (fd.getSelectedFile() != null) 
         frame.loadImageFile(fd.getSelectedFile().getPath());         
       
   }
   
   
   /**
    * Zapisanie obrazu do pliku
    * @param frame Ref. do GUI
    * @param fileName Nazwa pliku
    */
   private static void saveImage(IPhotoFrame frame, String fileName) {
       
      try { 
        File output = new File(fileName);
       
        String format;
        if (output.getName().toLowerCase().endsWith(".png")) format = "png";
        else if (output.getName().toLowerCase().endsWith(".jpg") || 
               output.getName().toLowerCase().endsWith(".jpeg")) format = "jpg";
        else throw new Exception("Nieprawid\u0142owe rozszerzenie pliku.");
        
        BufferedImage imgCopy = frame.getPhotoPanel().getPhoto().getImage().getCopy();
            
        //ImageIO.setUseCache(false);
        ImageIO.write(imgCopy, format, output);
         
        if (frame.getMainFrame().equals(frame))
          frame.getMainFrame().loadImageFile(output.getPath());
        
        InfoDialog.infoDialog(frame.getMainFrame(), "Obraz zapisany poprawnie.");
         
      }
            
      catch (Exception ex) {
       
         String err = (ex.getMessage() != null && !ex.getMessage().isEmpty()) ? ":\n" + ex.getMessage() : ".";  
         InfoDialog.errorDialog(frame.getMainFrame(), 
                 "Podczas pr\u00f3by zapisu pliku wyst\u0105pi\u0142 b\u0142\u0105d"+err);
            
      } 
       
          
   }
   

   /**
    * Zapisanie obrazu do tego samego pliku
    * @param frame Ref. do GUI
    */   
   public static void saveImageToFile(IPhotoFrame frame) {
       
      saveImage(frame, frame.getPhotoPanel().getFileName()); 
       
   }
         
   
   /**
    * Zapisanie obrazu do wybranego pliku
    * @param frame Ref. do GUI
    */   
   public static void saveImageAsFile(IPhotoFrame frame) {
       
       String fName = frame.getPhotoPanel().getFileName();        
       ImgFileDialog fd = new ImgFileDialog(frame.getMainFrame(), JFileChooser.SAVE_DIALOG, 
             new File(fName).getParentFile(), new File(fName));
       fd.showDialog();
       
       if (fd.getSelectedFile() != null) {
           
         boolean res =  (!fd.getSelectedFile().exists()) ? true : 
             new ConfirmDialog(frame.getMainFrame(), "Czy na pewno nadpisa\u0107 plik:\n" 
                   + fd.getSelectedFile().getName() + " ?").isConfirmed();
         
         if (res) saveImage(frame, fd.getSelectedFile().getPath());
         
       }
              
   }
   
    
    
}
