package sodal.pacman.entity.player;

import sodal.pacman.entity.Entity;
//import sodal.pacman.entity.enemy.Enemy;
import sodal.pacman.gui.ThePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Player extends Entity {

    private volatile Color color = Color.RED;

    protected int xCenter, yCenter;
    protected int radius;
    private Rectangle rect;
    private ThePanel panel;

    //images
    private BufferedImage pacman;
    protected BufferedImage up1, up2, up3;

    //animation
    private long animationTimeStamp = -1;
    private final long CHANGE_ANIMATION_MS = 10;


    public Player(int xCenter, int yCenter, int radius, int speed, ThePanel panel) {
        super(radius * 2, radius * 2, speed);
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.radius = radius;
        this.panel = panel;

        rect = new Rectangle(xCenter - radius, yCenter - radius, this.radius * 2, this.radius * 2);


        createBuffer();


    }


    private void createBuffer() {
        try {
            this.pacman = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/pacman.png"));
            this.up1 = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/up/up1.png"));
            this.up2 = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/up/up2.png"));
            this.up3 = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/up/up3.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update() {
        move();
        worldCollision();
    }

    private void worldCollision() {
        //circle - rectangular collision
        for (Rectangle rect : ThePanel.getWorld()) {
            if (this.rect.intersects(rect)) {
                if (panel.circleRectCollision(rect)) {
                    //backtrack player until it doesn't collide with worldRect.
                    panel.backtrack(rect);
                }
            }
        }
    }


    private void moveUp(int speed) {
        if (this.rect.y > 0) {
            this.yCenter -= speed;
        }
        /*if(animationTimeStamp == -1) {
            animationTimeStamp = System.currentTimeMillis();
            this.
        }
        if(System.currentTimeMillis() - animationTimeStamp >= CHANGE_ANIMATION_MS) {

        }*/

    }

    private void moveDown(int speed) {
        if (this.rect.y < ThePanel.getHEIGHT() - 2 * ThePanel.getTileSize()) {
            this.yCenter += speed;
        }

    }

    private void moveLeft(int speed) {
        if (this.rect.x > 0) {
            this.xCenter -= speed;
        }
    }

    private void moveRight(int speed) {
        if (this.rect.x < ThePanel.getWIDTH() - ThePanel.getTileSize()) {
            this.xCenter += speed;
        }
    }

    private void move() {
        byte[] dir = ThePanel.getDirection();
        if (dir[0] == 1) {
            moveUp(speed);
        } else if (dir[1] == 1) {
            moveDown(speed);
        } else if (dir[2] == 1) {
            moveLeft(speed);
        } else if (dir[3] == 1) {
            moveRight(speed);
        }
        this.rect.setLocation(this.xCenter - radius, this.yCenter - radius);
    }


    public void moveInOppositeDirection() {
        byte dir[] = ThePanel.getDirection();
        // System.out.println("dir: " + Arrays.toString(dir));
        if (dir[0] == 1) {
            moveDown(1);
            /// System.out.println("backtrack DOWN");
        } else if (dir[1] == 1) {
            moveUp(1);
            //  System.out.println("backtrack UP");
        } else if (dir[2] == 1) {
            moveRight(1);
            //  System.out.println("backtrack RIGHT");
        } else if (dir[3] == 1) {
            moveLeft(1);
            //  System.out.println("backtrack LEFT");
        }
        this.rect.setLocation(this.xCenter - radius, this.yCenter - radius);
    }


    @Override
    public void render(Graphics2D g2) {
        // g2.setColor(Color.magenta);
        //g2.fillRect(rect.x, rect.y, rect.width, rect.height);
        g2.drawImage(this.pacman, rect.x, rect.y, this.radius * 2, this.radius * 2, null);
        //   g2.setColor(color);
        // g2.drawOval(rect.x, rect.y, this.radius * 2, this.radius * 2);

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

    public Rectangle getRect() {
        return rect;
    }


    public void setLocation(int xCenter, int yCenter) {
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.rect.setLocation(this.xCenter - radius, this.yCenter - radius);
    }


}




