/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;

import gui.GUI;
import gui.HistogramChart;
import gui.SimpleDialog;
import processing.Histogram;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import fotik.IConf;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import processing.HistogramChannel;

/**
 *
 * Okienko dialogowe z histogramem obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class HistogramDialog extends SimpleDialog {

   /** Domyślnie wybrany kanał */
   private static final HistogramChannel defaultChannel = HistogramChannel.ALL;
    
   /** Wykres histogramowy */
   private final HistogramChart histChart; 
   /** Histogram (dane) */
   private Histogram histogram;
   /** Ilość operacji histogramu do cofnięcia */
   private int undos = 0;
   /** Przycisk "cofnij" */
   private JButton undoButton;
   /** Lista kanałów histogramu */
   private JComboBox<HistogramChannel> channelBox;
   
   
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej i wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public HistogramDialog(GUI frame) {
        
     super(frame, IConf.APP_NAME + " - histogram");
     histogram = new Histogram(frame.getPhotoPanel().getPhoto().getImage());
     histChart = new HistogramChart(histogram, defaultChannel.getColor());
     super.showDialog(570, 240); 
            
   } 
   
      
   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected void getContent()  {   

      JPanel p = new JPanel();
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(5, 5, 5, 5));
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      
      JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
      p2.setOpaque(false);
      
      channelBox = new JComboBox<>();
      channelBox.setFont(GUI.NORMAL_FONT);
      channelBox.setPreferredSize(new Dimension(350, 30));      
      for (HistogramChannel c: HistogramChannel.values()) channelBox.addItem(c);   
      channelBox.setSelectedItem(defaultChannel);
      JLabel txt = new JLabel("Kana\u0142: ");
      txt.setFont(GUI.NORMAL_FONT);
      p2.add(txt);
      p2.add(channelBox);      
      p.add(p2);
      
      p.add(histChart);
      
      JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
      bPanel.setOpaque(false);
      
      final JButton stretchButton = new JButton("Rozci\u0105gnij");
      stretchButton.setFocusable(false);
      
      final JButton equalizeButton = new JButton("Wyr\u00f3wnaj");
      equalizeButton.setFocusable(false);
      
      
      undoButton = new JButton("Cofnij (0)");
      undoButton.setFocusable(false);
      undoButton.setEnabled(false);
      undoButton.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
                         
            if (undos == 0) return;  
            frame.getPhotoPanel().photoUndo();   
                        
            histogram = new Histogram(frame.getPhotoPanel().getPhoto().getImage());
            histogram.setChannel((HistogramChannel)channelBox.getSelectedItem());
            histChart.setBarColor(((HistogramChannel)channelBox.getSelectedItem()).getColor());
            histChart.setHistogram(histogram);
            if (--undos == 0) undoButton.setEnabled(false);            
            undoButton.setText("Cofnij ("+String.valueOf(undos)+")");
              
          }
      });
      
      
      stretchButton.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
              
             Object ans = new PromptDialog(frame, IConf.APP_NAME + " - histogram - rozci\u0105ganie", 
                     "Podaj pr\u00f3g rozci\u0105gania (1-50):", "1").getAnswer();
             if (ans == null) return;
             int threshold;
             try {
                threshold = Integer.parseInt(ans.toString());
                if (threshold<1 || threshold>50) throw new NumberFormatException();
             }
             catch (NumberFormatException ex) {
                InfoDialog.errorDialog(frame, "Podano nieprawid\u0142ow\u0105 warto\u015b\u0107 parametru.");
                return;
             }
              
             frame.getPhotoPanel().changeImage(
                     frame.getPhotoPanel().getPhoto().getImage().getHStretchedCopy(histogram, threshold), false);  
             refresh();
              
          }
      });
      
      equalizeButton.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
      
             frame.getPhotoPanel().changeImage(
                     frame.getPhotoPanel().getPhoto().getImage().getHEqualizedCopy(histogram), false);  
             refresh();
              
          }
      });      
      
      // zmiana kanału histogramu      
      channelBox.addItemListener(new ItemListener() {

          @Override
          public void itemStateChanged(ItemEvent e) {
             
             if (e.getStateChange() == ItemEvent.SELECTED) { 
               histogram.setChannel((HistogramChannel) e.getItem()); 
               histChart.setBarColor(((HistogramChannel) e.getItem()).getColor());
               histChart.setHistogram(histogram);
             }
             
          }
      });
    
      
      bPanel.add(undoButton);  
      
      bPanel.add(new JLabel("   "));
      
      bPanel.add(stretchButton);
      bPanel.add(equalizeButton);
      
      bPanel.add(new JLabel("   "));
                
      bPanel.add(new CloseButton());
      
      p.add(bPanel);
      
      add(p);
      
      
   }   
   
   
   /**
    * Odświeżenie okna po wykonaniu operacji
    */
   private void refresh() {
       
      histogram = new Histogram(frame.getPhotoPanel().getPhoto().getImage());
      histogram.setChannel((HistogramChannel)channelBox.getSelectedItem());
      histChart.setBarColor(((HistogramChannel)channelBox.getSelectedItem()).getColor());
      histChart.setHistogram(histogram);
      undoButton.setEnabled(true);
      undos++;
      undoButton.setText("Cofnij ("+String.valueOf(undos)+")");       
       
   }
   
   
}