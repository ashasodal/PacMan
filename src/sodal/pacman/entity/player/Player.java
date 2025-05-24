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

    private byte[] direction;

    //images

    private BufferedImage image;
    private BufferedImage pacman;
    private BufferedImage[] up = new BufferedImage[3];
    private BufferedImage[] down = new BufferedImage[3];

    private BufferedImage[] left = new BufferedImage[3];

    private BufferedImage[] right = new BufferedImage[3];

    //animation


    private int counterUp, counterDown, counterLeft, counterRight;


    public Player(int xCenter, int yCenter, int radius, int speed, ThePanel panel) {
        super(radius * 2, radius * 2, speed);
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.radius = radius;
        this.panel = panel;

        //player direction
        direction = new byte[4];

        rect = new Rectangle(xCenter - radius, yCenter - radius, this.radius * 2, this.radius * 2);


        createPacManBuffer();
        createUpBuffer();
        createDownBuffer();
        createLeftBuffer();
        createRightBuffer();

        this.image = pacman;


    }


    private void createUpBuffer() {
        try {
            up[0] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/up/up1.png"));
            up[1] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/up/up2.png"));
            up[2] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/up/up3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void createRightBuffer() {
        try {
            right[0] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/right/right1.png"));
            right[1] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/right/right2.png"));
            right[2] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/right/right3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void createLeftBuffer() {
        try {
            left[0] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/left/left1.png"));
            left[1] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/left/left2.png"));
            left[2] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/left/left3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createDownBuffer() {
        try {
            down[0] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/down/down1.png"));
            down[1] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/down/down2.png"));
            down[2] = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/down/down3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPacManBuffer() {
        try {
            this.pacman = ImageIO.read(new File("./src/sodal/pacman/entity/player/image/pacman.png"));
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

        upAnimation();
    }

    private void upAnimation() {
        if (counterUp >= 0 && counterUp <= 10) {
            this.image = up[0];
            counterUp++;
            return;

        } else if (counterUp > 10 && counterUp <= 20) {
            this.image = up[1];
            counterUp++;
            return;

        } else if (counterUp > 20 && counterUp <= 30) {
            this.image = up[2];
            counterUp++;
            return;
        }

        counterUp = 0;
    }


    private void downAnimation() {
        if (counterDown >= 0 && counterDown <= 10) {
            this.image = down[0];
            counterDown++;
            return;

        } else if (counterDown > 10 && counterDown <= 20) {
            this.image = down[1];
            counterDown++;
            return;

        } else if (counterDown > 20 && counterDown <= 30) {
            this.image = down[2];
            counterDown++;
            return;
        }
        counterDown = 0;
    }

    private void moveDown(int speed) {
        if (this.rect.y < ThePanel.getHEIGHT() - 2 * ThePanel.getTileSize()) {
            this.yCenter += speed;
        }
        downAnimation();
    }

    private void moveLeft(int speed) {
        if (this.rect.x > 0) {
            this.xCenter -= speed;
        }

        leftAnimation();


    }

    private void leftAnimation() {

        if (counterLeft >= 0 && counterLeft <= 10) {
            this.image = left[0];
            counterLeft++;
            return;

        } else if (counterLeft > 10 && counterLeft <= 20) {
            this.image = left[1];
            counterLeft++;
            return;

        } else if (counterLeft > 20 && counterLeft <= 30) {
            this.image = left[2];
            counterLeft++;
            return;
        }
        counterLeft = 0;

    }

    private void moveRight(int speed) {
        if (this.rect.x < ThePanel.getWIDTH() - ThePanel.getTileSize()) {
            this.xCenter += speed;
        }

        rightAnimation();
    }


    private void rightAnimation() {
        if (counterRight >= 0 && counterRight <= 10) {
            this.image = right[0];
            counterRight++;
            return;

        } else if (counterRight > 10 && counterRight <= 20) {
            this.image = right[1];
            counterRight++;
            return;

        } else if (counterRight > 20 && counterRight <= 30) {
            this.image = right[2];
            counterRight++;
            return;
        }
        counterRight = 0;

    }

    private void move() {
        byte[] dir = this.direction;
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
        BufferedImage temp = this.image;
        byte dir[] = this.direction;
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
        this.image = temp;
        this.rect.setLocation(this.xCenter - radius, this.yCenter - radius);
    }


    @Override
    public void render(Graphics2D g2) {
        // g2.setColor(Color.magenta);
        //g2.fillRect(rect.x, rect.y, rect.width, rect.height);
        g2.drawImage(this.image, rect.x, rect.y, this.radius * 2, this.radius * 2, null);
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

    public byte[] getDirectionArray() {
        return direction;
    }




}




