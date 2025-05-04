package sodal.pacman.gui;

import sodal.pacman.entity.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ThePanel extends JPanel implements Runnable, KeyListener {

    private static final int TILE_SIZE = 35;

    private static final int numOfTilesWidth = 20;
    private static final int numOfTilesHeight = 14;
    private final int WIDTH = numOfTilesWidth * TILE_SIZE;
    private final int HEIGHT = numOfTilesHeight * TILE_SIZE;


    //entities
    private Player player;


    //keyListener
    private static boolean up, down, left, right;


    //gameLoop
    private static boolean isRunning;
    private int FPS = 60;
    private Thread gameLoop;


    public ThePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        //entities
        player = new Player(TILE_SIZE * 3, TILE_SIZE * 5, 3);
        //lister
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();

        //game loop
        gameLoop = new Thread(this);


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
        player.render(g2);

        g2.dispose();

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        if (k == KeyEvent.VK_UP) {
            up = true;
           down = false;
            left = false;
            right = false;
        } else if (k == KeyEvent.VK_DOWN) {
            down = true;
            up = false;
            left = false;
            right = false;
        } else if (k == KeyEvent.VK_LEFT) {
            left = true;
            right = false;
            up = false;
            down = false;
        } else if (k == KeyEvent.VK_RIGHT) {
            right = true;
            left = false;
            up = false;
            down = false;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();

        if (k == KeyEvent.VK_UP) {
            up = false;

        } else if (k == KeyEvent.VK_DOWN) {
            down = false;

        } else if (k == KeyEvent.VK_LEFT) {
            left = false;

        } else if (k == KeyEvent.VK_RIGHT) {
            right = false;

        }


    }


    public static boolean getUp() {
        return up;
    }

    public static boolean getDown() {
        return down;
    }

    public static boolean getLeft() {
        return left;
    }

    public static boolean getRight() {
        return right;
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
