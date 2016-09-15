/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;


import gui.GUI;
import gui.SliderField;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import fotik.IConf;
import gui.loader.IProgress;
import gui.loader.IProgressInvoker;
import gui.loader.Loader;
import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import processing.HistogramChannel;
import processing.ProcessedImage;

/**
 *
 * Okienko dialogowe z korekcją gamma obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GammaCorrectionDialog extends ThumbnailDialog implements IProgressInvoker {

   private static final float minVal = 0.2f;
   private static final float maxVal = 6.0f;
   private static final float defaultVal = 1.0f;
    
   /** Suwak kanału jasności */
   private SliderField brightField;
   /** Suwak kanału czerwonego */
   private SliderField redField;
   /** Suwak kanału zielonego */
   private SliderField greenField;
   /** Suwak kanału niebieskiego */
   private SliderField blueField;
   /** Czy pracować na odrębnych kanałach RGB */
   private JCheckBox channelsField;
   
   private JPanel padPanel;

   /** Wewn. klasa - listener suwaków */
   private class SliderListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
        
           SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  refresh();
               }
           });
                      
        }
   }  
   
   
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public GammaCorrectionDialog(GUI frame) {
        
     super(frame, IConf.APP_NAME + " - korekcja gamma", 280);

   }
      
   
   /**
    * Metoda wyświetlająca zawartość okienka
    * @return Panel z zaw.okienka
    */
   @Override
   protected JPanel getFieldsPanel() { 
             
       
      redField = new SliderField("\u03b3: "+HistogramChannel.RED.toString(), 1, minVal, maxVal, GUI.normalFont);
      redField.setVisible(false);
      redField.setValue(defaultVal); 
      
      greenField = new SliderField("\u03b3: "+HistogramChannel.GREEN.toString(), 1, minVal, maxVal, GUI.normalFont);
      greenField.setVisible(false);
      greenField.setValue(defaultVal); 
      
      blueField = new SliderField("\u03b3: "+HistogramChannel.BLUE.toString(), 1, minVal, maxVal, GUI.normalFont);
      blueField.setVisible(false);
      blueField.setValue(defaultVal);       

      brightField = new SliderField("\u03b3: "+HistogramChannel.ALL.toString(), 1, minVal, maxVal, GUI.normalFont);
      brightField.setValue(defaultVal); 
      
      
      padPanel = new JPanel();
      padPanel.setOpaque(false);
      padPanel.setPreferredSize(new Dimension(260, 40));
      
      JPanel p = new JPanel();
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(15, 0, 0, 0));
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      
      p.add(brightField);      
      p.add(redField);
      p.add(greenField);
      p.add(blueField);
      p.add(padPanel);
      
      // listenery
      redField.addPropertyChangeListener(new SliderListener());    
      greenField.addPropertyChangeListener(new SliderListener());  
      blueField.addPropertyChangeListener(new SliderListener());  
      brightField.addPropertyChangeListener(new SliderListener());  
      
      JScrollPane scrollPane = new JScrollPane(p);
      scrollPane.setBorder(null);
      scrollPane.getViewport().setBorder(null);
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      p.setMinimumSize(new Dimension(260, 200));
      p.setMaximumSize(new Dimension(260, 300));
      scrollPane.setPreferredSize(new Dimension(275, 200));
      JPanel p0 = new JPanel();
      p0.setOpaque(false);
      p0.add(scrollPane);
      
      return p0;
      
   }
   
   
   
   @Override
   protected JPanel getButtonsPanel() {
           
      JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      bPanel.setOpaque(false);      
      
      channelsField = new JCheckBox("Kana\u0142y RGB", false);
      channelsField.setFocusPainted(false);
      channelsField.setFont(GUI.normalFont);
      
      
      channelsField.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
             
            brightField.setVisible(!channelsField.isSelected());
            redField.setVisible(channelsField.isSelected());
            greenField.setVisible(channelsField.isSelected());  
            blueField.setVisible(channelsField.isSelected());
            padPanel.setVisible(!channelsField.isSelected());                        
            
            refresh();
            
          }
      });      
      
      bPanel.add(channelsField);
      bPanel.add(new JLabel("  "));
      
      
      JButton okButton = new JButton("Wykonaj");
      okButton.setFocusPainted(false);
      okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
           new Loader(frame, GammaCorrectionDialog.this, true).load();                   
           dispose();       

         }
      });    
      
      bPanel.add(okButton);
      
      
      JButton resButton = new JButton("Przywróć");
      resButton.setFocusPainted(false);
      resButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
            brightField.setValue(defaultVal);
            redField.setValue(defaultVal);
            greenField.setValue(defaultVal);
            blueField.setValue(defaultVal);
            
            refresh();
             
         }
      });     
      
      bPanel.add(resButton);
      
      
      bPanel.add(new CloseButton());
      
      return bPanel;
      
   }
      
   /**
    * Odświeżenie miniatury obrazu
    */
   private void refresh() {

     BufferedImage dest = new ProcessedImage(thumbImage).getCopy();
     
     if (!channelsField.isSelected()) {
        if (brightField.getValue() != 1.0) 
         dest = new ProcessedImage(dest).getGammaCorrectedCopy((float)brightField.getValue(), HistogramChannel.ALL);
     }
     else {
       if (redField.getValue() != 1.0)  
         dest = new ProcessedImage(dest).getGammaCorrectedCopy((float)redField.getValue(), HistogramChannel.RED);
       if (greenField.getValue() != 1.0)
         dest = new ProcessedImage(dest).getGammaCorrectedCopy((float)greenField.getValue(), HistogramChannel.GREEN); 
       if (blueField.getValue() != 1.0)
         dest = new ProcessedImage(dest).getGammaCorrectedCopy((float)blueField.getValue(), HistogramChannel.BLUE);  
     }
          
     thumbnail.setIcon(new ImageIcon(dest));
     thumbnail.revalidate();
     thumbnail.repaint();     
     
    }  
   
   

    @Override
    public void start(IProgress progress) {
     
        if (!channelsField.isSelected()) {
           frame.getPhotoPanel().changeImage(frame.getPhotoPanel().getPhoto().getImage().
                 getGammaCorrectedCopy((float)brightField.getValue(), HistogramChannel.ALL), false);
            progress.setProgress(100);
            progress.hideComponent();
        }
        else {
           BufferedImage dest = frame.getPhotoPanel().getPhoto().getImage().
                 getGammaCorrectedCopy((float)redField.getValue(), HistogramChannel.RED);  
           progress.setProgress(33);
           dest = new ProcessedImage(dest).getGammaCorrectedCopy((float)greenField.getValue(), HistogramChannel.GREEN);  
           progress.setProgress(66);
           dest = new ProcessedImage(dest).getGammaCorrectedCopy((float)blueField.getValue(), HistogramChannel.BLUE);    
           progress.setProgress(100);
           frame.getPhotoPanel().changeImage(dest, false);
           progress.hideComponent();
           
        }
              
        
    }
      

   
}

