/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui;

import gui.photoview.MouseMode;
import gui.photoview.PhotoPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 *
 * Panel kontrolny narzędzi maski filtrowania
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class ToolBarMaskPanel extends JPanel {
   
    
   /** Maska wł./wył. */ 
   private final JCheckBox maskVisibleItem;
   /** Przełączanie narzędzi */
   private final ButtonGroup maskTools;
   /** Kontener obrazu */
   private final PhotoPanel photoPanel;
    
   
   /**
    * Konstruktor
    * @param photoPanel Kontener przetwarzanego obrazu
    */
   public ToolBarMaskPanel(final PhotoPanel photoPanel) {       
       
     super();
     setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
     setOpaque(false);
     
     this.photoPanel = photoPanel;
     
     maskVisibleItem = new JCheckBox("Maska (filtry)", true);
     maskVisibleItem.setOpaque(false);
     maskVisibleItem.setFont(GUI.normalFont);
     maskVisibleItem.setFocusPainted(false);
     maskVisibleItem.setSelected(false);
    
     final JToggleButton maskAddAreaItem =  new JToggleButton(ImageRes.getIcon("icons/select.png"), true);
     maskAddAreaItem.setToolTipText("Dodawanie obszar\u00f3w (prostok\u0105t)");
     maskAddAreaItem.setFocusPainted(false);
     maskAddAreaItem.setActionCommand("add_rect");
     maskAddAreaItem.setPreferredSize(new Dimension(32, 32));
       
     maskAddAreaItem.addItemListener(new ItemListener() {
       @Override
       public void itemStateChanged(ItemEvent itemEvent) {

        if (itemEvent.getStateChange() == ItemEvent.SELECTED) setMouseListenerMode(MouseMode.ADD_RECTANGLE);
        
       }
     });
     
     final JToggleButton maskAddAreaEllipseItem =  new JToggleButton(ImageRes.getIcon("icons/select_ellipse.png"), true);
     maskAddAreaEllipseItem.setToolTipText("Dodawanie obszar\u00f3w (elipsa)");
     maskAddAreaEllipseItem.setFocusPainted(false);
     maskAddAreaEllipseItem.setActionCommand("add_ellipse");
     maskAddAreaEllipseItem.setPreferredSize(new Dimension(32, 32));
       
     maskAddAreaEllipseItem.addItemListener(new ItemListener() {
       @Override
       public void itemStateChanged(ItemEvent itemEvent) {

        if (itemEvent.getStateChange() == ItemEvent.SELECTED) setMouseListenerMode(MouseMode.ADD_ELLIPSE);
       }
     });     
         
     final JToggleButton maskRemoveAreaItem =  new JToggleButton(ImageRes.getIcon("icons/deselect.png"), false); 
     maskRemoveAreaItem.setFocusPainted(false);
     maskRemoveAreaItem.setActionCommand("remove");
     maskRemoveAreaItem.setToolTipText("Usuwanie obszar\u00f3w");
     maskRemoveAreaItem.setPreferredSize(new Dimension(32, 32));
  
     maskRemoveAreaItem.addItemListener(new ItemListener() {
       @Override
       public void itemStateChanged(ItemEvent itemEvent) {

        if (itemEvent.getStateChange() == ItemEvent.SELECTED) setMouseListenerMode(MouseMode.REMOVE);
        
       }
     });     
     
       
     maskVisibleItem.addChangeListener(new ChangeListener() {

         @Override
         public void stateChanged(ChangeEvent e) {
          
           try {  
             
             boolean sel = ((AbstractButton)e.getSource()).getModel().isSelected();               
             photoPanel.getPhoto().setMaskLayerVisible(sel);
             photoPanel.getMouseListener().setMaskEnabled(sel);
           
             if (sel) ((BottomToolBar)getParent()).getToolPanel().setViewModeSelected();
             else maskTools.getElements().nextElement().setSelected(true);
             
             ((BottomToolBar)getParent()).getToolPanel().setEnabled(!sel);
             enableButtons(sel);
             
             MouseMode cmd = sel ? getMaskToolCommand() : getToolPanelCommand();
             if (cmd != null) setMouseListenerMode(cmd);              

             photoPanel.revalidate();
             photoPanel.repaint();
             
      
           }
           catch (NullPointerException ex) {}
           
        }
     });
        
    
     JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
     p.setOpaque(false);
     p.add(maskVisibleItem);
     add(p);
     
     maskTools = new ButtonGroup();
     maskTools.add(maskAddAreaItem);
     maskTools.add(maskAddAreaEllipseItem);
     maskTools.add(maskRemoveAreaItem);    
     
     enableButtons(false);
      
     p = new JPanel(new FlowLayout(FlowLayout.LEFT));
     p.setOpaque(false);
     p.add(maskAddAreaItem);  
     p.add(maskAddAreaEllipseItem);
     p.add(maskRemoveAreaItem);          
    
     add(p);
     
              
   }


   /**
    * Ustawienie trybu myszy
    * @param mode Tryb myszy
    */
   private void setMouseListenerMode(MouseMode mode) {

     photoPanel.getMouseListener().setMode(mode);
     photoPanel.getPhoto().setCursor(mode.getCursor());
     
   }
   
   /**
    * Zmiana etykiety panelu
    * @param text Nowa etykieta
    */
   public void setMaskLabelText(String text) {
       
     maskVisibleItem.setText(text);
       
   }
   

   /**
    * Metoda zwraca "komendę" wybranego narzędzia
    * @return Komenda wybranego narzędzia
    */
   public MouseMode getMaskToolCommand() {
       
     return MouseMode.fromString(maskTools.getSelection().getActionCommand());
     
   }
   
   /**
    * Próba pobrania komendy z panelu narzędzi lokalnych (kadrowanie, klonowanie itp)
    * @return Komenda narzędzia 
    */
   private MouseMode getToolPanelCommand() {
       
     try {
         
        return ((BottomToolBar)getParent()).getToolPanel().getToolCommand();
         
     }  
     
     catch (Exception e)  { return null; }       
       
   }
   
   
   public void setMaskVisible(boolean visible) {
       
     maskVisibleItem.setSelected(visible);
       
   }
   
   
   @Override
   public void setEnabled(boolean enabled) {
       
     maskVisibleItem.setEnabled(enabled);
     if (!enabled) enableButtons(false);
     
       
   }
   
   private void enableButtons(boolean enabled) {
       
     for (Enumeration<AbstractButton> buttons = maskTools.getElements(); buttons.hasMoreElements();) 
        buttons.nextElement().setEnabled(enabled);                
       
   }
    
}
