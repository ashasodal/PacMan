package sodal.pacman.gui;

import sodal.pacman.entity.enemy.Enemy;
import sodal.pacman.entity.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ThePanel extends JPanel implements Runnable, KeyListener {

    private static final int TILE_SIZE = 30;

    private static final int numOfTilesWidth = 25;
    private static final int numOfTilesHeight = 15;
    private static final int WIDTH = numOfTilesWidth * TILE_SIZE;
    private static final int HEIGHT = numOfTilesHeight * TILE_SIZE;

    private static boolean gameOver = false;


    //entities

    //red ghost
    private static Enemy redGhost;

    //player
    private static Player player;
    private static byte[] direction;
    private static volatile boolean checkCollision = false;

    private volatile boolean playerEnemyCollision = false;

    //gameLoop
    private static boolean isRunning;
    private int FPS = 60;
    private Thread gameLoop;


    private static Rectangle[] world = new Rectangle[6];


    public ThePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        //entities
        player = new Player(TILE_SIZE * 2 - (TILE_SIZE / 2), TILE_SIZE - (TILE_SIZE / 2), TILE_SIZE / 2, 3, this);
        redGhost = new Enemy(TILE_SIZE * 10, TILE_SIZE * 1, TILE_SIZE, TILE_SIZE, 0);
        //lister
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();


        //player direction
        direction = new byte[4];

        // readWorld();

        //worldRectangles

        //cross
        Rectangle rect1 = new Rectangle(9 * TILE_SIZE, 7 * TILE_SIZE, 7 * TILE_SIZE, TILE_SIZE);
        Rectangle rect2 = new Rectangle(12 * TILE_SIZE, 4 * TILE_SIZE,  TILE_SIZE, 7 * TILE_SIZE);

        //pacman house
        Rectangle rect3 = new Rectangle(21 * TILE_SIZE, 13 * TILE_SIZE,  3* TILE_SIZE,  TILE_SIZE);
        Rectangle rect4 = new Rectangle(21 * TILE_SIZE, 12 * TILE_SIZE,   TILE_SIZE,  TILE_SIZE);
        Rectangle rect5 = new Rectangle(23 * TILE_SIZE, 12 * TILE_SIZE,   TILE_SIZE,  TILE_SIZE);

        //shield
        Rectangle rect6 = new Rectangle( 2 * TILE_SIZE, 2 *  TILE_SIZE,    5* TILE_SIZE,  TILE_SIZE);
        world[0] = rect1;
        world[1] = rect2;
        world[2] = rect3;
        world[3] = rect4;
        world[4] = rect5;
        world[5] = rect6;


        //game loop
        gameLoop = new Thread(this);
        gameLoop.start();
    }


    private void update() {
        if (!gameOver) {
            //due to backtracking, PLAYER SHOULD UPDATE FIRST
            redGhost.update();
            player.update();
            checkCollision();
        }


    }


    /**
     * collision between player and enemy
     */
    public void checkCollision() {

        checkCollision = true;
        playerEnemyCollision();
        checkCollision = false;

        if (playerEnemyCollision) {
            //stop the game.
            gameOver();
        }
    }


    private void revertEnemyIfCollides() {


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


    public void playerEnemyCollision() {
        if(player.getRect().intersects(redGhost.getEnemyRect())) {
            for (Rectangle rect : redGhost.getRect()) {
                //collision!!!
                if (circleRectCollision(rect)) {
                    playerEnemyCollision = true;
                    System.out.println(" Enemy Collision");
                    // player.setColor(Color.GREEN);
                    backtrack( rect);
                }
            }

        }

    }


    public void gameOver() {
        gameOver = true;
        this.setFocusable(false);
        player.setSpeed(0);
        redGhost.setSpeed(0);

    }


    /**
     * checks if the player was moving when colliding with enemy.
     *
     * @return
     */
    private boolean playerMoving() {
        for (int i = 0; i < direction.length; i++) {
            if (direction[i] == 1) {
                return true;
            }
        }
        return false;
    }


    public void backtrack(Rectangle rect) {
        double playerRadius = player.getRadius();
        boolean playerMoving = playerMoving();
       double distance = getDistance(rect);
        while (distance < playerRadius) {
            if (playerMoving) {
                player.moveInOppositeDirection();
            } else {
                redGhost.moveInOppositeDirection();
            }
            distance = getDistance(rect);
        }
        System.out.println("distance: " + distance);
        System.out.println("radius: " + playerRadius);
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
        g2.setColor(Color.BLACK);
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
        grid(g2);
        redGhost.render(g2);
        player.render(g2);
        renderWorld(g2);
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
        for (int i = 0; i < direction.length; i++) {
            if (i == index) {
                direction[index] = 1;
                continue;
            }
            direction[i] = 0;
        }
    }


    /**
     * 0 -> up, 1 -> down, 2 -> left, 3 -> right
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (!checkCollision) {
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!checkCollision) {
            int k = e.getKeyCode();
            if (k == KeyEvent.VK_UP) {
                direction[0] = 0;
            } else if (k == KeyEvent.VK_DOWN) {
                direction[1] = 0;
            } else if (k == KeyEvent.VK_LEFT) {
                direction[2] = 0;
            } else if (k == KeyEvent.VK_RIGHT) {
                direction[3] = 0;
            }
        }
    }


    public static byte[] getDirection() {
        return direction;
    }


  /*  public static Enemy getRedGhost() {
        return redGhost;
    }*/

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


    public static boolean getCheckCollision() {
        return checkCollision;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static boolean getGameOver() {
        return gameOver;
    }

    public static Rectangle[] getWorld() {
        return world;
    }

}
