package sodal.pacman.gui;

import sodal.pacman.entity.enemy.Enemy;
import sodal.pacman.entity.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class ThePanel extends JPanel implements Runnable, KeyListener {

    private static final int TILE_SIZE = 30;

    private static final int numOfTilesWidth = 30;
    private static final int numOfTilesHeight = 15;
    private final int WIDTH = numOfTilesWidth * TILE_SIZE;
    private final int HEIGHT = numOfTilesHeight * TILE_SIZE;


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


    public ThePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        //entities
        player = new Player(TILE_SIZE * 2 - (TILE_SIZE / 2), TILE_SIZE  - (TILE_SIZE / 2), TILE_SIZE / 2, 3);
        redGhost = new Enemy(TILE_SIZE * 10, TILE_SIZE*7, TILE_SIZE, TILE_SIZE, 1);
        //lister
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();


        //player direction
        direction = new byte[4];


        //game loop
        gameLoop = new Thread(this);
        gameLoop.start();
    }


    private void update() {
        //due to backtracking, PLAYER SHOULD UPDATE FIRST
        redGhost.update();
        player.update();
        checkCollision();

    }


    public byte[] copyArray(byte[] arr) {
        byte[] copy = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        return copy;
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


    private void playerEnemyCollision() {
        int xCenter = player.getxCenter();
        int yCenter = player.getyCenter();
        //check if player is colliding with any of the enemy rectangles
        //circle-rectangle collision
        for (int i = 0; i < redGhost.getRect().length; i++) {
            Rectangle rect = redGhost.getRect()[i];
            double closestRectX = clamp(rect.x, rect.x + rect.width, xCenter);
            double closestRectY = clamp(rect.y, rect.y + rect.height, yCenter);
            double distance = distance(closestRectX, closestRectY);
            //collision!!!
            if (distance < player.getRadius()) {
                playerEnemyCollision = true;
                System.out.println("Collision");
                // player.setColor(Color.GREEN);
                backtrack(distance, rect);
            } else {
                // player.setColor(Color.RED);
            }
        }
    }


    public void gameOver() {
       /* try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println("game over!!!");
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


    public void backtrack(double distance, Rectangle rect) {
        double playerRadius = player.getRadius();
        boolean playerMoving = playerMoving();
        while (distance < playerRadius) {
            if (playerMoving) {
                player.moveInOppositeDirection();
            } else {
                redGhost.moveInOppositeDirection();
            }
            double closestRectX = clamp(rect.x, rect.x + rect.width, player.getxCenter());
            double closestRectY = clamp(rect.y, rect.y + rect.height, player.getyCenter());
            distance = distance(closestRectX, closestRectY);


        }
        System.out.println("distance: " + distance);
        System.out.println("radius: " + playerRadius);
    }


    private double distance(double closestRectX, double closestRectY) {
        double deltaX = Math.abs(player.getxCenter() - closestRectX);
        double deltaY = Math.abs(player.getyCenter() - closestRectY);
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }


    private double clamp(double min, double max, double value) {
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


        g2.dispose();

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

}
