/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui;

import gui.photoview.IPhotoFrame;
import gui.photoview.MouseMode;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;


/**
 *
 * Dolny pasek narzędziowy w oknie aplikacji
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class BottomToolBar extends JToolBar {

  /** Pole powiększenia widoku */
  private final SliderField zoomSlider;  
  /** Panel warstwy maski */
  private final ToolBarMaskPanel maskPanel;
  /** Panel narzędzi */
  private final ToolBarPanel toolPanel;  
  
  /** Kolor linii separatora */
  private static final Color sepColor = new Color(0xccccff);
  /** Okno z panelem przetwarzanego obrazu */
  private final IPhotoFrame photoFrame;
  
  
  
  /**
   * Wewn. klasa - separator grup przycisków
   */
  private class ToolBarSeparator extends JSeparator {
      
     private ToolBarSeparator() {
         
       super(SwingConstants.VERTICAL);
       setPreferredSize(new Dimension(1, 65));
       setBackground(sepColor);       
       setForeground(sepColor);
       
     }
      
  }
  
  
  /**
   * Konstruktor
   * @param photoFrame Okno zawierające kontener zdjęcia
   * @param showMaskToolBar Czy pokazywać panel narzędzi maski
   */
  public  BottomToolBar(final IPhotoFrame photoFrame, boolean showMaskToolBar) {
  
    super();
      
    setFloatable(false);
    setOpaque(false);
    
    this.photoFrame = photoFrame;
    
    setLayout(new FlowLayout(FlowLayout.LEFT));
    setPreferredSize(new Dimension(800, 90));
    setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, sepColor));
    
    zoomSlider = new SliderField("Powi\u0119kszenie", 0, 5, 200, "%");
    
    if (photoFrame.getMainFrame().equals(photoFrame)) zoomSlider.setValue((int)GUI.defaultZoom);    
    else zoomSlider.setValue(photoFrame.getPhotoPanel().getPhoto().getZoom());
    
    add(zoomSlider);    
    

    // listener do zmiany powiekszenia
    zoomSlider.addPropertyChangeListener(new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
        
           if (Math.round((double)evt.getOldValue()) == Math.round((double)evt.getNewValue())) return; 
            
           photoFrame.getPhotoPanel().setZoom((double)evt.getNewValue());
           
        }
    });    
    
    add(new ToolBarSeparator());
    add(new JLabel(" "));

    maskPanel = new ToolBarMaskPanel(photoFrame.getPhotoPanel());
    
    if (showMaskToolBar) {
      add(maskPanel);
      add(new JLabel(" "));      
      add(new ToolBarSeparator());
      add(new JLabel("  "));    
    }
       
    toolPanel = new ToolBarPanel(photoFrame.getPhotoPanel());

    if (photoFrame.getMainFrame().equals(photoFrame)) add(toolPanel);  
    
  }
  

  
 /**
   * Konstruktor
   * @param photoFrame Okno zawierające kontener zdjęcia
   */
  public  BottomToolBar(IPhotoFrame photoFrame) {
      
    this(photoFrame, true);  
      
  }
  
  
  
  public void addElement(int sWidth) {
      
    addElement(null, sWidth);  
      
  }
  
  
  public void addElement(Component element) {
      
     addElement(element, 80); 
      
  }
  
  
  private void addElement(Component element, int sWidth) {
      
      JPanel p = new JPanel(new FlowLayout());
      
      //p.setBorder(new EmptyBorder(12, 0, 0 ,0));
      p.setOpaque(false);
      
      if (element != null)  {
        p.add(element);    
        element.setPreferredSize(new Dimension(140, 25));
        element.setSize(new Dimension(140, 25));
      }
      else add(Box.createHorizontalStrut(sWidth));
      
      JButton b = new JButton("Zamknij", ImageRes.getIcon("icons/clear.png"));
      b.setPreferredSize(new Dimension(140, 25));
      b.setSize(new Dimension(140, 25));
      b.setFocusPainted(false);
      b.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            try {  
              ((JDialog)photoFrame).dispose();
            }
            catch (ClassCastException ex) {}
          }
      });
      
      p.add(b);
      add(p);
      
      
  }  
  
  
  public SliderField getZoomSlider() {
      return zoomSlider;
  }

  
  public ToolBarMaskPanel getMaskPanel() {
      return maskPanel;
  }      

  public ToolBarPanel getToolPanel() {
     return toolPanel;
  }
      
  
    
  @Override
  public void setEnabled(boolean enabled) {
        
    zoomSlider.setEditable(enabled);   
    maskPanel.setEnabled(enabled);
    toolPanel.setEnabled(enabled);
        
  }
  
    
}
