package sodal.pacman.entity;

import sodal.pacman.gui.ThePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Entity {

    protected int speed;
    protected int x,y;
    protected int width,height;

    protected BufferedImage image;

    public Entity(int x, int y, int width, int height, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.width = width;
        this.height = height;


        try {
            image = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/up/1.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }




    }


    public abstract void update();
    public abstract void render(Graphics2D g2);






}
