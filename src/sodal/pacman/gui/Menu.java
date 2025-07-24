package sodal.pacman.gui;

import sodal.pacman.ui.UIManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Menu extends JPanel implements KeyListener {


    private static JFrame frame;


    private GamePanel gamePanel;


    private BufferedImage backgroundBuffer;


    //buttons
    private BufferedImage playBuffer;
    private BufferedImage quitBuffer;
    private BufferedImage highscoreBuffer;

    //highlights the current button
    private Rectangle buttonLight;


    public Menu(JFrame frame, int width, int height) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.YELLOW);

        //bufferimage
        this.backgroundBuffer = UIManager.createBuffer(width, height, "./res/image/menu/background.png");
        this.playBuffer = UIManager.createBuffer(GamePanel.getTileSize() * 3, GamePanel.getTileSize(), "./res/image/menu/play.png");
        this.quitBuffer = UIManager.createBuffer(GamePanel.getTileSize() * 3, GamePanel.getTileSize(), "./res/image/menu/quit.png");
        this.highscoreBuffer = UIManager.createBuffer(GamePanel.getTileSize() * 3, GamePanel.getTileSize(), "./res/image/menu/highscore.png");
        this.buttonLight = new Rectangle(GamePanel.getTileSize() * 7, GamePanel.getTileSize() * 11, GamePanel.getTileSize() * 3, GamePanel.getTileSize());


        this.setFocusable(true);
        this.addKeyListener(this);
        this.requestFocusInWindow();


        gamePanel = new GamePanel(frame, this);

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        paintAllBuffers(g2);
        g2.setColor(Color.magenta);
        g2.drawRect(buttonLight.x, buttonLight.y, buttonLight.width, buttonLight.height);
        g2.dispose();
    }


    private void paintAllBuffers(Graphics2D g2) {
        g2.drawImage(backgroundBuffer, 0, 0, null);
        g2.drawImage(playBuffer, GamePanel.getTileSize() * 7, GamePanel.getTileSize() * 11, null);
        g2.drawImage(highscoreBuffer, GamePanel.getTileSize() * 7, GamePanel.getTileSize() * 13, null);
        g2.drawImage(quitBuffer, GamePanel.getTileSize() * 7, GamePanel.getTileSize() * 15, null);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            pressButton();
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            //if not at last button
            if (buttonLight.y != GamePanel.getTileSize() * 15) {
                goDown();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            //if not at first button
            if (buttonLight.y != GamePanel.getTileSize() * 11) {
                goUp();
            }
        }
    }


    private void goUp() {
        buttonLight.y -= 2 * GamePanel.getTileSize();
        this.repaint();
    }

    private void goDown() {
        buttonLight.y += 2 * GamePanel.getTileSize();
        this.repaint();
    }


    private void pressButton() {
        //start button
        if (buttonLight.y == GamePanel.getTileSize() * 11) {
            startGame();
        }
        //instruction button
        else if (buttonLight.y == GamePanel.getTileSize() * 13) {
            System.out.println("highscore");
        }
        //highscore button
        else {
            System.out.println("quit");
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void startGame() {
        gamePanel.restart();
        UIManager.switchTo(frame, this, gamePanel);
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
}
