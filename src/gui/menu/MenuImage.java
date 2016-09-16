/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.menu;


import gui.GUI;
import gui.dialogs.BrightnessContrastDialog;
import gui.dialogs.fftdialog.FFTDialog;
import gui.dialogs.GammaCorrectionDialog;
import gui.dialogs.HSBBalanceDialog;
import gui.dialogs.HistogramDialog;
import gui.dialogs.ResizeDialog;
import gui.dialogs.RotateDialog;
import gui.loader.IProgress;
import gui.loader.IProgressInvoker;
import gui.loader.Loader;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;



/**
 *
 * Menu "Obraz" 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class MenuImage extends JMenu implements IProgressInvoker {
    
   /** Elementy menu */ 
   private final JMenuItem histogramItem, grayFourierItem, colorFourierItem, scaleItem, rotateItem,
                        gammaItem, brightItem, HSBItem, whiteItem, grayItem;
   /** Wybrana pozycja menu */
   private JMenuItem selectedItem;
   /** Submenu - odbicie, FFT, filtry */
   private final JMenu flipMenu, fourierMenu, filtersMenu;
   /** Ref. do GUI */
   private final GUI frame;
   
   
   /**
    * Konstruktor 
    * @param frame Referencja do GUI 
    */    
   public MenuImage(GUI frame) {
       
    super("Obraz ");
    setMnemonic(KeyEvent.VK_O);
    
    this.frame = frame;
    
    histogramItem = new JMenuItem("Histogram");
    histogramItem.setPreferredSize(new Dimension(160, 20));
    histogramItem.setMnemonic(KeyEvent.VK_H);
    histogramItem.setEnabled(false);
    add(histogramItem);
    histogramItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
         
        new HistogramDialog(MenuImage.this.frame);
          
      }
    });  
    
    
    filtersMenu = new MenuFilters(frame);
    filtersMenu.setMnemonic(KeyEvent.VK_F);
    filtersMenu.setEnabled(false);
    add(filtersMenu);    

    fourierMenu = new JMenu("FFT 2D");
    fourierMenu.setMnemonic(KeyEvent.VK_T);
    fourierMenu.setEnabled(false);
    fourierMenu.setPreferredSize(new Dimension(160, 20));
    add(fourierMenu);    

    grayFourierItem = new JMenuItem("W skali szaro\u015bci");
    grayFourierItem.setPreferredSize(new Dimension(160, 20));
    grayFourierItem.setMnemonic(KeyEvent.VK_S);
    fourierMenu.add(grayFourierItem);
    grayFourierItem.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(final ActionEvent e) {
    
        selectedItem = grayFourierItem;
        new Loader(MenuImage.this.frame, MenuImage.this, true).load();    
        
     }
    });    
    
    colorFourierItem = new JMenuItem("W kolorze");
    colorFourierItem.setPreferredSize(new Dimension(160, 20));
    colorFourierItem.setMnemonic(KeyEvent.VK_K);
    fourierMenu.add(colorFourierItem);
    colorFourierItem.addActionListener(new ActionListener() {
     @Override
     public void actionPerformed(final ActionEvent e) {
    
        selectedItem = colorFourierItem;
        new Loader(MenuImage.this.frame, MenuImage.this, true).load();        
        
     }
    });        
        
    
    add(new JSeparator());
        
    brightItem = new JMenuItem("Jasno\u015b\u0107/kontrast");
    brightItem.setMnemonic(KeyEvent.VK_J);
    brightItem.setEnabled(false);
    brightItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
         
        new BrightnessContrastDialog(MenuImage.this.frame);
          
      } 
        
    });
    add(brightItem);    
    
    gammaItem = new JMenuItem("Korekcja gamma");
    gammaItem.setMnemonic(KeyEvent.VK_G);
    gammaItem.setEnabled(false);
    gammaItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
         
        new GammaCorrectionDialog(MenuImage.this.frame);
          
      } 
        
    });
    
    add(gammaItem);    
        
    
    whiteItem = new JMenuItem("Autom. balans bieli");
    whiteItem.setMnemonic(KeyEvent.VK_B);
    whiteItem.setEnabled(false);
    whiteItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
         
        selectedItem = whiteItem;
        new Loader(MenuImage.this.frame, MenuImage.this, true).load();           
       
      } 
        
    });
    
    add(whiteItem);    
    
    
    HSBItem = new JMenuItem("Balans HSB");
    HSBItem.setMnemonic(KeyEvent.VK_A);
    HSBItem.setEnabled(false);
    HSBItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
         
        new HSBBalanceDialog(MenuImage.this.frame);
          
      } 
        
    });
    add(HSBItem);    
    
    grayItem = new JMenuItem("Konw. do skali szaro\u015bci");
    grayItem.setMnemonic(KeyEvent.VK_S);
    grayItem.setEnabled(false);
    grayItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
         
         MenuImage.this.frame.getPhotoPanel().changeImage(
           MenuImage.this.frame.getPhotoPanel().getPhoto().getImage().getGrayCopy(), false);    
          
      } 
        
    });
    
    add(grayItem);    
    
    
    add(new JSeparator());    
    
    rotateItem = new JMenuItem("Rotacja");
    rotateItem.setMnemonic(KeyEvent.VK_R);
    rotateItem.setEnabled(false);
    rotateItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
         
        new RotateDialog(MenuImage.this.frame);
          
      } 
        
    });
    
    add(rotateItem);
    
    flipMenu = new JMenu("Odbicie");
    flipMenu.setMnemonic(KeyEvent.VK_D);
    flipMenu.setEnabled(false);
    
    JMenuItem flipItem = new JMenuItem("w poziomie");
    flipItem.setMnemonic(KeyEvent.VK_Z);
    flipItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
         
           MenuImage.this.frame.getPhotoPanel().changeImage(
                 MenuImage.this.frame.getPhotoPanel().getPhoto().getImage().getFlippedCopy(SwingConstants.HORIZONTAL));
           
        }
    });
    flipMenu.add(flipItem);
    
    flipItem = new JMenuItem("w pionie");
    flipItem.setMnemonic(KeyEvent.VK_N);
    flipItem.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
         
           MenuImage.this.frame.getPhotoPanel().changeImage(
                 MenuImage.this.frame.getPhotoPanel().getPhoto().getImage().getFlippedCopy(SwingConstants.VERTICAL));
           
        }
    });
    flipMenu.add(flipItem);    
    
    add(flipMenu);
    
    scaleItem = new JMenuItem("Skalowanie");
    scaleItem.setMnemonic(KeyEvent.VK_L);
    scaleItem.setEnabled(false);
    add(scaleItem);
    scaleItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
       
          new ResizeDialog(MenuImage.this.frame);    
          
      }
    });                 

    
   } 
   
   
   @Override
   public void setEnabled(boolean enabled) {
       
     histogramItem.setEnabled(enabled);
     filtersMenu.setEnabled(enabled);
     fourierMenu.setEnabled(enabled);
     rotateItem.setEnabled(enabled);
     flipMenu.setEnabled(enabled);
     scaleItem.setEnabled(enabled);
     gammaItem.setEnabled(enabled);     
     brightItem.setEnabled(enabled);
     HSBItem.setEnabled(enabled);
     whiteItem.setEnabled(enabled);
     grayItem.setEnabled(enabled);
       
   }
   
   
   @Override
   public void start(IProgress progress) {
      
     if (selectedItem == grayFourierItem)  
       new FFTDialog(frame, progress, false);        
       
     else if (selectedItem == colorFourierItem)  
       new FFTDialog(frame, progress, true); 
          
     else if (selectedItem == whiteItem) {
       MenuImage.this.frame.getPhotoPanel().changeImage(
           MenuImage.this.frame.getPhotoPanel().getPhoto().getImage().getWhiteAdjustedCopy(progress), false);     
       setCursor(null);
     }
    
     
   }
    
    
}
