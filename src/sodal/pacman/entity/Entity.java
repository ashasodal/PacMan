package sodal.pacman.entity;

import sodal.pacman.gui.ThePanel;

import java.awt.*;

public abstract class Entity {

    private int speed;
    private int xPos, yPos;
    private int size;

    public Entity(int x, int y, int speed) {
        this.xPos = x;
        this.yPos = y;
        this.speed = speed;
        size = ThePanel.getTileSize();
    }




    public abstract void update(Graphics2D g2);
    public abstract void render(Graphics2D g2);


    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getSize() {
        return size;
    }
}
