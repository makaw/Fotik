/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui.dialogs;

import gui.GUI;
import gui.SimpleDialog;
import processing.LinearFilterFactory;
import processing.LinearFilterType;
import processing.ILinearFilter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import fotik.IConf;
import java.awt.image.BufferedImage;


/**
 *
 * Okienko dialogowe rotacji obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class LinearFilterDialog extends SimpleDialog {
    
    
   /** Lista wyboru */
   private JComboBox<LinearFilterType> filterBox;       
   /** Panel z maską wybranego filtra */
   private JPanel maskPanel;
   /** Pole parametru filtra */
   private JTextField paramField;
   /** Etykieta pola parametru filtra */
   private JLabel paramFieldLabel;
   /** Pola maski filtra */
   private final Map<Integer, JTextField> maskPanelValues;
   
   
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public LinearFilterDialog(GUI frame) {
        
     super(frame, IConf.APP_NAME + " - filtracja liniowa-splotowa");
     maskPanelValues = new HashMap<>();
     super.showDialog(450, 270); 
            
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
      
      // rodzaj filtra
      
      JLabel txt = new JLabel("Filtr: ");
      txt.setFont(GUI.normalFont);
      p2.add(txt);
           
      filterBox = new JComboBox<>();
      filterBox.setFont(GUI.normalFont);
      filterBox.setPreferredSize(new Dimension(350, 30));
      
      for (LinearFilterType f: LinearFilterType.values()) 
          if (f.isVisibleInList()) filterBox.addItem(f);          
      
      p2.add(filterBox);
           
      p.setBorder(new EmptyBorder(10, 20, 10, 10));
      p.add(p2);      
      
      // parametr filtra
      
      p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
      p2.setOpaque(false);
      paramFieldLabel = new JLabel("Parametr: ");
      paramFieldLabel.setFont(GUI.normalFont);
      p2.add(paramFieldLabel);
      
      paramField = new JTextField(5);
      paramField.setPreferredSize(new Dimension(120, 23));
      p2.add(paramField);
      p.add(p2);      
      
      // maska filtra      
      
      maskPanel = new JPanel();      
      maskPanel.setOpaque(false);
      JScrollPane scrollPane = new JScrollPane(maskPanel);
      scrollPane.setOpaque(false);
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setPreferredSize(new Dimension(260, 70));
      
      p.add(scrollPane);
     
      
      filterBox.addItemListener(new ItemListener() {

          @Override
          public void itemStateChanged(ItemEvent e) {
             
             if (e.getStateChange() == ItemEvent.SELECTED) { 
               showMask((LinearFilterType) e.getItem(), null); 
             }
             
          }
      });
      
      
      paramField.getDocument().addDocumentListener(new DocumentListener() {

          @Override
          public void insertUpdate(DocumentEvent e) {
            changePar();
          }

          @Override
          public void removeUpdate(DocumentEvent e) {
            changePar();
          }

          @Override
          public void changedUpdate(DocumentEvent e) {
            changePar();
          }
     
          
          public void changePar() {
            
            try {  
              float p = Float.valueOf(paramField.getText());
              showMask((LinearFilterType) filterBox.getSelectedItem(), p);              
            }
            catch (NumberFormatException e) { }
              
          }
      });
      
      p2 = new JPanel();
      p2.setOpaque(false);
      p2.setPreferredSize(new Dimension(300, 10));
      p.add(p2);
      
       
      showMask((LinearFilterType) filterBox.getSelectedItem(), null);
           
      filterBox.setFont(GUI.normalFont);
      filterBox.setPreferredSize(new Dimension(350, 30));      
      
      
      JButton okButton = new JButton("Wykonaj");
      okButton.setFocusPainted(false);
      okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
                      
           LinearFilterType f = (LinearFilterType)(filterBox.getSelectedItem()); 
           Float par = null;
           if (paramField.isEnabled()) {
               
             try {  
               par =  Float.parseFloat(paramField.getText());  
             }
             catch (NumberFormatException ex) {
               InfoDialog.errorDialog(frame, "Wprowadzono nieprawid\u0142owe dane ");
               return;
             }
               
           }
                      
           BufferedImage img = frame.getPhotoPanel().getPhoto().getMaskImage().
                           getLFilteredCopy(LinearFilterFactory.getFilter(f, par));
           frame.getPhotoPanel().changeImage(frame.getPhotoPanel().getPhoto().applyMaskImage(img), false);
           dispose();
           
         }
      });
      
      
      JButton quitButton = new CloseButton("Anuluj");
      
      p2 = new JPanel(new FlowLayout());
      p2.setOpaque(false);
      p2.setBorder(new EmptyBorder(0, 0, 5, 0)); 
      quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
      p2.add(okButton);
      p2.add(new JLabel(" "));
      p2.add(quitButton);
      
      p.add(p2);
      add(p);
      
      
   }
   
   
   private void showMask(LinearFilterType filterType, Object param) {
       
     if (filterType==null) return;
     
     Object defaultParam = LinearFilterFactory.getFilter(filterType).getDefaultParam();
     
     NumberFormat nf = NumberFormat.getInstance();
     nf.setMaximumFractionDigits(2);
     
     paramField.setEnabled(defaultParam!=null);
     paramFieldLabel.setForeground(defaultParam!=null ? Color.DARK_GRAY : Color.LIGHT_GRAY);
     if (defaultParam==null) param = null;
     else if (param == null) paramField.setText(String.valueOf(defaultParam));
          
     nf.setMaximumFractionDigits(7);
          
     ILinearFilter filter = LinearFilterFactory.getFilter(filterType, param);
     float[] mask = filter.getMask().getKernelData(null);
     int size1 = filter.getMask().getWidth();
     int size = mask.length;          
     
     maskPanel.removeAll();     
     maskPanel.setLayout(new GridLayout(size1, size1, 2, 2));
     maskPanelValues.clear();
     
     for (int i=0; i<size; i++) { 
       JTextField tmp = new JTextField(4);
       tmp.setText(nf.format(mask[i]));
       tmp.setCaretPosition(0);
       tmp.setEditable(false);
       maskPanel.add(tmp);
       maskPanelValues.put(i, tmp);
     }
           
     maskPanel.revalidate();
     maskPanel.repaint();
       
   }

    
    
}
