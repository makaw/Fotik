/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package gui;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;


/**
 *
 * Ostylowane pole tekstowe
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class TextPanel extends JTextPane {
    
  private final static String SEP = System.getProperty("line.separator");  
  
  private final Style style, style2; 
  private final StyledDocument doc;
    
  public TextPanel() {
      
    super();
    setEditable(false);
    setOpaque(false);
    setBorder(new EmptyBorder(10, 0, 0, 0));
    setBackground(new Color(0, 0, 0, 0));
      
    doc =  getStyledDocument();  

    style = StyleContext.getDefaultStyleContext().getStyle(
                   StyleContext.DEFAULT_STYLE);
      
    StyleConstants.setFontFamily(style, GUI.NORMAL_FONT.getFamily());
    StyleConstants.setFontSize(style, GUI.NORMAL_FONT.getSize());
    StyleConstants.setForeground(style, Color.DARK_GRAY);
    StyleConstants.setBackground(style, getBackground());
       
    style2 = doc.addStyle("bold", style);
    StyleConstants.setBold(style2, true);
    StyleConstants.setForeground(style2, Color.BLACK);
     
      
  }  
  
  /**
   * Umieszczenie tekstu
   * @param txt Tekst
   * @param bold True jeżeli pogrubiony
   */
  public void setText(String txt, boolean bold) {
    
      txt = txt.replace("\n", SEP);
      
      try {          
        doc.insertString(doc.getLength(), txt, bold ? style2 : style);
      }
      catch(BadLocationException e) {
        System.err.println(e.getMessage());
      }                 
      
  }
    
    
}
