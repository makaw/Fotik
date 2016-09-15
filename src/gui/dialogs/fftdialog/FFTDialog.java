/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs.fftdialog;


import gui.dialogs.fftdialog.menu.FFTMenuTransform;
import gui.dialogs.fftdialog.menu.FFTMenuEdit;
import gui.GUI;
import gui.SimpleDialog;
import gui.loader.IProgress;
import gui.photoview.PhotoPanel;
import processing.ProcessedImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import fotik.IConf;
import gui.BottomToolBar;
import gui.dialogs.PreviewDialog;
import gui.loader.IProgressInvoker;
import gui.photoview.IPhotoFrame;
import gui.photoview.MouseMode;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import processing.imagefft.ColorImageFFT;
import processing.imagefft.GrayImageFFT;
import processing.imagefft.ImageFFT;

/**
 *
 * Okienko dialogowe z wizualizacją widma obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class FFTDialog extends SimpleDialog  implements IProgressInvoker, IPhotoFrame {

   /** Kontener obrazu FFT */
   private final PhotoPanel photoPanel;
   /** Obraz źródłowy */
   private final ProcessedImage image;
   /** Obraz FFT */
   private final BufferedImage fftImg;
   /** FFT (dane) */
   private final ImageFFT fft;
   /** Interfejs wskaźnika postępu */
   private final IProgress progress;
   /** True jeżeli wywołany został podgląd, false jeżeli zastosowanie IFFT */
   private boolean loaderPreview;      
   /** Dolny pasek narzędziowy */
   private BottomToolBar toolBar;
   /** Menu "Edycja" */
   private FFTMenuEdit menuEdit;
   /** True jeżeli FFT w kolorze */
   private final boolean inColor;
   
   
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej i wyświetlenie okienka
    * @param frame Referencja do GUI
    * @param progress Interfejs okna z paskiem postępu
    * @param inColor True jeżeli obraz kolorowy
    */ 
   public FFTDialog(GUI frame, IProgress progress, boolean inColor) {
        
     super(frame, IConf.APP_NAME + " - 2D FFT widmo amplitudowe " + 
             (inColor ? "(kolor)" : "(odcienie szaro\u015bci)"));

     this.progress = progress;
     this.image = frame.getPhotoPanel().getPhoto().getImage();
     this.inColor = inColor;
     
     photoPanel = new PhotoPanel(this, new Color(0xe9ebf2));
     photoPanel.getCmButton().setVisible(false);     
     
     fft = inColor ? new ColorImageFFT(image.getCopy(), progress) : new GrayImageFFT(image.getGrayCopy(), progress);      
     fftImg = fft.processImage() ? fft.getSpectrumImage() : null;                            
     
     if (fftImg != null) super.showDialog(640, 600); 
            
   }  
   
   
   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected void getContent()  {              
       
      JMenuBar menuBar = new JMenuBar();  
      menuBar.add(new FFTMenuTransform(this)); 
      menuEdit = new FFTMenuEdit(this);
      menuBar.add(menuEdit);
      setJMenuBar(menuBar);      
       
      JPanel p = new JPanel();
      p.setOpaque(false);
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      
      JPanel p0 = new JPanel(new GridLayout(1,1));
      p0.setOpaque(false);
      photoPanel.setBackground(Color.WHITE);
      photoPanel.setPreferredSize(new Dimension(620, 510)); 
      
      photoPanel.setInitImage(fftImg);   
      
      p0.add(photoPanel);
      p.add(p0);
      
      toolBar = new BottomToolBar(this);
      toolBar.addElement(20);
      p.add(toolBar);
      toolBar.getMaskPanel().setMaskLabelText("Maska");
      
      add(p);
            
      if (progress!=null)  progress.hideComponent();            
     
      
   }      

   
   public void setLoaderPreview(boolean loaderPreview) {
      this.loaderPreview = loaderPreview;
   }
   
   
   @Override
   public void setMaskVisible(boolean visible) {
       
      toolBar.getMaskPanel().setMaskVisible(visible);
       
   }
   
   /**
    * Operacja "Cofnij"
    */
   public void undo() {
          
     photoPanel.photoUndo();     
     fft.getFftOp().undo();
     
   }
   
   /**
    * Operacja "Ponów"
    */
   public void redo() {
       
     photoPanel.photoRedo();
     fft.getFftOp().redo();
     
   }      
   

   @Override
   public void start(IProgress progress) {
        
     fft.setProgress(progress);
      
     if (fft.inverseProcess()) {
          
       progress.hideComponent(); 
        
       if (loaderPreview)  
         new PreviewDialog(frame, IConf.APP_NAME + " - FFT - podgl\u0105d wyniku transformacji odwrotnej",
                fft.getInverseImage());
        
       else {
         frame.getPhotoPanel().changeImage(fft.getInverseImage());
         dispose();
       }
        
        
     }
     else progress.hideComponent();
     
   }

   
   @Override
   public PhotoPanel getPhotoPanel() {
       return photoPanel;
   }         

   public ImageFFT getFft() {
      return fft;
   }

   

   @Override
   public GUI getMainFrame() {
      return frame;
   }

  
   public boolean isInColor() {
      return inColor;
   }         
   
   
  /**
   * Włączenie wyłączenie opcji "Cofnij" w menu
   * @param enabled True jeżeli włączone
   */
  @Override
  public void enableUndoMenuItem(boolean enabled) {
      
    menuEdit.enableUndo(enabled);
    if (enabled && !getTitle().endsWith("*")) setTitle(getTitle() + " *");
    if (!enabled && getTitle().endsWith("*")) setTitle(getTitle().substring(0, getTitle().length()-2));
      
  }
      
  
  /**
   * Włączenie wyłączenie opcji "Ponów" w menu
   * @param enabled True jeżeli włączone
   */
  @Override
  public void enableRedoMenuItem(boolean enabled) {
      
    menuEdit.enableRedo(enabled);
      
  }   

  /**
   * Ustawienie wartości suwaka powiększenia obrazu
   * @param value Powiększenie obrazu w %
   */
  @Override
  public void setZoomSliderValue(int value) {
      
    if (toolBar != null) toolBar.getZoomSlider().setValue(value);
      
  }

  @Override
  public void enableImageMenuItems(boolean enabled) {}

  @Override
  public int getLocalBlurSize() {
     return -1;
  }

  @Override
  public int getLocalSmudgeSize() {
     return -1;
  }
  
  @Override
  public int getMaxLocalSmudgeSize() {
     return -1;
  }  
  
  @Override
  public void setCloneThumb(ImageIcon thumb) {}
   

  
   
}
