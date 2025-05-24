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






    public Entity(int width, int height, int speed) {

        this.speed = speed;
        this.width = width;
        this.height = height;



       // test();


    }











    public int getWidth() {
        return width;
    }




    public int getHeight() {
        return height;
    }




    public void setSpeed(int speed) {
        this.speed = speed;
    }



    public abstract void update();

    public abstract void render(Graphics2D g2);




}
