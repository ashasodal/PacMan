package sodal.pacman.gui;

import sodal.pacman.entity.player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PipedReader;
import java.io.SyncFailedException;

public class ScoreBoard {

    private int x, y;
    private int width, height;

    private Player player;
    private BufferedImage healthImage;
    private long timer = -1;
    private int displayTime = 0;
    private StringBuffer displayTimerString = new StringBuffer("00:00");



    public ScoreBoard(Player player) {
        this.x = 0;
        this.y = ThePanel.getHEIGHT() - ThePanel.getTileSize();
        this.width = ThePanel.getWIDTH();
        this.height = ThePanel.getTileSize();
        this.player = player;

        this.healthImage = this.player.getLeftBuffer()[2];

        createBuffer();

    }


    public void update() {
        if (ThePanel.getStartGame()) {
            if(timer == -1) {
                System.out.println("start TIMER");
                timer = System.currentTimeMillis();
            }
            long deltaTime = System.currentTimeMillis() - timer;
            int maxTimeMinutes = 2;
            //one second has passed
            if (deltaTime >= 1000) {
                displayTime++;
                //second part in 00:|00| <-
                int seconds = displayTime % 60;
                if(displayTime >= 60 && displayTime < 120) {
                    displayTimerString.replace(0, 2,"01");
                }

                if(seconds >= 0 && seconds <= 9) {
                    displayTimerString.replace(displayTimerString.length() -2, displayTimerString.length(), "00");
                    displayTimerString.replace(displayTimerString.length()-1, displayTimerString.length(), String.valueOf(seconds));
                }
                else {
                    displayTimerString.replace(displayTimerString.length() -2, displayTimerString.length(), "00");
                    displayTimerString.replace(displayTimerString.length()-2, displayTimerString.length(),String.valueOf(seconds));
                }

                System.out.println(seconds);
                System.out.println(displayTimerString);





                timer = System.currentTimeMillis();
            }
        }
    }


    private void createBuffer() {
        try {
            this.healthImage = ImageIO.read(new File("./res/image/scoreboard/health.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics2D g2) {
        renderScoreBoard(g2);
        renderHealth(g2);
        renderScore(g2);
        renderTimer(g2);

    }


    private void renderTimer(Graphics2D g2) {
        g2.setColor(new Color(100, 0, 0));
        Font font = getFont("./res/font/pixel.otf", 20f);
        g2.setFont(font);
        String text = "TIME: " + displayTimerString;
        int x = this.width - 6* ThePanel.getTileSize();
        int y = alignY(g2,font);
        g2.drawString(text, x, y);
    }

    private void renderScoreBoard(Graphics2D g2) {
        g2.setColor(Color.gray);
        g2.fillRect(this.x, this.y, this.width, this.height);



    }


    private void renderScore(Graphics2D g2) {
        //font color
        g2.setColor(new Color(100, 0, 0));
        float fontSize = 20f;
        Font font = getFont("./res/font/pixel.otf", fontSize);
        g2.setFont(font);
        String text = "SCORE: " + player.getScore();
        int x = alignX(g2,font,text);
        int y = alignY(g2,font);
        g2.drawString(text, x, y);
    }


    private int alignX(Graphics2D g2, Font font, String text) {
        //alignment
        FontMetrics metrics = g2.getFontMetrics(font);
        //x align
        int textWidth = metrics.stringWidth(text);
        return (this.width - textWidth) / 2;
    }

    private int alignY(Graphics2D g2, Font font) {
        FontMetrics metrics = g2.getFontMetrics(font);
        int textHeight = metrics.getHeight();            // total height of font (ascent + descent)
        int ascent = metrics.getAscent();                // ascent is how far baseline is from top
        return this.y + (this.height - textHeight) / 2 + ascent;
    }


    private Font getFont(String path, float fontSize) {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(fontSize);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        } catch (IOException | FontFormatException e) {
            font = new Font(Font.SERIF, Font.BOLD, (int) fontSize);
        }
        return font;
    }


    private void renderHealth(Graphics2D g2) {
        int x = this.x;
        for (int i = 0; i < player.getHealth(); i++) {
            g2.drawImage(this.healthImage, x, this.y, ThePanel.getTileSize(), ThePanel.getTileSize(), null);
            x += ThePanel.getTileSize();
        }
    }





}
