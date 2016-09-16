/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.photoview;


import gui.GUI;
import gui.ImageRes;
import gui.dialogs.InfoDialog;
import fotik.History;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;


/**
 * Przewijany obszar wyświetlanego obrazu (kontener)
 * 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class PhotoPanel extends JPanel  implements ItemListener {

    /** Jeden centymetr */
    public static final int CM_UNIT = (int)((double)Toolkit.getDefaultToolkit().getScreenResolution() / 2.54d); 
    /** Czy centymetry są początkowo wybrane */
    private static final boolean DEFAULT_CM_SELECTED = false;
    /** Kolor tła panelu */   
    private Color bgColor;
    /** Przewijany obszar obrazu */
    private JScrollPane photoScrollPane;

    /** Pozioma podziałka */
    private PhotoRuler columnView;
    /** Pionowa podziałka */
    private PhotoRuler rowView;
    /** Przycisk przełączania jednostek podziałki px/cm */
    private final JToggleButton cmButton;
    /** Wyświetlany obraz - zdjęcie */
    private Photo photo;
    /** Interfejs nadrzędnego okna */
    private final IPhotoFrame photoFrame;
    /** Historia zmian wyświetlanego obrazu */
    private History<BufferedImage> history;
    
    /** Obraz do ponowienia cofniętej zmiany */
    private BufferedImage redoImage;
    /** Czy zmieniany był rozmiar w ostatniej zmianie */
    private boolean lastUndoChangedSize = false;
    
    /** Nazwa wczytanego pliku */
    private String fileName = "";
    
    /** Listener myszy (narzędzia) */
    private MouseHandler mouseListener;     

    
    /** Wewn. klasa - narożniki przewijanego obszaru */
    @SuppressWarnings("serial")
    private class Corner extends JPanel {
      @Override
      protected void paintComponent(Graphics g) {    
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());        
      }
    }     
    
    
    /**
     * Konstruktor
     * @param photoFrame Ref. do GUI 
     * @param bgColor Kolor tła podziałki
     */
    public PhotoPanel(IPhotoFrame photoFrame, Color bgColor) {
        
      setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

      this.photoFrame = photoFrame;
      this.bgColor = bgColor;
      
      add(new JLabel(ImageRes.getIcon("no_image.jpg")));        
      
      cmButton = new JToggleButton(ImageRes.getIcon("cm.png"), true);
      cmButton.setFocusPainted(false);
      cmButton.setSelected(DEFAULT_CM_SELECTED);
      cmButton.setPreferredSize(new Dimension(20, 20));   

    }
    
    
    public PhotoPanel(GUI frame) {
        
      this(frame, new Color(0xcce0ff));  
        
    }
    
    
    public PhotoPanel(Color bgColor) {
        
      this(null, bgColor);
        
    }    
    

    public MouseHandler getMouseListener() {
        return mouseListener;
    }

    
    
    
    /**
     * Zamknięcie obrazu, wyczyszczenie panelu (główne okno)
     */
    public void clear() {
       
      if (!photoFrame.getMainFrame().equals(photoFrame)) return;  
        
      removeAll();
      add(new JLabel(ImageRes.getIcon("no_image.jpg"))); 
      fileName = "";
      
      if (photoFrame != null) {
        photoFrame.enableImageMenuItems(false);
        photoFrame.enableRedoMenuItem(false);
        photoFrame.getMainFrame().setTitle("");
      }
      
      photo = null;
      redoImage = null;
      
      revalidate();
      repaint();
        
    }
    
    
    /**
     * Ustawienie powiększenia obrazu
     * @param zoom Powiększenie w %
     */
    public void setZoom(double zoom) {
            
      if (photo == null) return;
      
      photo.setZoom(zoom);
      
      columnView = new PhotoRuler(zoom, SwingConstants.HORIZONTAL, cmButton.isSelected(), bgColor);
      rowView = new PhotoRuler(zoom, SwingConstants.VERTICAL, cmButton.isSelected(), bgColor);
      
      photoScrollPane.setColumnHeaderView(columnView);
      photoScrollPane.setRowHeaderView(rowView);
      
      columnView.setPreferredWidth((int)(photo.getImage().getWidth()*(zoom/100.0d)));
      rowView.setPreferredHeight((int)(photo.getImage().getHeight()*(zoom/100.0d)));            
      
      revalidate();
      repaint();              

    }

    
    /**
     * Zmiana na wyświetlanym obrazie
     * @param img Obraz po zmianach
     * @param newSize True jeżeli został zmieniony rozmiar (np. podczas rotacji)
     */
    public void changeImage(BufferedImage img, boolean newSize) {
       
       if (img == null) return; 
               
       if (photoFrame != null) {
         photoFrame.enableUndoMenuItem(true);
         photoFrame.enableRedoMenuItem(false);
         photoFrame.setMaskVisible(false);
       }
       
       history.push(photo.getImage(), newSize);       
       
       if (newSize) rebuild(new ImageIcon(img));
       else {
          photo.setImage((Image)img);
          revalidate();
          repaint();            
       }
       
    }

    
    /**
     * Zmiana na wyświetlanym obrazie bez zmiany rozmiaru
     * @param img Obraz po zmianach
     */
    public void changeImage(BufferedImage img) {
        
      changeImage(img, true);  
        
    }    
    
    
    /**
     * Ustawienie nowego obrazu 
     * @param image Nowy obraz
     * @return True 
     */
    public boolean setInitImage(BufferedImage image) {
             
      ImageIcon img = new ImageIcon(image);
      
      history = new History<>();
      if (photoFrame !=null) {
        redoImage = null;
        photoFrame.enableRedoMenuItem(false);
        photoFrame.enableUndoMenuItem(false);
      }
      
      rebuild(img);
      
      return true;
        
    }    
    
    
    /**
     * Wczytanie nowego obrazu (z nowego pliku)
     * @param fileName Nazwa pliku
     * @return True jeżeli obrazek wczytany poprawnie
     */
    public boolean loadImage(String fileName) {
      
      BufferedImage bImg = PhotoIO.loadImageFromFile(photoFrame.getMainFrame(), fileName);  
     
      if (bImg == null) {          
         InfoDialog.errorDialog(photoFrame.getMainFrame(), "Podczas wczytywania obrazka wystąpił b\u0142\u0105d.");
         return false;          
      }      
      
      ImageIcon img = new ImageIcon(bImg);           
      
      this.fileName = fileName;
      
      redoImage = null;
      photoFrame.enableRedoMenuItem(false);
      
      history = new History<>();
      rebuild(img);
      
      return true;
        
    }

    /**
     * Zbudowanie od nowa panelu z obrazem
     * @param img Obraz po zmianach (lub nowy)
     */
    public void rebuild(ImageIcon img) {
                
      removeAll();             
     
      Corner buttonCorner = new Corner();  
      if (cmButton.getItemListeners().length == 0) cmButton.addItemListener(this);
      buttonCorner.add(cmButton); 
                 
      if (mouseListener != null && photo != null) {
        photo.removeMouseListener(mouseListener);
        photo.removeMouseMotionListener(mouseListener);
      }

      photo = new Photo(img);     
      photo.addMouseMotionListener(new MouseMotionListener() {
      
        @Override
        public void mouseMoved(MouseEvent e) { }
    
           @Override
           public void mouseDragged(MouseEvent e) {   
              scrollRectToVisible(new Rectangle(e.getX(), e.getY(), 1, 1));
           }    
      });       
      
      if (photoFrame != null) {
        if (mouseListener == null) mouseListener = new MouseHandler(photoFrame);       
        photo.addMouseListener(mouseListener);
        photo.addMouseMotionListener(mouseListener);  
      }
      
      // ustalenie początkowego powiększenia
      double w1 = (double) getPreferredSize().width / photo.getImage().getWidth();
      double h1 = (double) getPreferredSize().height / photo.getImage().getHeight();
      double startZoom = (w1 < h1 ? w1 : h1) * 100.0d;
      int zoom = (int)(startZoom < GUI.DEFAULT_ZOOM ? startZoom : GUI.DEFAULT_ZOOM);

      if (photoFrame != null) {
        photoFrame.setZoomSliderValue(zoom);
        photoFrame.enableImageMenuItems(true);      
      }
        
      photoScrollPane = new JScrollPane(photo);
      photoScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.black));

      setZoom(zoom);
      
      photoScrollPane.setColumnHeaderView(columnView);
      photoScrollPane.setRowHeaderView(rowView);
      photoScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, buttonCorner);
      photoScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER, new Corner());
      photoScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new Corner());

      add(photoScrollPane);
      
      photo.setMaxUnitInc(columnView.getIncrement());
      
      revalidate();
      repaint();      
        
    }
    

    public Photo getPhoto() {
        return photo;
    }
   
    
    
    public JToggleButton getCmButton() {
        return cmButton;
    }

    
    public String getFileName() {
        return fileName;
    }

    
    /**
     * Ponowienie cofniętej zmiany
     */
    public void photoRedo() {
        
      changeImage(redoImage, lastUndoChangedSize);
      redoImage = null;
      lastUndoChangedSize = false;
      
      photoFrame.enableRedoMenuItem(false);
      photoFrame.enableUndoMenuItem(true);
        
        
    }
    

    
    /**
     * Cofnięcie ostatniej zmiany na obrazie
     */
    public void photoUndo() {
        
       if (isChanged()) {
     
          redoImage = photo.getImage();
          lastUndoChangedSize = history.isSizeChanged();
          
          if (history.isSizeChanged()) rebuild(new ImageIcon(history.pop()));
          else {
             photo.setImage(history.pop());
             revalidate();
             repaint();    
          }
          photoFrame.enableRedoMenuItem(true);
       }
       if (!isChanged() && photoFrame != null) photoFrame.enableUndoMenuItem(false);
        
    }
    
    
    public ImageIcon getOrigImage() {
        
      return new ImageIcon(history.getOriginal());
        
    }
    
    
    /**
     * Czy obraz był zmieniany
     * @return True jeżeli obraz był zmieniany
     */
    public boolean isChanged() {
        
      return history!=null && !history.isEmpty();
        
    }
    
        
    /**
     * Zmiana jednostki podziałki (dla listenera)
     * @param e Zdarzenie zmiany
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        
      if (e.getStateChange() == ItemEvent.SELECTED) {
        rowView.setCm(true);
        columnView.setCm(true);
      } else {
        rowView.setCm(false);
        columnView.setCm(false);
      }
      photo.setMaxUnitInc(rowView.getIncrement());
      
    }    
    
    
}
