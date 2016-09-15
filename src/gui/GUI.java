/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui;
 

import gui.menu.MenuEdit;
import gui.photoview.PhotoPanel;
import gui.menu.MenuHelp;
import gui.menu.MenuFile;
import gui.menu.MenuImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import fotik.Fotik;
import fotik.IConf;
import gui.photoview.IPhotoFrame;
import gui.photoview.MouseMode;
import java.awt.GridLayout;
import javax.swing.ImageIcon;


/**
 *
 * Graficzny interfejs użytkownika (główne okno aplikacji)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GUI extends JFrame implements IPhotoFrame {
    
  /** Szerokość okna aplikacji w pikselach */
  protected static final int fWidth = 800;
  /** Wysokość okna aplikacji w pikselach */
  protected static final int fHeight = 670;  
  /** Tytuł okna aplikacji */
  private static final String fTitle = IConf.APP_NAME;
  
  /** Domyślne powiększenie widoku  */
  public static final double defaultZoom = 100.0d;  
  
  /** Używana czcionka (normalna) */  
  public static final Font normalFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
  /** Używana czcionka (mała) */  
  public static final Font smallFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
  
  /** Górny pasek narzędziowy */
  private final BottomToolBar toolBar;
  /** Belka glownego menu aplikacji */
  private final JMenuBar menu;
  /** Kontener wczytanego obrazu-zdjęcia */
  private final PhotoPanel photoPanel;
  
  /** Grupy górnego menu */
  private final JMenu menuFile, menuImage, menuEdit;//, menuFilters;
  
  /**
   * Konstruktor budujący główne okno aplikacji
   */  
  public GUI() {
      
    super(fTitle);  
    
    setIconImage(ImageRes.getImage("icon.png"));  
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
   
    
    // umieszczenie okna programu na środku ekranu
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((int)((screenSize.getWidth() - fWidth)/2), 
              (int)((screenSize.getHeight() - fHeight)/2), fWidth, fHeight);

       
    getContentPane().setBackground(new Color(0xed, 0xf4, 0xff));
    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));      
      
    // stworzenie glownego menu aplikacji
    menu = new JMenuBar();
    menuFile = new MenuFile(this);
    menu.add(menuFile);
    menuEdit = new MenuEdit(this);
    menu.add(menuEdit);
    menuImage = new MenuImage(this);
    menu.add(menuImage);
    //menuFilters = new MenuFilters(this);
    //menu.add(menuFilters);
    menu.add(new MenuHelp(this));
    
    setJMenuBar(menu);      
    
    JPanel p = new JPanel(new GridLayout(1,1));
    
    p.setBackground(Color.WHITE);
 
    photoPanel = new PhotoPanel(this);
    photoPanel.setBackground(Color.WHITE);
    photoPanel.setPreferredSize(new Dimension(780, 522));
    p.add(photoPanel);
    add(p);
    
  
    // inicjalizacja i dodanie dolnego paska narzędziowego
    toolBar = new BottomToolBar(this);
    toolBar.setEnabled(false);
    add(toolBar);             
    
    pack();
    setMinimumSize(new Dimension(fWidth, fHeight));
    setSize(fWidth, fHeight);
    
    // blokowanie minimalnego rozmiaru okna
    final GUI frame = this;
    addComponentListener(new ComponentAdapter() { 
        
       @Override
       public void componentResized(ComponentEvent e) { 
         Dimension d=frame.getSize(); 
         Dimension minD=frame.getMinimumSize(); 
         if(d.width<minD.width) d.width=minD.width; 
         if(d.height<minD.height) d.height=minD.height; 
         frame.setSize(d); 
       } 
    });

    setResizable(true);
    setVisible(true);


  }
  


  @Override
  public PhotoPanel getPhotoPanel() {
      return photoPanel;
  }        
  
  
  
  /**
   * Wczytanie z pliku nowego obrazu
   * @param fileName Nazwa pliku
   */
  public void loadImageFile(String fileName) {
      
    if (photoPanel.loadImage(fileName)) {
      setTitle(fTitle + " [" + new File(fileName).getName() + "]"); 
      enableUndoMenuItem(false);
      toolBar.getMaskPanel().setMaskVisible(false);
      toolBar.getToolPanel().setViewModeSelected();
    }
      
  }  
  
  /**
   * Zamknięcie obrazu
   */
  public void closeImageFile() {
      
    photoPanel.clear();
    enableUndoMenuItem(false);
    setTitle(fTitle);
      
  }
  
  /**
   * Ustawienie wartości suwaka powiększenia obrazu
   * @param value Powiększenie obrazu w %
   */
  @Override
  public void setZoomSliderValue(int value) {
      
    toolBar.getZoomSlider().setValue(value);
      
  }
  
  /**
   * Ustawienie dowolnej wartości suwaka powiększenia obrazu
   * @param value Powiększenie obrazu w %
   */
  public void setZoomSliderFreeValue(int value) {
      
    toolBar.getZoomSlider().setValue(value, true);
      
  }  
  
  
  /**
   * Ustawienie widoczności maski
   * @param visible True jeżeli widoczna
   */
  @Override
  public void setMaskVisible(boolean visible) {
      
    toolBar.getMaskPanel().setMaskVisible(visible);
      
  }
  
  
  
  /**
   * Włączenie/wyłączenie elementów menu związanych z obrazem
   * @param enabled True jeżeli włączone
   */
  @Override
  public void enableImageMenuItems(boolean enabled) {
      
    toolBar.setEnabled(enabled);
    menuFile.setEnabled(enabled);
    menuEdit.setEnabled(enabled);
    menuImage.setEnabled(enabled);
    //menuFilters.setEnabled(enabled);
      
  }
  
  /**
   * Włączenie wyłączenie opcji "Cofnij" w menu
   * @param enabled True jeżeli włączone
   */
  @Override
  public void enableUndoMenuItem(boolean enabled) {
      
    ((MenuEdit)menuEdit).enableUndo(enabled);
    ((MenuFile)menuFile).enableSave(enabled);
    if (enabled && !getTitle().endsWith("*")) setTitle(getTitle() + " *");
    if (!enabled && getTitle().endsWith("*")) setTitle(getTitle().substring(0, getTitle().length()-2));
      
  }
      
  
  /**
   * Włączenie wyłączenie opcji "Ponów" w menu
   * @param enabled True jeżeli włączone
   */
  @Override
  public void enableRedoMenuItem(boolean enabled) {
      
    ((MenuEdit)menuEdit).enableRedo(enabled);
      
  }
        
  
  
  /**
   * Zakończenie wykonywania programu
   */
  public void quitApp() {
      
     Fotik.quitApp();
      
  }
 

  
  /**
   * Statyczna metoda ustawiająca temat LookAndFeel 
   */
  public static void setLookAndFeel() {
      
    MetalLookAndFeel.setCurrentTheme(new OceanTheme());
    try {
      UIManager.setLookAndFeel(new MetalLookAndFeel());
    }
    catch(Exception e) {
      System.err.println(e);
    }
    
    JFrame.setDefaultLookAndFeelDecorated(true);   
    JDialog.setDefaultLookAndFeelDecorated(true); 
    
    
  }  
  
  /**
   * Statyczna metoda lokalizujaca wybrane komponenty GUI
   */  
  public static void setLocalePL() {
      
    // lokalizacja komponentu JFileChooser    
    UIManager.put("FileChooser.saveButtonText", "Zapisz");
    UIManager.put("FileChooser.saveButtonToolTipText", "Zapisz jako");
    UIManager.put("FileChooser.openButtonText", "Otwórz");
    UIManager.put("FileChooser.openButtonToolTipText", "Otwórz wybrany plik");
    UIManager.put("FileChooser.directoryOpenButtonText", "Otwórz");
    UIManager.put("FileChooser.directoryOpenButtonToolTipText", "Otwórz wybrany plik");
    UIManager.put("FileChooser.cancelButtonText", "Anuluj");
    UIManager.put("FileChooser.cancelButtonToolTipText", "Anuluj i zamknij okno");
    UIManager.put("FileChooser.updateButtonText", "Modyfikuj");
    UIManager.put("FileChooser.helpButtonText", "Pomoc");
    UIManager.put("FileChooser.fileNameLabelText", "Nazwa pliku:");
    UIManager.put("FileChooser.filesOfTypeLabelText", "Pliki typu:");
    UIManager.put("FileChooser.upFolderToolTipText", "Poziom do góry");
    UIManager.put("FileChooser.homeFolderToolTipText", "Folder domowy");
    UIManager.put("FileChooser.newFolderToolTipText", "Utwórz nowy folder");
    UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
    UIManager.put("FileChooser.detailsViewButtonToolTipText", "Szczegóły");
    UIManager.put("FileChooser.lookInLabelText", "Szukaj w:");  
    UIManager.put("FileChooser.saveInLabelText", "Zapisz w:");  
    UIManager.put("FileChooser.acceptAllFileFilterText", "Wszystkie pliki");      
        
  }

  
  @Override
  public GUI getMainFrame() {
    return this;
  }

  /**
   * Ustawienie domyślnego trybu narzędzi
   */
  public void setDefaultToolSelected() {
    toolBar.getToolPanel().setViewModeSelected();
    photoPanel.getMouseListener().setMode(MouseMode.VIEW);            
  }
    
  
  @Override
  public int getLocalBlurSize() {
     return toolBar.getToolPanel().getBlurSize();
  }

  @Override
  public int getLocalSmudgeSize() {
     return toolBar.getToolPanel().getSmudgeSize();
  }  
  
  @Override
  public int getMaxLocalSmudgeSize() {
     return toolBar.getToolPanel().getMaxSmudgeSize();
  }    
  
  
  @Override
  public void setCloneThumb(ImageIcon thumb) {
      
     toolBar.getToolPanel().changeCloneThumb(thumb);
      
  }
     
  
  
}
  
