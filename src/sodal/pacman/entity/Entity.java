package sodal.pacman.entity;

import sodal.pacman.gui.ThePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Entity {

    protected int speed;
    protected int x, y;
    protected int width, height;

    protected BufferedImage image;

    protected Rectangle rect;

    public Entity(int x, int y, int width, int height, int speed, String path) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;
        createBuffer(path);

        rect = new Rectangle(this.x, this.y, this.width,this.height);


    }


    private void createBuffer(String path) {
        try {
            image = ImageIO.read(new File(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Rectangle getRect() {
        return rect;
    }


    public abstract void update();

    public abstract void render(Graphics2D g2);


}
