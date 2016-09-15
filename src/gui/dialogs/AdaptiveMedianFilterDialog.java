/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;


import gui.loader.Loader;
import gui.loader.IProgress;
import gui.loader.IProgressInvoker;
import processing.MedianFilter;
import javax.swing.JTextField;
import processing.AdaptiveMedianFilter;
import gui.GUI;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import fotik.IConf;
import gui.SimpleDialog;
import gui.SliderField;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import processing.LinearFilterFactory;
import processing.LinearFilterType;


/**
 *
 * Okienko dialogowe adaptacyjnego filtra medianowego
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class AdaptiveMedianFilterDialog extends SimpleDialog implements IProgressInvoker {
    
   /** Rozmiar maski filtra medianowego */ 
   private int medianSize;
   /** Wartość progu do kwalifikowania krawędzi */
   private int threshold; 
   /** Parametr filtra wykrywającego krawędzie */
   private float edgeParam;
   /** Pole rozmiaru maski filtra medianowego */
   private JTextField medianSizeField;
   /** Pole wartości progu do kwalifikowania krawędzi */
   private SliderField thresholdField;
   /** Pole parametru filtra wykrywającego krawędzie */
   private SliderField edgeParamField;
   /** Pole odwrócenia wykrywania krawędzi */
   private JCheckBox inverseEdgeDetectField;
   /** Pole odszumienia przed wykrywaniem krawędzi */
   private JCheckBox noiseRemoveEdgeDetectField;   
   
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej i wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public AdaptiveMedianFilterDialog(GUI frame) {
        
     super(frame, IConf.APP_NAME + " - adaptacyjny odszumiaj\u0105cy filtr medianowy");

     super.showDialog(390, 370);
   }
      
   
   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected void getContent() { 
      
     JPanel p0 = new JPanel();
     p0.setOpaque(false);
     p0.setBorder(new EmptyBorder(5, 5, 0, 5));
     p0.setPreferredSize(new Dimension(370, 420));
     p0.setLayout(new BoxLayout(p0, BoxLayout.Y_AXIS));       
       
     JPanel p = new JPanel(new FlowLayout());
     p.setOpaque(false);
     p.setPreferredSize(new Dimension(380, 330));

     thresholdField = new SliderField("Pr\u00f3g kwalifikowania kraw\u0119dzi: ", 0, 1, 20, GUI.normalFont);
     thresholdField.setValue(AdaptiveMedianFilter.defaultThresholdSize);  
     p.add(thresholdField);  
     
     p.add(new JLabel(" "));
     
     edgeParamField = new SliderField("Par. filtra wykr. kraw\u0119dzi \u03c1: ", 1, 0.1, 19.9, GUI.normalFont);
     edgeParamField.setValue(LinearFilterFactory.getFilter(LinearFilterType.EDGE_DETECT).getDefaultParam());  
     p.add(edgeParamField);  
     
     p.add(new JLabel(" "));
     
     noiseRemoveEdgeDetectField = new JCheckBox("Odszumienie przed wykrywaniem kraw\u0119dzi");
     noiseRemoveEdgeDetectField.setFont(GUI.normalFont);
     noiseRemoveEdgeDetectField.setFocusPainted(false);
     p.add(noiseRemoveEdgeDetectField);     
     
     p.add(new JLabel(" "));     
     
     inverseEdgeDetectField = new JCheckBox("Odwr\u00f3cenie wykrywania kraw\u0119dzi             ");
     inverseEdgeDetectField.setFont(GUI.normalFont);
     inverseEdgeDetectField.setFocusPainted(false);
     p.add(inverseEdgeDetectField);
     
     p.add(new JLabel(" "));
     
     JLabel txt = new JLabel("Rozmiar maski filtru medianowego: ");
     txt.setFont(GUI.normalFont);
     p.add(txt);
                 
     medianSizeField = new JTextField(5);
     medianSizeField.setText(String.valueOf(MedianFilter.defaultSize));  
     medianSizeField.setPreferredSize(new Dimension(100, 30));
     p.add(medianSizeField);  
     
     p0.add(p);
     p0.add(new JLabel(" "));
     
     JPanel bPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
     bPanel.setOpaque(false);      
            
     JButton edgeButton = new JButton("Kraw\u0119dzie");
     edgeButton.setFocusPainted(false);
     edgeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {   
                      
           float ep = (float)edgeParamField.getValue();
           
           BufferedImage img = frame.getPhotoPanel().getPhoto().getMaskImage().
                   getEdgeImage(ep, noiseRemoveEdgeDetectField.isSelected());
           
           new PreviewDialog(frame, IConf.APP_NAME + " - podgląd wykrytych krawędzi (laplasjan \u03c1="
                            + String.valueOf(ep) + ")", img);
             
        }
     });     
      
     bPanel.add(edgeButton);     
      
     bPanel.add(new JLabel("      "));
      
     JButton okButton = new JButton("Wykonaj");
     okButton.setFocusPainted(false);
     okButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {   
           
           threshold = (int)(thresholdField.getValue()); 
           edgeParam = (float)edgeParamField.getValue();
           medianSize = 0;

           try {  
             medianSize = Integer.parseInt(medianSizeField.getText());           
             if (!MedianFilter.isSizeCorrect(medianSize)) throw new NumberFormatException();
           }
           catch (NumberFormatException ex) {
             InfoDialog.errorDialog(frame, "Wprowadzono nieprawid\u0142owy rozmiar maski "
                    + "filtu medianowego (rozmiar to 3 lub wi\u0119cej, "
                     + "tylko liczby nieparzyste).");
             return;
           }           
           
           new Loader(frame, AdaptiveMedianFilterDialog.this, true).load();
           dispose();
           
        }
     });    
      
     bPanel.setPreferredSize(new Dimension(380, 60));
     bPanel.add(okButton);      
     bPanel.add(new CloseButton());     
     p0.add(bPanel);
     
     add(p0);
     
   }
      
       
      
   @Override
   public void start(IProgress progress) {
          
     BufferedImage img = frame.getPhotoPanel().getPhoto().getMaskImage().
             getAdaptiveMedianFilteredCopy(medianSize, threshold, edgeParam, inverseEdgeDetectField.isSelected(), 
                     noiseRemoveEdgeDetectField.isSelected(), progress);
       
     frame.getPhotoPanel().changeImage(frame.getPhotoPanel().getPhoto().applyMaskImage(img), false);            
       
   }
    
    
}
