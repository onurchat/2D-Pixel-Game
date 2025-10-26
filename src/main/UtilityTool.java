package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Provides utility function for image manipulation.
 */
public class UtilityTool {

    /**
     * Scales a given BufferedImage to the specified width and height.
     * 
     * @param original The original BufferedImage to scale
     * @param width the desired width
     * @param height the desired height
     * @return a new BufferedImage that is scaled
     */
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {

        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }

}
