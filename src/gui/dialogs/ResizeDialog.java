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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import fotik.IConf;
import java.awt.image.AffineTransformOp;
import javax.swing.JComboBox;


/**
 *
 * Okienko dialogowe skalowania obrazu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class ResizeDialog extends SimpleDialog {          
      
   
   /** Wewn. klasa: rodzaje interpolacji */
   private enum Interpolation {
     
      BILINEAR(AffineTransformOp.TYPE_BILINEAR), BICUBIC(AffineTransformOp.TYPE_BICUBIC), 
      NEIGHBOUR(AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
      
      int code;
      
      Interpolation(int code) {
         this.code = code; 
      }
      
      @Override
      public String toString() {
          
        switch (this) {
            
            default:  return super.toString();
            case BILINEAR: return "bilinearna";  
            case BICUBIC: return "bikubiczna";
            case NEIGHBOUR: return "najbli\u017cszy s\u0105siad";            
            
        } 
          
      }       
       
   }
   
   
   /** Maksymalny rozmiar obrazu (px) */
   private final static int maxSize = 10000; 
   /** Checkbox zachowania proporcji obrazu */
   private JCheckBox keepRatioField; 
   /** Pole wyboru interpolacji */
   private JComboBox<Interpolation> interpolationField;
      
   
   
   /** Wewn. klasa: listener do zachowania proporcji rozmiarów obrazka */
   private class RatioListener implements FocusListener {
          
     /** Współczynnik proporcji obrazu */  
     private final double ratio;
     /** Drugie z pól */
     private final JTextField relatedField;
     
     RatioListener(double ratio, JTextField relatedField) {
       this.ratio = ratio;
       this.relatedField = relatedField;
     }
     
     /**
      * Zachowanie proporcji obrazu
      * @param e Zdarzenie (focus)
      */
     public void check(FocusEvent e) {
                             
       if (!keepRatioField.isSelected()) return;  
                     
       String source = ((JTextField)(e.getSource())).getText();
       try { 
          relatedField.setText(String.valueOf((int)(Double.valueOf(source)*ratio)));    
       } catch (NumberFormatException ex) {}
         
     }

     @Override
     public void focusGained(FocusEvent e) {
       check(e);
     }

     @Override
     public void focusLost(FocusEvent e) {
       check(e);
     }

   } 
    
   
   /**
    * Konstruktor, wyświetlenie okienka
    * @param frame Referencja do GUI
    */ 
   public ResizeDialog(GUI frame) {
        
     super(frame, IConf.APP_NAME + " - skalowanie");          
     super.showDialog(350, 220); 
            
   } 
   
   
    
   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected void getContent()  {   

      final double ratio = frame.getPhotoPanel().getPhoto().getImage().getRatio();

      JPanel p = new JPanel();
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(5, 5, 5, 5));
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      
      JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
      JLabel txt = new JLabel("Nowe wymiary: ");
      txt.setFont(GUI.normalFont);
      p2.add(txt);
           
      final JTextField widthField = new JTextField(5);
      final JTextField heightField = new JTextField(5);
      
      widthField.setFont(GUI.normalFont);
      widthField.setPreferredSize(new Dimension(100, 30));
      widthField.setText(String.valueOf(frame.getPhotoPanel().getPhoto().getImage().getWidth()));           
      p2.add(widthField);
      
      p2.add(new JLabel(" \u00d7 "));
      
      heightField.setFont(GUI.normalFont);
      heightField.setPreferredSize(new Dimension(100, 30));
      heightField.setText(String.valueOf(frame.getPhotoPanel().getPhoto().getImage().getHeight()));           
      p2.add(heightField);     
      
      p.setBorder(new EmptyBorder(10, 20, 0, 0));
      p.add(p2);      
      
      keepRatioField = new JCheckBox(" zachowaj proporcje obrazu", true);
      interpolationField = new JComboBox<>();
      for (Interpolation i: Interpolation.values()) 
        interpolationField.addItem(i);
                  
      p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
      p2.setOpaque(false);
      keepRatioField.setFocusPainted(false);
      keepRatioField.setFont(GUI.normalFont);
      p2.add(keepRatioField);    
      
      p.add(p2);
      
      p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
      p2.setOpaque(false);
      txt = new JLabel("Typ interpolacji: ");
      txt.setFont(GUI.normalFont);
      p2.add(txt);
      
      interpolationField.setFont(GUI.normalFont);
      interpolationField.setSelectedIndex(0);
      p2.add(interpolationField);  
      
      p.add(p2);
      
      widthField.addFocusListener(new RatioListener(1.0d/ratio, heightField));
      heightField.addFocusListener(new RatioListener(ratio, widthField));
      
      JButton okButton = new JButton("Wykonaj");
      okButton.setFocusPainted(false);
      okButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
           try {  
             int width = Integer.parseInt(widthField.getText());
             int height = Integer.parseInt(heightField.getText());
             if (width<1 || height<1 || width>maxSize || height>maxSize) throw new NumberFormatException();
             
             frame.getPhotoPanel().changeImage(
                      frame.getPhotoPanel().getPhoto().getImage().getScaledCopy(width, height, 
                              ((Interpolation)(interpolationField.getSelectedItem())).code));
             dispose();
           }
           catch (NumberFormatException ex) {
             InfoDialog.errorDialog(frame, "Wprowadzono nieprawid\u0142owe dane (zakres 1-"
                     +String.valueOf(maxSize)+").");               
           }
           
           
         }
      });                  
      
      
      JButton resButton = new JButton("Przywróć");
      resButton.setFocusPainted(false);
      resButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           widthField.setText(String.valueOf(frame.getPhotoPanel().getPhoto().getImage().getWidth()));
           heightField.setText(String.valueOf(frame.getPhotoPanel().getPhoto().getImage().getHeight()));
           keepRatioField.setSelected(true);
         }
      });
      
      
      JButton quitButton = new CloseButton("Anuluj");
      
      p2 = new JPanel(new FlowLayout());
      p2.setOpaque(false);
      p2.setBorder(new EmptyBorder(0, 0, 5, 0)); 
      p2.add(okButton);
      p2.add(new JLabel(" "));
      p2.add(resButton);
      p2.add(new JLabel(" "));
      p2.add(quitButton);
      
      p.add(p2);
      add(p);
      
      
   }
   

    
    
}
