package sodal.pacman.entity;

import java.awt.*;

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
