/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;

import gui.GUI;
import gui.ImageRes;
import gui.SimpleDialog;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 *
 * Okienko wyboru pliku (z podglądem obrazków)
 * 
 * @author Maciej Kawecki
 * @version 1.0 
 * 
 */
public class ImgFileDialog extends SimpleDialog {
    
  /** Komponent wyboru pliku */  
  private JFileChooser chooser;  
  /** Typ okienka (wg JFileChooser) */
  private final int dialogType;
  /** Dopuszczalne rozszerzenia plików */
  private static final String[] extensions = {"jpg", "jpeg", "png"};
  /** Wybrany plik */
  private File selectedFile;
  /** Początkowy katalog */
  private final File directory;
  
  /**
   * Konstruktor
   * @param frame Ref. do GUI
   * @param dialogType Typ okienka
   * @param directory Początkowy katalog
   * @param selectedFile Początkowo wybrany plik
   */
  public ImgFileDialog(GUI frame, int dialogType, File directory, File selectedFile) {
        
    super(frame, dialogType == JFileChooser.OPEN_DIALOG ? "Otwórz plik" : "Zapisz plik");
    this.dialogType = dialogType;
    this.directory = directory;
    this.selectedFile = selectedFile;
      
  }
  
/**
   * Konstruktor
   * @param frame Ref. do GUI
   * @param dialogType Typ okienka
   * @param directory Początkowy katalog
   */
  public ImgFileDialog(GUI frame, int dialogType, File directory) {
      
    this(frame, dialogType, directory, null);  
      
  }  
  

  @Override
  protected void getContent() {
        
    chooser = new JFileChooser();
    chooser.setDialogType(dialogType);
    chooser.setAcceptAllFileFilterUsed(false); 
    chooser.addChoosableFileFilter(new FileNameExtensionFilter("Obrazy (JPEG, PNG)", extensions));
    chooser.setAcceptAllFileFilterUsed(true); 
    if (directory != null) chooser.setCurrentDirectory(directory);
    if (selectedFile != null) chooser.setSelectedFile(selectedFile);
    
    ImgPreview preview = new ImgPreview();
    chooser.setAccessory(preview);
    chooser.addPropertyChangeListener(preview);
    chooser.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent action) {

          if (action.getActionCommand().equals("ApproveSelection")) 
            selectedFile = chooser.getSelectedFile();
          else
            selectedFile = null;
          
          dispose();
          
        }
        
    });
        
    add(chooser);
    revalidate();
    repaint();
        
  }
    
  /**
   * Wywołanie okienka
   */  
  public void showDialog() {
        
    super.showDialog(600, 400);
        
  }

  
  public File getSelectedFile() {
      return selectedFile;
  }

        
    
  /**
   * Wewn. klasa: panel z podgladem wybranego obrazka
   */
  private class ImgPreview extends JPanel implements PropertyChangeListener {
  
    /** Wymiary miniatury obrazka */  
    private int width, height;
    /** Miniatura obrazka */
    private Image image;
    /** Maksymalna szerokość/wysokość miniatury */
    private static final int maxSize = 180;
    /** Kolor tła */
    private final Color bgColor;
    
      
    ImgPreview() {
      super();
      setPreferredSize(new Dimension(maxSize, -1));
      bgColor = getBackground();
    }
    
    
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        
      image = null;  
        
      String propertyName = e.getPropertyName();        
      if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            
        File selection = (File)e.getNewValue();            
        if (selection != null) {
            
          String name = selection.getAbsolutePath();            
          if (name != null) {
            
            boolean test = false;
            for (String ext: extensions) if (name.toLowerCase().endsWith(ext)) {  test = true; break; }
            if (test) {
    
              image = new ImageIcon(name).getImage();
              width = image.getWidth(this);
              height = image.getHeight(this);
      
              if (width >= height) {
                double ratio = (double)(maxSize) / width;
                width = maxSize;
                height = (int)(height * ratio);
              }
              else {
                double ratio = (double)getHeight() / height;
                height = getHeight();
                width = (int)(width * ratio);
              }
                   
              image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
             
            }
          }
        }
      }
             
      revalidate();
      repaint();
      
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        
      g.setColor(bgColor);   
      g.fillRect(0, 0, maxSize, getHeight()); 
      if (image == null) {
         image = ImageRes.getImage("no_image2.jpg");
         width = image.getWidth(this);
         height = image.getHeight(this);
      }
      g.drawImage(image, getWidth()/2 - width/2 + 5, getHeight()/2 - height/2, this);
        
    }
    
  }
  
  
    
    
}

