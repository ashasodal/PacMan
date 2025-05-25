package sodal.pacman.gui;

import sodal.pacman.entity.enemy.Enemy;
import sodal.pacman.entity.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ThePanel extends JPanel implements Runnable, KeyListener {

    private static final int TILE_SIZE = 30;

    private static final int numOfTilesWidth = 25;
    private static final int numOfTilesHeight = 15;
    private static final int WIDTH = numOfTilesWidth * TILE_SIZE;
    private static final int HEIGHT = numOfTilesHeight * TILE_SIZE;


    //entities

    //red ghost
    // private static Enemy redGhost;
    private Enemy[] enemies = new Enemy[4];

    //player
    private static Player player;


    private volatile boolean playerEnemyCollision = false;

    //gameLoop
    private static boolean isRunning;
    private int FPS = 60;
    private Thread gameLoop;


    //collision
    private long RESPAWN_DELAY_MS = 3000; // in ms

    private long collisionTimeStamp = -1;

    private static Rectangle[] world = new Rectangle[8];


    //scoreBoard
    private ScoreBoard scoreBoard;


    public ThePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        this.setLayout(null);
        //entities
        int radius = TILE_SIZE / 2;
        player = new Player(TILE_SIZE * 22 + radius, TILE_SIZE * 12 + radius, radius, 3, this);
        scoreBoard = new ScoreBoard(player);
        //lister
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();


        //enemies
        enemies[0] = new Enemy(TILE_SIZE * 20, 3 * TILE_SIZE, TILE_SIZE, TILE_SIZE, 1, "./src/sodal/pacman/entity/enemy/image/blinky.png", "up", 60);
        enemies[1] = new Enemy(TILE_SIZE * 20, 3 * TILE_SIZE, TILE_SIZE, TILE_SIZE, 1, "./src/sodal/pacman/entity/enemy/image/clyde.png", "down", 90);
        enemies[2] = new Enemy(TILE_SIZE * 20, 3 * TILE_SIZE, TILE_SIZE, TILE_SIZE, 1, "./src/sodal/pacman/entity/enemy/image/inky.png", "left", 120);
        enemies[3] = new Enemy(TILE_SIZE * 20, 3 * TILE_SIZE, TILE_SIZE, TILE_SIZE, 1, "./src/sodal/pacman/entity/enemy/image/pinky.png", "right", 180);


        //worldRectangles

        //cross
        Rectangle rect1 = new Rectangle(9 * TILE_SIZE, 7 * TILE_SIZE, 7 * TILE_SIZE, TILE_SIZE);
        Rectangle rect2 = new Rectangle(12 * TILE_SIZE, 4 * TILE_SIZE, TILE_SIZE, 7 * TILE_SIZE);

        //pacman house
        Rectangle rect3 = new Rectangle(21 * TILE_SIZE, 13 * TILE_SIZE, 3 * TILE_SIZE, TILE_SIZE);
        Rectangle rect4 = new Rectangle(21 * TILE_SIZE, 12 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        Rectangle rect5 = new Rectangle(23 * TILE_SIZE, 12 * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        //shield
        Rectangle rect6 = new Rectangle(TILE_SIZE, 2 * TILE_SIZE, 5 * TILE_SIZE, TILE_SIZE);


        //L

        Rectangle rect7 = new Rectangle(0, 7 * TILE_SIZE, TILE_SIZE, 6 * TILE_SIZE);
        Rectangle rect8 = new Rectangle(TILE_SIZE, 12 * TILE_SIZE, 10 * TILE_SIZE, TILE_SIZE);


        world[0] = rect1;
        world[1] = rect2;
        world[2] = rect3;
        world[3] = rect4;
        world[4] = rect5;
        world[5] = rect6;
        world[6] = rect7;
        world[7] = rect8;


        //game loop
        gameLoop = new Thread(this);
        gameLoop.start();
    }


    private void update() {
        //due to backtracking, PLAYER SHOULD UPDATE FIRST
        if (!playerEnemyCollision) {
            for (Enemy enemy : enemies) {
                enemy.update();
            }
            player.update();
        }
        checkCollision();
    }


    /**
     * collision between player and enemy
     */
    public void checkCollision() {
        if (!playerEnemyCollision) {
            checkPlayerEnemyCollision();
            return;
        }
        handlePlayerEnemyCollision();

    }

    public void checkPlayerEnemyCollision() {
        for (Enemy enemy : enemies) {
            if (player.getRect().intersects(enemy.getEnemyRect())) {
                for (Rectangle rect : enemy.getRect()) {
                    //collision!!!
                    if (circleRectCollision(rect)) {
                        playerEnemyCollision = true;
                        player.decrementHealth();
                        collisionTimeStamp = System.currentTimeMillis();
                        handlePlayerEnemyCollision();
                        return;
                    }
                }
            }
        }
    }


    private void handlePlayerEnemyCollision() {
        //game over.
        if (player.getHealth() == 0) {
            gameOver();
        } else {
            collisionDelay();
        }
    }

    private void collisionDelay() {
        if (System.currentTimeMillis() - collisionTimeStamp >= RESPAWN_DELAY_MS) {
            //dead animation
            deadAnimation();
            //animation has finished
            if (player.getDeadCounter() == 120) {
                respawn();
            }
        }
    }

    private void respawn() {
        //put player in house
        player.setLocation(22 * TILE_SIZE + player.getRadius(), 12 * TILE_SIZE + player.getRadius());
        //direction = [0,0,0,0]
        player.resetDirectionArray();
        player.setPacManImage();
        //put enemies in initial position
        for (Enemy enemy : enemies) {
            enemy.setLocation(enemy.getInitialX(), enemy.getInitialY());
            enemy.setCounterToZero();
            enemy.initialDirection();
        }
        playerEnemyCollision = false;
        player.resetDeadCounter();
    }

    private void deadAnimation() {
        int delay = player.getPlayerDeadDelay();
        for (int i = 0; i < player.getDeadBuffer().length; i++) {
            if (player.getDeadCounter() >= i * delay && player.getDeadCounter() < (i + 1) * delay) {
                player.setImage(player.getDeadBuffer()[i]);
                System.out.println("-------");
                System.out.println("playerDeadCounter: " + player.getDeadCounter());
                player.incrementDeadCounter();
                System.out.println("playerDeadCounter incremented: " + player.getDeadCounter());
                System.out.println("-------");
                return;
            }
        }
    }

    public void gameOver() {

        if (System.currentTimeMillis() - collisionTimeStamp >= RESPAWN_DELAY_MS) {
            //make enemies invisible
            for (Enemy enemy : enemies) {
                enemy.setSize(0,0);
                enemy.setSpeed(0);
            }

            deadAnimation();
            
            player.resetDirectionArray();
            this.setFocusable(false);
            player.setSpeed(0);

        }
    }


    public boolean circleRectCollision(Rectangle rect) {
        double distance = getDistance(rect);
        //collision!!!
        if (distance < player.getRadius()) {
            return true;
        }
        return false;
    }


    public double getDistance(Rectangle rect) {
        double closestRectX = clamp(rect.x, rect.x + rect.width, player.getxCenter());
        double closestRectY = clamp(rect.y, rect.y + rect.height, player.getyCenter());
        return distance(closestRectX, closestRectY);
    }


    public void backtrack(Rectangle rect) {
        double playerRadius = player.getRadius();
        double distance = getDistance(rect);
        while (distance < playerRadius) {
            player.moveInOppositeDirection();
            distance = getDistance(rect);
        }
    }


    private double distance(double closestRectX, double closestRectY) {
        double deltaX = Math.abs(player.getxCenter() - closestRectX);
        double deltaY = Math.abs(player.getyCenter() - closestRectY);
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }


    public double clamp(double min, double max, double value) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }


    public Thread getGameLoop() {
        return gameLoop;
    }


    private void grid(Graphics2D g2) {
        g2.setColor(Color.gray);
        int x = 0;
        int y = 0;
        for (int i = 0; i < this.numOfTilesHeight; i++) {
            for (int j = 0; j < this.numOfTilesWidth; j++) {
                g2.drawRect(x, y, TILE_SIZE, TILE_SIZE);
                x += TILE_SIZE;
            }
            x = 0;
            y += TILE_SIZE;
        }
    }


    public static int getTileSize() {
        return TILE_SIZE;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //paint


        g2.fillRect(0, 0, WIDTH, HEIGHT);
        //grid(g2);

        player.render(g2);
        for (Enemy enemy : enemies) {
            enemy.render(g2);
        }
        scoreBoard.render(g2);
        renderWorld(g2);


        //draw over the lines in world
        g2.setColor(Color.BLACK);
        //L
        g2.drawLine(TILE_SIZE, 12 * TILE_SIZE + 1, TILE_SIZE, 13 * TILE_SIZE - 1);

        //house
        g2.drawLine(21 * TILE_SIZE + 1, 13 * TILE_SIZE, 22 * TILE_SIZE - 1, 13 * TILE_SIZE);
        g2.drawLine(23 * TILE_SIZE + 1, 13 * TILE_SIZE, 24 * TILE_SIZE - 1, 13 * TILE_SIZE);

        //cross
        //left
        g2.drawLine(12 * TILE_SIZE, 7 * TILE_SIZE + 1, 12 * TILE_SIZE, 8 * TILE_SIZE - 1);
        //top
        g2.drawLine(12 * TILE_SIZE + 1, 7 * TILE_SIZE, 13 * TILE_SIZE - 1, 7 * TILE_SIZE);
        //right
        g2.drawLine(13 * TILE_SIZE, 7 * TILE_SIZE + 1, 13 * TILE_SIZE, 8 * TILE_SIZE - 1);
        //bottom
        g2.drawLine(12 * TILE_SIZE + 1, 8 * TILE_SIZE, 13 * TILE_SIZE - 1, 8 * TILE_SIZE);


        g2.dispose();
    }


    public void renderWorld(Graphics2D g2) {
        g2.setColor(Color.blue);
        for (Rectangle r : world) {
            g2.drawRect(r.x, r.y, r.width, r.height);
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    private void switchDirection(int index) {
        for (int i = 0; i < player.getDirectionArray().length; i++) {
            if (i == index) {
                player.getDirectionArray()[index] = 1;
                continue;
            }
            player.getDirectionArray()[i] = 0;
        }
    }


    /**
     * 0 -> up, 1 -> down, 2 -> left, 3 -> right
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_UP) {
            switchDirection(0);
        } else if (k == KeyEvent.VK_DOWN) {
            switchDirection(1);
        } else if (k == KeyEvent.VK_LEFT) {
            switchDirection(2);
        } else if (k == KeyEvent.VK_RIGHT) {
            switchDirection(3);
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }


    //game loop.
    @Override
    public void run() {

        isRunning = true;
        double drawInterval = 1000_000_000.0 / FPS; // 0.01666 seconds.
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        //display fps variables.
        long timer = 0;
        int drawCount = 0;

        while (isRunning) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            //displays FPS.
            if (timer >= 1_000_000_000) {
                // System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }


    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }


    public static Rectangle[] getWorld() {
        return world;
    }

}
