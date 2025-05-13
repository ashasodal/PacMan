package sodal.pacman.entity;

import sodal.pacman.gui.ThePanel;

import javax.imageio.ImageIO;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Entity {

    protected int speed;

    protected int width, height;

    protected BufferedImage image;




    public Entity(int width, int height, int speed, String path) {

        this.speed = speed;
        this.width = width;
        this.height = height;
        createBuffer(path);


       // test();


    }


    private void createBuffer(String path) {
        try {
            image = ImageIO.read(new File(path));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }








    public int getWidth() {
        return width;
    }




    public int getHeight() {
        return height;
    }

    public void test() {
        Color c = new Color(100, 55, 6);
        int newColor = c.getRGB();

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                int rgb = image.getRGB(j, i);
                int alpha = (rgb >> 24) & 0xFF;

                if (alpha == 0) {
                    image.setRGB(j, i, newColor);
                }
            }
        }
    }


    public void setSpeed(int speed) {
        this.speed = speed;
    }



    public abstract void update();

    public abstract void render(Graphics2D g2);


}
