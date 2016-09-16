/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;


import gui.GUI;
import gui.SimpleDialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import fotik.IConf;
import gui.SliderField;
import gui.loader.IProgress;
import gui.loader.IProgressInvoker;
import gui.loader.Loader;
import java.awt.image.BufferedImage;
import javax.swing.JComboBox;
import processing.LinearFilterFactory;
import processing.LinearFilterType;
import processing.SmartBlurFilter;


/**
 *
 * Okienko dialogowe filtru "Inteligentnego rozmycia" (Smart blur)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class SmartBlurFilterDialog extends SimpleDialog implements IProgressInvoker {          
      
   /** Pole czułości filtra */
   private SliderField sensitivityField;
   /** Pole rozmiaru kolejno rozmywanych fragmentów obrazu */
   private SliderField sizeField;
   /** Pole wyboru filtra odszumiającego */
   private JComboBox<LinearFilterType> blurFilterBox;
      
   
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public SmartBlurFilterDialog(GUI frame) {
        
     super(frame, IConf.APP_NAME + " - filtr \"Smart blur\" (\"Inteligentne rozmycie\")");          
     super.showDialog(400, 340); 
            
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
      
      // rodzaj filtra liniowego
      
      JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      p2.setOpaque(false);
      JLabel txt = new JLabel("Odszumiaj\u0105cy filtr liniowy: ");
      txt.setFont(GUI.NORMAL_FONT);
      txt.setBorder(new EmptyBorder(0, 0, 5, 0));
      p2.add(txt);      
      
      blurFilterBox = new JComboBox<>();
      blurFilterBox.setFont(GUI.NORMAL_FONT);
      blurFilterBox.setPreferredSize(new Dimension(340, 30));
      
      // tylko odszumiające i nieparametryzowane
      for (LinearFilterType f: LinearFilterType.values()) 
          if (f.isNoiseRemoving() && LinearFilterFactory.getFilter(f).getDefaultParam() == null)
            blurFilterBox.addItem(f);          
      
      blurFilterBox.setSelectedItem(LinearFilterType.NR_EVEN_9x9);
      
      p2.add(blurFilterBox);
      
      p2.setBorder(new EmptyBorder(0, 0, 15, 0));
      p.add(p2);
      p.add(new JLabel(" "));
      
      // parametry algorytmu Smart Blur
      
      sensitivityField = new SliderField("     Parametr czu\u0142o\u015bci filtra", 1, 1.0, 25.0, GUI.NORMAL_FONT);
      sensitivityField.setValue(SmartBlurFilter.defaultSensitivity);
      p.add(sensitivityField);
      
      sizeField = new SliderField("Rozmiar rozmywanych fragment\u00f3w", 0, 5, 25, GUI.NORMAL_FONT);
      sizeField.setValue(SmartBlurFilter.defaultSize);
      p.add(sizeField);      
      
      JButton okButton = new JButton("Wykonaj");
      okButton.setFocusPainted(false);
      okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   

            new Loader(frame, SmartBlurFilterDialog.this, true).load();
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
     
      BufferedImage img = frame.getPhotoPanel().getPhoto().getImage().
          getSmartBlurredCopy((LinearFilterType)blurFilterBox.getSelectedItem(), 
          sensitivityField.getValue(), (int) sizeField.getValue(), progress);  
      frame.getPhotoPanel().changeImage(frame.getPhotoPanel().getPhoto().applyMaskImage(img), false);          
        
    }
    
}
