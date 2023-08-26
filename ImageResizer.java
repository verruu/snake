import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResizer {

    private final int desiredWidth = 5;  // Set the desired width for resized images
    private final int desiredHeight = 5; // Set the desired height for resized images

    private final File resourcesDir = new File("resources"); // Path to the resources directory
    private final File[] fileList = resourcesDir.listFiles();

    public void resize() {
        // Iterate through all the image files in the resources directory
        for (File file : fileList) {
            if (file.isFile() && file.getName().endsWith(".png")) {
                try {
                    // Load the image using ImageIcon and getResource
                    ImageIcon imageIcon = new ImageIcon(ImageResizer.class.getResource("/resources/" + file.getName()));
                    Image image = imageIcon.getImage();

                    // Create a new BufferedImage with the desired dimensions
                    BufferedImage resizedImage = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_ARGB);

                    // Draw the loaded image onto the new BufferedImage
                    Graphics2D g2d = resizedImage.createGraphics();
                    g2d.drawImage(image, 0, 0, desiredWidth, desiredHeight, null);
                    g2d.dispose();

                    // Save the resized image to a new file or overwrite the original file
                    ImageIO.write(resizedImage, "png", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}