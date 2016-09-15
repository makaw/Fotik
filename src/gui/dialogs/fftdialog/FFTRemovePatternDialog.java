/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01-02/2016
 */
package gui.dialogs.fftdialog;


import gui.GUI;
import gui.SimpleDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import fotik.IConf;
import gui.SliderField;
import gui.dialogs.InfoDialog;
import gui.loader.IProgress;
import gui.loader.IProgressInvoker;
import gui.loader.Loader;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;


/**
 *
 * Okienko dialogowe usuwania regularnych wzorów z widma obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class FFTRemovePatternDialog extends SimpleDialog implements IProgressInvoker {          
      
   private static final int defaultRadius = 50;
   private static final int defaultAxisPad = 5;
   private static final int defaultThreshold = 220;
    
    
   /** Checkbox podbicia kontrastu obrazu widma */
   private JCheckBox contrastGainedField; 
      
   private SliderField radiusField;
   private SliderField axisPadField;
   private SliderField thresholdField;
   
   private final FFTDialog fftDialog;
   
   /**
    * Konstruktor, wyświetlenie okienka
    * @param fftDialog Okno z obrazem widma
    */ 
   public FFTRemovePatternDialog(FFTDialog fftDialog) {
        
     super(fftDialog.getMainFrame(), IConf.APP_NAME + " - 2D FFT: usuwanie regularnych wzor\u00f3w");     
     this.fftDialog = fftDialog;
     super.showDialog(390, 370); 
            
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
      
      radiusField = new SliderField("Promie\u0144 otoczenia centralnego punktu", 0, 5, 250, GUI.normalFont);
      radiusField.setValue(defaultRadius);
      p.add(radiusField);
      
      JPanel p0 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      p0.setPreferredSize(new Dimension(400, 25));
      p0.setOpaque(false);
      
      JLabel txt = new JLabel("(otoczenie centralnego punktu jest pomijane)");
      txt.setFont(GUI.smallFont);
      txt.setForeground(new Color(0xa0a0a0));
      txt.setBorder(new EmptyBorder(2, 0, 5, 0));
      p0.add(txt);
      p.add(p0);
      
      
      axisPadField = new SliderField("Odst\u0119p od osi centralnego punktu", 0, 2, 50, GUI.normalFont);
      axisPadField.setValue(defaultAxisPad);
      p.add(axisPadField);
      
      thresholdField = new SliderField("Graniczna warto\u015b\u0107 jasno\u015bci", 0, 50, 255, GUI.normalFont);
      thresholdField.setValue(defaultThreshold);
      p.add(thresholdField);
      
      contrastGainedField = new JCheckBox(" wzmocniony kontrast obrazu FFT", !fftDialog.isInColor());
  
      JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
      p2.setOpaque(false);
      p2.setBorder(new EmptyBorder(0, 50, 0, 0));
      contrastGainedField.setFocusPainted(false);
      contrastGainedField.setFont(GUI.normalFont);
      p2.add(contrastGainedField);    
      
      p.add(p2);
      
      
      JButton okButton = new JButton("Wykonaj");
      okButton.setFocusPainted(false);
      okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
                           
           new Loader(frame, FFTRemovePatternDialog.this, true).load();
           dispose();
           
         }
      });                        
      
      
      JButton quitButton = new CloseButton("Anuluj");
      
      p2 = new JPanel(new FlowLayout());
      p2.setOpaque(false);
      p2.setBorder(new EmptyBorder(0, 0, 5, 0)); 
      p2.add(okButton);
      p2.add(new JLabel(" "));
      p2.add(quitButton);
      
      p.add(p2);
      add(p);
      
      
   }

    @Override
    public void start(IProgress progress) {
      
      BufferedImage img = fftDialog.getFft().getFftOp().removePatterns(fftDialog.getPhotoPanel().getPhoto().getImage(), 
                    (int)radiusField.getValue(), (int)axisPadField.getValue(), 
                    (int)thresholdField.getValue(), contrastGainedField.isSelected(), progress);  
        
      if (img == null) 
        InfoDialog.infoDialog(frame, "Nie znaleziono \u017cadnych obszar\u00f3w do usuni\u0119cia.");
      else
        fftDialog.getPhotoPanel().changeImage(img, false);        
        
    }
    
}
