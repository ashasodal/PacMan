package sodal.pacman.gui;

import sodal.pacman.entity.Entity;
import sodal.pacman.entity.enemy.Enemy;
import sodal.pacman.entity.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

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


    //gameLoop
    private static boolean isRunning;
    private int FPS = 60;
    private Thread gameLoop;


    public ThePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        //entities
        player = new Player(TILE_SIZE * 10, TILE_SIZE * 9, TILE_SIZE,TILE_SIZE, 1);
       redGhost = new Enemy(TILE_SIZE *10, TILE_SIZE*10, TILE_SIZE,TILE_SIZE,0);
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
        player.update();
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


    public static byte[] getDirection() {
        return direction;
    }


    public static Enemy getRedGhost() {
        return redGhost;
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

}
