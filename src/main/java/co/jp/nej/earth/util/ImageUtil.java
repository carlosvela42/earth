package co.jp.nej.earth.util;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {
    public static final String JPG_TYPE = "jpg";
    public static final String PNG_TYPE = "png";
    
    public static byte[] getThumbnail(byte[] imageData, int width, int height, String outPutType) {
        ByteArrayInputStream in = new ByteArrayInputStream(imageData);
        try {
            BufferedImage img = ImageIO.read(in);
            if (height == 0) {
                height = (width * img.getHeight()) / img.getWidth();
            }

            if (width == 0) {
                width = (height * img.getWidth()) / img.getHeight();
            }

            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write(imageBuff, outPutType, buffer);

            return buffer.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}