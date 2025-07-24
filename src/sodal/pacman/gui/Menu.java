package sodal.pacman.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

public class Menu extends JPanel implements KeyListener {


    private JFrame frame;


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
        this.backgroundBuffer = GamePanel.createBuffer(width, height, "./res/image/menu/background.png");
        this.playBuffer = GamePanel.createBuffer(GamePanel.getTileSize() * 3, GamePanel.getTileSize(), "./res/image/menu/play.png");
        this.quitBuffer = GamePanel.createBuffer(GamePanel.getTileSize() * 3, GamePanel.getTileSize(), "./res/image/menu/quit.png");
        this.highscoreBuffer = GamePanel.createBuffer(GamePanel.getTileSize() * 3, GamePanel.getTileSize(), "./res/image/menu/highscore.png");
        this.buttonLight = new Rectangle(GamePanel.getTileSize() * 7, GamePanel.getTileSize() * 11, GamePanel.getTileSize() * 3, GamePanel.getTileSize());


        this.setFocusable(true);
        this.addKeyListener(this);
        this.requestFocusInWindow();


        gamePanel = new GamePanel(frame,this);

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(backgroundBuffer, 0, 0, null);
        g2.drawImage(playBuffer, GamePanel.getTileSize() * 7, GamePanel.getTileSize() * 11, null);
        g2.drawImage(highscoreBuffer, GamePanel.getTileSize() * 7, GamePanel.getTileSize() * 13, null);
        g2.drawImage(quitBuffer, GamePanel.getTileSize() * 7, GamePanel.getTileSize() * 15, null);


        g2.setColor(Color.magenta);
        g2.drawRect(buttonLight.x, buttonLight.y, buttonLight.width, buttonLight.height);


        g2.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        //startbutton
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("ENTEREDDDDDDD");
            pressButton();
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            //if buttonligt is not at button
            if (buttonLight.y != GamePanel.getTileSize() * 15) {
                buttonLight.y += 2 * GamePanel.getTileSize();
                this.repaint();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (buttonLight.y != GamePanel.getTileSize() * 11) {
                System.out.println("UPPP!!!");
                buttonLight.y -= 2 * GamePanel.getTileSize();
                this.repaint();
            }
        }

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
        this.removeKeyListener(this);
        frame.remove(this);
        frame.add(gamePanel);
        gamePanel.restart();
        gamePanel.addKeyListener(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();

        frame.validate();
        frame.repaint();
        System.out.println("added blue");
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
