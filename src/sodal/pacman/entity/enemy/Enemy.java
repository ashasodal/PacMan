package sodal.pacman.entity.enemy;

import sodal.pacman.entity.Entity;
import sodal.pacman.gui.ThePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Enemy extends Entity {

    private Rectangle[] rect;
    protected int x, y;
    protected int initialX, initialY;

    private byte[] direction;


    private static Random rand;


    private long CHANGE_DIRECTION_MS;
    private long startTime = -1;

    private Rectangle enemyRect;


    private BufferedImage image;

    public Enemy(int x, int y, int width, int height, int speed, String path, int delay) {
        super(width, height, speed);
        this.x = x;
        this.y = y;
        this.initialX = x;
        this.initialY = y;
        this.CHANGE_DIRECTION_MS = delay;
        createRectangles();
        createBuffer(path);
        direction = new byte[4];
        rand = new Random();
        randomDir();
    }

    private void createBuffer(String path) {
        try {
            this.image = ImageIO.read(new File(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createRectangles() {
        enemyRect = new Rectangle(x, y, width, height);
        rect = new Rectangle[5];
       /* rect[0] = new Rectangle(this.x, this.y, this.width *3, this.height*2);
        rect[1] = new Rectangle(this.x + this.width/2, this.y -this.height , this.width *2, this.height);*/
        rect[0] = new Rectangle(this.x, this.y + 11, this.width, this.height - 11);
        rect[1] = new Rectangle(this.x + 2, this.y + 6, this.width - 4, 5);
        rect[2] = new Rectangle(this.x + 4, this.y + 4, this.width - 8, 2);
        rect[3] = new Rectangle(this.x + 6, this.y + 2, this.width - 12, 2);
        rect[4] = new Rectangle(this.x + 10, this.y, this.width - 20, 2);


    }



    public void moveInOppositeDirection(int width, int height) {
        if (direction[0] == 1) {
            moveDown(height);
        } else if (direction[1] == 1) {
            moveUp(height);
        } else if (direction[2] == 1) {
            moveRight(width);
        } else if (direction[3] == 1) {
            moveLeft(width);
        }
        enemyRect.setLocation(this.x, this.y);
    }


    private void moveUp(int speed) {
        if (y > 0) {
            y -= speed;
            for (int i = 0; i < rect.length; i++) {
                this.rect[i].y -= speed;
            }
        }
    }

    private void moveDown(int speed) {
        if (y < ThePanel.getHEIGHT() - 2 * ThePanel.getTileSize()) {
            y += speed;
            for (int i = 0; i < rect.length; i++) {
                this.rect[i].y += speed;
            }
        }
    }

    private void moveLeft(int speed) {
        if (x > 0) {
            x -= speed;
            for (int i = 0; i < rect.length; i++) {
                this.rect[i].x -= speed;
            }
        }

    }

    private void moveRight(int speed) {
        if (x < ThePanel.getWIDTH() - ThePanel.getTileSize()) {
            x += speed;
            for (int i = 0; i < rect.length; i++) {
                this.rect[i].x += speed;
            }
        }
    }

    public void move() {
        if (direction[0] == 1) {
            moveUp(speed);
        } else if (direction[1] == 1) {
            moveDown(speed);
        } else if (direction[2] == 1) {
            moveLeft(speed);
        } else if (direction[3] == 1) {
            moveRight(speed);
        }
        enemyRect.setLocation(this.x, this.y);
    }


    public Rectangle[] getRect() {
        return rect;
    }


    @Override
    public void update() {
        move();
        worldCollision();
        moveRandom();
    }


    public void worldCollision() {
        for (Rectangle rect : ThePanel.getWorld()) {
            if (enemyRect.intersects(rect)) {
                Rectangle intersection = enemyRect.intersection(rect);
                moveInOppositeDirection(intersection.width, intersection.height);
            }
        }
    }


    private void resetDir() {
        for (int i = 0; i < direction.length; i++) {
            if (direction[i] == 1) {
                direction[i] = 0;
                return;
            }
        }
    }

    private void moveRandom() {
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }
        long deltaTime = System.currentTimeMillis() - startTime;
        if (deltaTime >= CHANGE_DIRECTION_MS) {
            randomDir();
            startTime = -1;
        }
    }

    public Rectangle getEnemyRect() {
        return enemyRect;
    }

    @Override
    public void render(Graphics2D g2) {
        // g2.setColor(new Color(123,56,88));
        // g2.fillRect(this.enemyRect.x,this.enemyRect.y,this.enemyRect.width,this.enemyRect.height);
        g2.drawImage(this.image, this.x, this.y, this.width, this.height, null);
        // this.paintRect(g2);
    }


    public void paintRect(Graphics2D g2) {


        // Set a semi-transparent blue color (alpha range: 0.0f - fully transparent to 1.0f - fully opaque)
        float alpha = 1.0f; // 50% transparent
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(255, 200, 100, (int) (255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[0].x, this.rect[0].y, this.rect[0].width, this.rect[0].height);


        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(3, 100, 1, (int) (255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[1].x, this.rect[1].y, this.rect[1].width, this.rect[1].height);


        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(3, 20, 100, (int) (255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[2].x, this.rect[2].y, this.rect[2].width, this.rect[2].height);


        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(3, 100, 1, (int) (255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[3].x, this.rect[3].y, this.rect[3].width, this.rect[3].height);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(180, 100, 200, (int) (255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[4].x, this.rect[4].y, this.rect[4].width, this.rect[4].height);


// Optional: reset composite to full opacity for other drawings
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));


    }


    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        enemyRect.setLocation(this.x, this.y);
        rect[0].setLocation(this.x, this.y + 11);
        rect[1].setLocation(this.x + 2, this.y + 6);
        rect[2].setLocation(this.x + 4, this.y + 4);
        rect[3].setLocation(this.x + 6, this.y + 2);
        rect[4].setLocation(this.x + 10, this.y);
    }


    public void resetStartTime() {
        startTime = -1;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.enemyRect.setSize(width, height);
        rect[0].setSize(this.width, this.height - 11);
        rect[1].setSize(this.width - 4, 5);
        rect[2].setSize(this.width - 8, 2);
        rect[3].setSize(this.width - 12, 2);
        rect[4].setSize(this.width - 20, 2);
    }


    public void resetToInitialState() {
        this.setLocation(this.initialX, this.initialY);
        this.setSize(ThePanel.getTileSize(), ThePanel.getTileSize());
        this.resetStartTime();
        this.randomDir();
        this.setSpeed(1);
    }

    private void randomDir() {
        resetDir();
        direction[rand.nextInt(4)] = 1;
    }

}



