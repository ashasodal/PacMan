package sodal.pacman.entity;

import sodal.pacman.gui.ThePanel;

import java.awt.*;

public abstract class Entity {

    protected int speed;
    protected int x,y;
    protected int size;

    public Entity(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        size = ThePanel.getTileSize();
    }




    public abstract void update();
    public abstract void render(Graphics2D g2);






}
