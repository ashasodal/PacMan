package sodal.pacman.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UIManager {


    public static void switchTo(JFrame frame, JPanel from, JPanel to) {
        from.removeKeyListener((KeyListener) from);
        frame.remove(from);

        frame.add(to);
        to.addKeyListener((KeyListener) to);
        to.setFocusable(true);
        to.requestFocusInWindow();

        frame.validate();
        frame.repaint();
    }

    public static BufferedImage createBuffer(int width, int height, String path) {
        try {
            // Load the original image
            BufferedImage original = ImageIO.read(new File(path));
            // Create a new resized image
            BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resized.createGraphics();
            g2d.drawImage(original, 0, 0, width, height, null);
            g2d.dispose();
            return resized;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // file could not be found!
        return null;
    }

    public static Font getFont(String path, float fontSize) {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(fontSize);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (IOException | FontFormatException e) {
            font = new Font(Font.SERIF, Font.BOLD, (int) fontSize);
        }
        return font;
    }
}
