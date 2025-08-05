package sodal.pacman.gui;

import sodal.pacman.ui.UIManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu extends JPanel implements KeyListener {


    private GamePanel gamePanel;
    private BufferedImage backgroundBuffer;
    //buttons
    private BufferedImage playBuffer;
    private BufferedImage quitBuffer;
    private BufferedImage highscoreBuffer;

    private BufferedImage backBuffer;
    //highlights the current button
    private Rectangle buttonLight;

    private boolean showHighScore = false;

    private String time;
    private String score;


    public Menu(JFrame frame, int width, int height) {
        gamePanel = new GamePanel(frame, this);
        readHighScoreFile();

        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.YELLOW);

        //bufferimage
        this.backgroundBuffer = UIManager.createBuffer(width, height, "./res/image/menu/background.png");
        this.playBuffer = UIManager.createBuffer(GamePanel.getTileSize() * 3, GamePanel.getTileSize(), "./res/image/menu/play.png");
        this.quitBuffer = UIManager.createBuffer(GamePanel.getTileSize() * 3, GamePanel.getTileSize(), "./res/image/menu/quit.png");
        this.highscoreBuffer = UIManager.createBuffer(GamePanel.getTileSize() * 3, GamePanel.getTileSize(), "./res/image/menu/highscore.png");
        this.buttonLight = new Rectangle(GamePanel.getTileSize() * 7, GamePanel.getTileSize() * 11, GamePanel.getTileSize() * 3, GamePanel.getTileSize());
        this.backBuffer = UIManager.createBuffer(GamePanel.getTileSize() * 3, GamePanel.getTileSize(), "./res/image/menu/back.png");

        this.setFocusable(true);
        this.addKeyListener(this);
        this.requestFocusInWindow();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if (showHighScore) {
            showHighScore(g2);
            return;
        }
        paintAllBuffers(g2);
        g2.setColor(Color.GREEN);
        g2.drawRect(buttonLight.x, buttonLight.y, buttonLight.width, buttonLight.height);
        g2.dispose();
    }

    private void showHighScore(Graphics2D g2) {
        renderHighScoreBackground(g2);
        BufferedImage boardBuffer = gamePanel.getBoardBuffer();
        g2.drawImage(boardBuffer, (GamePanel.getWIDTH() - boardBuffer.getWidth()) / 2, GamePanel.getTileSize() * 5, null);
        g2.drawImage(backBuffer, GamePanel.getTileSize() * 7, GamePanel.getTileSize() * 15, null);
        g2.setColor(Color.GREEN);
        g2.drawRect(buttonLight.x, buttonLight.y, buttonLight.width, buttonLight.height);
        //gamePanel.renderText(g2, "HIGH SCORE", GamePanel.getTileSize() * 6, ScoreBoard.getTextColor());
        if (score.equals("No high score yet.")) {
            gamePanel.renderText(g2, score, GamePanel.getTileSize() * 8, ScoreBoard.getTextColor());
        } else {
            gamePanel.renderText(g2, "SCORE: " + score, GamePanel.getTileSize() * 8, ScoreBoard.getTextColor());
            gamePanel.renderText(g2, "TIME TAKEN: " + time, GamePanel.getTileSize() * 9, ScoreBoard.getTextColor());
        }
        g2.dispose();
    }


    private void renderHighScoreBackground(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GamePanel.getWIDTH(), GamePanel.getHEIGHT());
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
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            //if not at last button
            if (buttonLight.y != GamePanel.getTileSize() * 15) {
                goDown();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
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
        //back button
        if (showHighScore) {
            showHighScore = false;
            //buttonlight on highscore button
            buttonLight.y = GamePanel.getTileSize() * 13;
            this.repaint();
        }

        //start button
        else if (buttonLight.y == GamePanel.getTileSize() * 11) {
            startGame();
        }
        //high score  button
        else if (buttonLight.y == GamePanel.getTileSize() * 13) {
            System.out.println("highscore");
            showHighScore = true;
            buttonLight.y = GamePanel.getTileSize() * 15;
            this.repaint();
        }
        //quit button
        else {
            System.out.println("quit");
            TheFrame.getFrame().dispatchEvent(new WindowEvent(TheFrame.getFrame(), WindowEvent.WINDOW_CLOSING));
        }
    }

    private void startGame() {
        gamePanel.initializeGameState();
        UIManager.switchTo(TheFrame.getFrame(), this, gamePanel);
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }


    public void readHighScoreFile() {
        try {
            File myObj = new File("./res/highscore/highscore.txt");
            Scanner myReader = new Scanner(myObj);
            int line = 1;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.equals("No high score yet.")) {
                    //update highscore
                    score = data;
                    return;
                }
                // score
                if (line == 1) {
                    score = data;
                }
                //time
                else if (line == 2) {
                    time = data;
                }
                line++;

                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
