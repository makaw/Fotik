/*
 * Fotik
 * Przeglądarka zdjęć, przekształcenia obrazu, filtry, FFT 2D
 * Maciej Kawecki 01/2016
 */
package processing;

import gui.loader.IProgress;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Odszumiający filtr adaptacyjny medianowy
 *
 * @author Maciej Kawecki
 * @version 1.0
 *
 */
public class AdaptiveMedianFilter extends MedianFilter {

    /** Domyślny próg kwalifikowania krawędzi */
    public final static int defaultThresholdSize = 1;
    
    /** Próg kwalifikowania krawędzi */
    private final int threshold;   
    
    /** Parametr filtra wykrywania krawędzi */
    private final float edgeParam;
    
    /** Czy odwrócić wykryte krawędzie */
    private final boolean inverseEdgeDetection;
    
    /** Czy odszumić przed wykrywaniem krawędzi */
    private final boolean noiseRemoveEdgeDetection;

    /**
     * Konstruktor
     *
     * @param image Przetwarzany obraz
     * @param medianSize Rozmiar maski filtra medianowego
     * @param threshold Próg kwalifikowania krawędzi
     * @param edgeParam Parametr filtra wykrywania krawędzi
     * @param inverseEdgeDetection Czy odwrócić wykryte krawędzie
     * @param removeNoiseEdgeDetection Czy odszumić przed wykrywaniem krawędzi 
     * @param progress Interfejs paska postępu
     */
    public AdaptiveMedianFilter(BufferedImage image, int medianSize, int threshold,  float edgeParam, 
            boolean inverseEdgeDetection, boolean removeNoiseEdgeDetection, IProgress progress) {

        super(image, medianSize, progress);
        this.edgeParam = edgeParam;
        this.threshold = threshold;   
        this.inverseEdgeDetection = inverseEdgeDetection;
        this.noiseRemoveEdgeDetection = removeNoiseEdgeDetection;
        
    }
    

    /**
     * Przefiltrowanie obrazu
     *
     * @return Przefiltrowany obraz
     */
    @Override
    public BufferedImage filter() {
        
        BufferedImage edgeImage = new ProcessedImage(image).getEdgeImage(edgeParam, noiseRemoveEdgeDetection);
        BufferedImage dstImage = new ProcessedImage(image).getCopy();

        int total = image.getWidth() * image.getHeight();

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {

              Color medColor = new Color(getMedianColor(i, j));
              Color edgeColor = new Color(edgeImage.getRGB(i, j));
              int bright = (int)(Color.RGBtoHSB(edgeColor.getRed(), edgeColor.getGreen(),
                      edgeColor.getBlue(), null)[2] * 255.0f);
              
              // w razie spełnienia warunku krawędzi, ustalenie koloru piksela jako mediany 
              if ((bright>threshold*10.0f && inverseEdgeDetection) ||
                  (bright<threshold*10.0f && !inverseEdgeDetection)) dstImage.setRGB(i, j, medColor.getRGB());  
              //else dstImage.setRGB(i, j, Color.WHITE.getRGB());
                
            }

            // zmiana wartości paska postępu
            if (!progress.setProgress((int) (((float) (j * image.getWidth()) / (float) total) * 100))) {
                return null;
            }

        }

        return dstImage;

    }
    

}
