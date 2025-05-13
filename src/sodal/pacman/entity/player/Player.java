package sodal.pacman.entity.player;

import sodal.pacman.entity.Entity;
//import sodal.pacman.entity.enemy.Enemy;
import sodal.pacman.gui.ThePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class Player extends Entity {

    private volatile Color color = Color.RED;

    protected int xCenter, yCenter;
    protected int radius;
    private Rectangle rect;




    public Player(int xCenter, int yCenter, int radius, int speed) {
        super(radius * 2, radius * 2, speed, "./src/sodal/pacman/entity/player/image/up/up1.png");
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.radius = radius;
        rect = new Rectangle(xCenter - radius, yCenter - radius, this.radius * 2, this.radius * 2);
    }

    @Override
    public void update() {
        move();
    }

    private void move() {
        if (!ThePanel.getCheckCollision()) {
            byte[] dir = ThePanel.getDirection();
            if (dir[0] == 1) {
                this.yCenter -= this.speed;
            } else if (dir[1] == 1) {
                this.yCenter += this.speed;
            } else if (dir[2] == 1) {
                this.xCenter -= this.speed;
            } else if (dir[3] == 1) {
                this.xCenter += this.speed;
            }
            this.rect.setLocation(this.xCenter - radius, this.yCenter - radius);
        }
    }


    public void moveInOppositeDirection() {
        byte dir[] = ThePanel.getDirection();
        System.out.println("dir: " +  Arrays.toString(dir));
        if (dir[0] == 1) {
            this.yCenter += 1;
            System.out.println("backtrack DOWN");
        } else if (dir[1] == 1) {
            this.yCenter -= 1;
            System.out.println("backtrack UP");
        } else if (dir[2] == 1) {
            this.xCenter += 1;
            System.out.println("backtrack RIGHT");
        } else if (dir[3] == 1) {
            this.xCenter -= 1;
            System.out.println("backtrack LEFT");
        }


        this.rect.setLocation(this.xCenter - radius, this.yCenter - radius);
    }



    @Override
    public void render(Graphics2D g2) {


        g2.drawImage(this.image, rect.x, rect.y, this.radius * 2, this.radius * 2, null);
        g2.setColor(color);
        g2.drawOval(rect.x, rect.y, this.radius * 2, this.radius * 2);
       // g2.drawRect(rect.x, rect.y, rect.width, rect.height);


    }






    public int getxCenter() {
        return xCenter;
    }

    public int getyCenter() {
        return yCenter;
    }

    public int getRadius() {
        return this.radius;
    }


    public void setColor(Color color) {
        this.color = color;
    }



}




