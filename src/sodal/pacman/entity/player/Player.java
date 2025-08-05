package sodal.pacman.entity.player;

import sodal.pacman.entity.Entity;
//import sodal.pacman.entity.enemy.Enemy;
import sodal.pacman.entity.food.Food;
import sodal.pacman.gui.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Player extends Entity {



    protected int score = 0;

    protected int xCenter, yCenter;
    private int initialXCenter, initialYCenter;
    protected int health = 3;
    protected int radius;
    private Rectangle rect;
    private GamePanel panel;

    private byte[] direction;

    //images

    private BufferedImage image;
    private BufferedImage pacman;
    private BufferedImage[] up = new BufferedImage[3];
    private BufferedImage[] down = new BufferedImage[3];

    private BufferedImage[] left = new BufferedImage[3];

    private BufferedImage[] right = new BufferedImage[3];


    private BufferedImage[] dead = new BufferedImage[12];

  // ANIMATION
    private final long PLAYER_FRAME_INTERVAL_MS = 100;   // Interval between image changes
    private long playerAnimationStartTime = -1;           // When animation started
    private boolean isPlayerUpAnimating, isPlayerDownAnimating, isPlayerLeftAnimating, isPlayerRightAnimating;            // Animation state flag


    public Player(int xCenter, int yCenter, int radius, int speed, GamePanel panel) {
        super(radius * 2, radius * 2, speed);
        this.xCenter = xCenter;
        this.yCenter = yCenter;
        this.initialXCenter = xCenter;
        this.initialYCenter = yCenter;
        this.radius = radius;
        this.panel = panel;

        //player direction
        direction = new byte[4];

        rect = new Rectangle(xCenter - radius, yCenter - radius, this.radius * 2, this.radius * 2);


        createBuffer();

        this.image = pacman;


    }


    private void createBuffer() {
        try {

            this.pacman = ImageIO.read(new File("./res/image/player/pacman.png"));

            for (int i = 0; i < 3; i++) {
                up[i] = ImageIO.read(new File("./res/image/player/up/up" + (i + 1) + ".png"));
                down[i] = ImageIO.read(new File("./res/image/player/down/down" + (i + 1) + ".png"));
                left[i] = ImageIO.read(new File("./res/image/player/left/left" + (i + 1) + ".png"));
                right[i] = ImageIO.read(new File("./res/image/player/right/right" + (i + 1) + ".png"));
            }

            for (int i = 0; i < dead.length; i++) {
                dead[i] = ImageIO.read(new File("./res/image/player/dead/dead" + (i + 1) + ".png"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void update() {
        move();
        worldCollision();
        foodCollision();
    }

    private void worldCollision() {
        //circle - rectangular collision
        for (Rectangle rect : GamePanel.getWorld()) {
            if (this.rect.intersects(rect)) {
                if (panel.circleRectCollision(rect)) {
                    //backtrack player until it doesn't collide with worldRect.
                    panel.backtrack(rect);
                }
            }
        }
    }

    private void foodCollision() {
        boolean eatFoodSound = false;
        for(Food food: GamePanel.getAllFood()) {
            Rectangle foodRect = new Rectangle(food.getX(), food.getY(), food.getWidth(), food.getHeight());
            if(!food.isEaten() && panel.circleRectCollision(foodRect)) {
                //make food invisible
                food.setSize((byte)0,(byte)0);
                score++;
                //play once when pacman hits more than one food at the same time
                if(!eatFoodSound) {
                    GamePanel.playSound("./res/sound/eat.wav",0, -10f);
                    eatFoodSound = true;
                }
                //max score
                if(score == 285) {
                    GamePanel.stopBackgroundSound();
                    GamePanel.handleGameOverState();
                }
            }
        }
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
       // System.out.println(rect.getLocation());
    }

    private void moveUp(int speed) {
        if (this.rect.y > 0) {
            this.yCenter -= speed;
        }
        //if player is not backtracking
        if (speed != 1) {
            upAnimation();
        }
    }

    private void moveDown(int speed) {
        if (this.rect.y < GamePanel.getHEIGHT() - 2 * GamePanel.getTileSize()) {
            this.yCenter += speed;
        }
        //if player is not backtracking
        if (speed != 1) {
            downAnimation();
        }
    }

    private void moveLeft(int speed) {
        if (this.rect.x > 0 && this.rect.y != 10 * GamePanel.getTileSize()) {
            this.xCenter -= speed;
        }

        if(this.rect.y == 10 * GamePanel.getTileSize()) {
            if(this.rect.x <= -GamePanel.getTileSize() /2) {
              this.xCenter = GamePanel.getWIDTH() - (GamePanel.getTileSize()/2) + radius;
              //this.speed = 0;
              //  System.out.println("speed: " + speed);
            }
            else {
                this.xCenter -= speed;
            }
        }
        //if player is not backtracking
        if (speed != 1) {
            leftAnimation();
        }
    }

    private void moveRight(int speed) {
        if (this.rect.x < GamePanel.getWIDTH() - GamePanel.getTileSize()  && this.rect.y != 10 * GamePanel.getTileSize()) {
            this.xCenter += speed;
        }

        if(this.rect.y == 10 * GamePanel.getTileSize()) {
            if(this.rect.x >= GamePanel.getWIDTH() - GamePanel.getTileSize()/2) {
                this.xCenter = -GamePanel.getTileSize()/2 + radius;
               // this.speed = 0;
            }
            else {
                this.xCenter += speed;
            }
        }

        //if player is not backtracking
        if (speed != 1) {
            rightAnimation();
        }
    }

    private void upAnimation() {

        isPlayerDownAnimating = false;
        isPlayerLeftAnimating = false;
        isPlayerRightAnimating = false;

        if (!isPlayerUpAnimating) {
            isPlayerUpAnimating = true;
            playerAnimationStartTime = System.currentTimeMillis();
        }

        long currentTime = System.currentTimeMillis() - playerAnimationStartTime;

        if (currentTime >= 0 && currentTime < PLAYER_FRAME_INTERVAL_MS) {
            this.image = up[0];
        } else if (currentTime >= PLAYER_FRAME_INTERVAL_MS && currentTime < 2 * PLAYER_FRAME_INTERVAL_MS) {
            this.image = up[1];
        } else if (currentTime >= 2 * PLAYER_FRAME_INTERVAL_MS && currentTime < 3 * PLAYER_FRAME_INTERVAL_MS) {
            this.image = up[2];
        } else if (currentTime >= 3 * PLAYER_FRAME_INTERVAL_MS) {
            isPlayerUpAnimating = false;
        }

    }


    private void downAnimation() {

        isPlayerUpAnimating = false;
        isPlayerLeftAnimating = false;
        isPlayerRightAnimating = false;

        if (!isPlayerDownAnimating) {
            isPlayerDownAnimating = true;
            playerAnimationStartTime = System.currentTimeMillis();
        }

        long currentTime = System.currentTimeMillis() - playerAnimationStartTime;

        if (currentTime >= 0 && currentTime < PLAYER_FRAME_INTERVAL_MS) {
            this.image = down[0];
        } else if (currentTime >= PLAYER_FRAME_INTERVAL_MS && currentTime < 2 * PLAYER_FRAME_INTERVAL_MS) {
            this.image = down[1];
        } else if (currentTime >= 2 * PLAYER_FRAME_INTERVAL_MS && currentTime < 3 * PLAYER_FRAME_INTERVAL_MS) {
            this.image = down[2];
        } else if (currentTime >= 3 * PLAYER_FRAME_INTERVAL_MS) {
            isPlayerDownAnimating = false;
        }

    }





    private void leftAnimation() {

        isPlayerUpAnimating = false;
        isPlayerDownAnimating = false;
        isPlayerRightAnimating = false;

        if (!isPlayerLeftAnimating) {
            isPlayerLeftAnimating = true;
            playerAnimationStartTime = System.currentTimeMillis();
        }

        long currentTime = System.currentTimeMillis() - playerAnimationStartTime;

        if (currentTime >= 0 && currentTime < PLAYER_FRAME_INTERVAL_MS) {
            this.image = left[0];
        } else if (currentTime >= PLAYER_FRAME_INTERVAL_MS && currentTime < 2 * PLAYER_FRAME_INTERVAL_MS) {
            this.image = left[1];
        } else if (currentTime >= 2 * PLAYER_FRAME_INTERVAL_MS && currentTime < 3 * PLAYER_FRAME_INTERVAL_MS) {
            this.image = left[2];
        } else if (currentTime >= 3 * PLAYER_FRAME_INTERVAL_MS) {
            isPlayerLeftAnimating = false;
        }

    }




    private void rightAnimation() {

        isPlayerUpAnimating = false;
        isPlayerDownAnimating = false;
        isPlayerLeftAnimating = false;

        if (!isPlayerRightAnimating) {
            isPlayerRightAnimating = true;
            playerAnimationStartTime = System.currentTimeMillis();
        }

        long currentTime = System.currentTimeMillis() - playerAnimationStartTime;

        if (currentTime >= 0 && currentTime < PLAYER_FRAME_INTERVAL_MS) {
            this.image = right[0];
        } else if (currentTime >= PLAYER_FRAME_INTERVAL_MS && currentTime < 2 * PLAYER_FRAME_INTERVAL_MS) {
            this.image = right[1];
        } else if (currentTime >= 2 * PLAYER_FRAME_INTERVAL_MS && currentTime < 3 * PLAYER_FRAME_INTERVAL_MS) {
            this.image = right[2];
        } else if (currentTime >= 3 * PLAYER_FRAME_INTERVAL_MS) {
            isPlayerRightAnimating = false;
        }

    }




    public void moveInOppositeDirection() {
        //BufferedImage temp = this.image;
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
        // this.image = temp;
        this.rect.setLocation(this.xCenter - radius, this.yCenter - radius);

    }


    @Override
    public void render(Graphics2D g2) {
       //  g2.setColor(Color.magenta);
        // g2.fillRect(rect.x, rect.y, rect.width, rect.height);
        g2.drawImage(this.image, rect.x, rect.y, this.radius * 2, this.radius * 2, null);
       // g2.setColor(Color.red);
        // g2.fillOval(rect.x, rect.y, this.radius * 2, this.radius * 2);

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


    public void resetDirectionArray() {
        Arrays.fill(direction, (byte) 0);
    }


    public void setPacManImage() {
        this.image = pacman;
    }


    public int getHealth() {
        return health;
    }

    public void decrementHealth() {
        this.health -= 1;
    }

    public BufferedImage[] getLeftBuffer() {
        return left;
    }


    public BufferedImage[] getDeadBuffer() {
        return dead;
    }


    public void setImage(BufferedImage image) {
        this.image = image;
    }


    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.rect.setSize(this.width, this.height);
    }


    public int getSpeed() {
        return speed;
    }


    public void resetMovementAnimations() {
        isPlayerUpAnimating = false;
        isPlayerDownAnimating = false;
        isPlayerLeftAnimating = false;
        isPlayerRightAnimating = false;
    }


    public void resetHealth() {
        this.health = 3;
    }

    public int getInitialXCenter() {
        return initialXCenter;
    }

    public int getInitialYCenter() {
        return initialYCenter;
    }


    public void resetToInitialState() {
        //put player in house
        this.setLocation(this.initialXCenter, this.initialYCenter);
        this.resetDirectionArray();
        this.resetMovementAnimations();
        this.setPacManImage();
        this.setSpeed(3);
        this.setSize(GamePanel.getTileSize(), GamePanel.getTileSize());
    }


    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }

}




