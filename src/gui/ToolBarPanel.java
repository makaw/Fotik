/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui;

import fotik.IConf;
import gui.photoview.MouseMode;
import gui.photoview.PhotoPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;


/**
 *
 * Panel narzędzi
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class ToolBarPanel extends JPanel {
   
    
   /** Przełączanie narzędzi */
   private final ButtonGroup tools;
   /** Kontener obrazu */
   private final PhotoPanel photoPanel;
   /** Etykieta komponentu */
   private final JLabel toolsLabel;
   /** Suwak do zmiany rozmiaru obszaru lokalnego odszumiania */
   private final SliderField blurSizeField;
   /** Suwak do zmiany rozmiaru obszaru lokalnego smużenia */
   private final SliderField smudgeSizeField;
   /** Etykiety panelu z miniaturą obrazu do klonowania */
   private final JLabel cloneThumbLabelTitle, cloneThumbLabelImg;
   /** Przycisk do usunięcia skopiowanego obrazu (klonowanie) */
   private final JButton cloneThumbCancelBtn;
   
   /**
    * Konstruktor
    * @param photoPanel Kontener powiązanego obrazu
    */
   public ToolBarPanel(final PhotoPanel photoPanel) {       
       
     super();
     setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
     setOpaque(false);
     
     this.photoPanel = photoPanel;
     
     toolsLabel = new JLabel("Narz\u0119dzia");
     toolsLabel.setFont(GUI.NORMAL_FONT);
    
     final JToggleButton maskAddAreaItem =  new JToggleButton(ImageRes.getIcon("icons/arrow.png"), true);
     maskAddAreaItem.setToolTipText("Tryb przegl\u0105dania");
     maskAddAreaItem.setFocusPainted(false);
     maskAddAreaItem.setActionCommand("view");
     maskAddAreaItem.setPreferredSize(new Dimension(32, 32));
       
     maskAddAreaItem.addItemListener(new ItemListener() {
       @Override
       public void itemStateChanged(ItemEvent itemEvent) {

         if (itemEvent.getStateChange() == ItemEvent.SELECTED) setMouseListenerMode(MouseMode.VIEW);
         
       }
     });
         
     final JToggleButton maskRemoveAreaItem =  new JToggleButton(ImageRes.getIcon("icons/crop.png"), false); 
     maskRemoveAreaItem.setFocusPainted(false);
     maskRemoveAreaItem.setActionCommand("crop");
     maskRemoveAreaItem.setToolTipText("Kadrowanie");
     maskRemoveAreaItem.setPreferredSize(new Dimension(32, 32));
  
     maskRemoveAreaItem.addItemListener(new ItemListener() {
       @Override
       public void itemStateChanged(ItemEvent itemEvent) {

        if (itemEvent.getStateChange() == ItemEvent.SELECTED) setMouseListenerMode(MouseMode.CROP);
        
       }
     });     
     
     
    final JToggleButton cloneItem =  new JToggleButton(ImageRes.getIcon("icons/clone.png"), false); 
    cloneItem.setFocusPainted(false);
    cloneItem.setActionCommand("clone");
    cloneItem.setToolTipText("Klonowanie");
    cloneItem.setPreferredSize(new Dimension(32, 32));
    
    final JPanel cloneThumbPanel = new JPanel();
    cloneThumbPanel.setVisible(false);
    cloneThumbPanel.setOpaque(false);
    cloneThumbPanel.setLayout(new BoxLayout(cloneThumbPanel, BoxLayout.Y_AXIS));
    cloneThumbPanel.setPreferredSize(new Dimension(140, 70));
    cloneThumbLabelTitle = new JLabel();

    cloneThumbLabelImg = new JLabel();
    cloneThumbPanel.add(cloneThumbLabelTitle);
    
    JPanel p0 = new JPanel();
    p0.setLayout(new BoxLayout(p0, BoxLayout.X_AXIS));
    p0.setOpaque(false);
    p0.add(cloneThumbLabelImg);
    cloneThumbCancelBtn = new JButton(ImageRes.getIcon("icons/clear.png"));
    cloneThumbCancelBtn.setToolTipText("Anuluj wyb\u00f3r");
    cloneThumbCancelBtn.setFocusPainted(false);
    cloneThumbCancelBtn.setVisible(false);
    cloneThumbCancelBtn.setPreferredSize(new Dimension(32, 32));
    p0.add(cloneThumbCancelBtn);
    cloneThumbPanel.add(p0);
    
    cloneThumbLabelTitle.setFont(GUI.NORMAL_FONT);
    cloneThumbLabelImg.setBorder(new EmptyBorder(5, 10, 0, 10));
    
    
    cloneItem.addItemListener(new ItemListener() {
       @Override
       public void itemStateChanged(ItemEvent itemEvent) {

         if (itemEvent.getStateChange() == ItemEvent.SELECTED) setMouseListenerMode(MouseMode.CLONE);
         changeCloneThumb(null);
         cloneThumbPanel.setVisible(itemEvent.getStateChange() == ItemEvent.SELECTED);
         
       }
     });          
    
    
    final JToggleButton blurItem =  new JToggleButton(ImageRes.getIcon("icons/blur.png"), false); 
    blurItem.setFocusPainted(false);
    blurItem.setActionCommand("blur");
    blurItem.setToolTipText("Lokalne odszumianie");
    blurItem.setPreferredSize(new Dimension(32, 32));              
    
    blurSizeField = new SliderField("Promie\u0144", 0, 3, IConf.MAX_BLUR_RADIUS, GUI.NORMAL_FONT);
    blurSizeField.setValue(10);
    blurSizeField.changeSize(150, 70);
    blurSizeField.setVisible(false);
        
  
    blurItem.addItemListener(new ItemListener() {
       @Override
       public void itemStateChanged(ItemEvent itemEvent) {

         if (itemEvent.getStateChange() == ItemEvent.SELECTED) setMouseListenerMode(MouseMode.BLUR);
         blurSizeField.setVisible(itemEvent.getStateChange() == ItemEvent.SELECTED);
        
       }
    });           

    final JToggleButton smudgeItem =  new JToggleButton(ImageRes.getIcon("icons/smudge.png"), false); 
    smudgeItem.setFocusPainted(false);
    smudgeItem.setActionCommand("smudge");
    smudgeItem.setToolTipText("Lokalne smu\u017cenie");
    smudgeItem.setPreferredSize(new Dimension(32, 32));
    
    smudgeSizeField = new SliderField("Rozmiar smugi", 0, 1, IConf.MAX_SMUDGE_SIZE, GUI.NORMAL_FONT);
    smudgeSizeField.setValue(5);
    smudgeSizeField.changeSize(150, 70);
    smudgeSizeField.setVisible(false);    
  
    smudgeItem.addItemListener(new ItemListener() {
       @Override
       public void itemStateChanged(ItemEvent itemEvent) {

         if (itemEvent.getStateChange() == ItemEvent.SELECTED) setMouseListenerMode(MouseMode.SMUDGE);
         smudgeSizeField.setVisible(itemEvent.getStateChange() == ItemEvent.SELECTED);
         
       }
     });              
     
     p0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
     p0.setOpaque(false);
     JPanel p00 = new JPanel();
     p00.setOpaque(false);
     p00.setLayout(new BoxLayout(p00, BoxLayout.Y_AXIS));
     
     JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
     p.setOpaque(false);
     p.add(toolsLabel);
     p.setBorder(new EmptyBorder(0, 0, 4, 0));
     p00.add(p);
    
     tools = new ButtonGroup();
     tools.add(maskAddAreaItem);
     tools.add(maskRemoveAreaItem);    
     tools.add(cloneItem);
     tools.add(blurItem);
     tools.add(smudgeItem);      
     
     p = new JPanel(new FlowLayout(FlowLayout.LEFT));
     p.setOpaque(false);
     p.add(maskAddAreaItem);  
     p.add(maskRemoveAreaItem); 
     p.add(cloneItem);
     p.add(blurItem);
     p.add(smudgeItem);

     p00.add(p);
     p0.add(p00);
     p0.add(blurSizeField);   
     p0.add(smudgeSizeField);
     p0.add(cloneThumbPanel);
     add(p0);
     
   }


   private void setMouseListenerMode(MouseMode mode) {

     toolsLabel.setText("Narz\u0119dzia" + (!mode.getName().isEmpty() ? ": " + mode.getName() : ""));
     photoPanel.getMouseListener().setMode(mode);
     photoPanel.getPhoto().setCursor(mode.getCursor()); 
     
   }
   
   
   public void setViewModeSelected() {
       
     tools.getElements().nextElement().setSelected(true);
       
   }

   /**
    * Metoda zwraca "komendę" wybranego narzędzia
    * @return Komenda wybranego narzędzia
    */
   public MouseMode getToolCommand() {
       
     return MouseMode.fromString(tools.getSelection().getActionCommand());
     
   }
   
   
   @Override
   public void setEnabled(boolean enabled) {
       
     toolsLabel.setForeground(enabled ? Color.DARK_GRAY : new Color(0xc0c0c0));
     
     for (Enumeration<AbstractButton> buttons = tools.getElements(); buttons.hasMoreElements();) 
        buttons.nextElement().setEnabled(enabled);    
          
       
   }

   /**
    * Wybrany rozmiar obszaru lokalnego odszumiania
    * @return Rozmiar obszaru lokalnego odszumiania
    */
   public int getBlurSize() {
       
      return (int)blurSizeField.getValue();
      
   }

   /**
    * Wybrany rozmiar obszaru lokalnego smużenia
    * @return Rozmiar obszaru lokalnego smużenia
    */   
   public int getSmudgeSize() {
       
      return (int)smudgeSizeField.getValue();
      
   }
   
   /**
    * Maksymalny rozmiar obszaru lokalnego smużenia
    * @return Maksymalny rozmiar obszaru lokalnego smużenia
    */   
   public int getMaxSmudgeSize() {
       
      return (int)smudgeSizeField.getMaxValue();
      
   }   
   
   
   public void changeCloneThumb(ImageIcon thumb) {
       
      if (thumb == null) {
         
        cloneThumbLabelTitle.setText("");
        cloneThumbLabelImg.setIcon(null);
        cloneThumbCancelBtn.setVisible(false);
        setMouseListenerMode(MouseMode.CLONE);     
      
     }
      
     else {
     
       cloneThumbLabelTitle.setText(thumb.getImage().getWidth(null)+" \u00d7 "+thumb.getImage().getHeight(null));    
       cloneThumbLabelImg.setIcon(thumb);
       cloneThumbCancelBtn.setVisible(true);
       setMouseListenerMode(MouseMode.CLONE_PUT);
       
       cloneThumbCancelBtn.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               changeCloneThumb(null);                         
           }
       });
       
      }
    
       
   }
   
   
    
}
