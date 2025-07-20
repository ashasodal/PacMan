package sodal.pacman.gui;

import sodal.pacman.entity.player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScoreBoard {

    private int x, y;
    private int width, height;

    private Player player;
    private BufferedImage healthImage;

    private Font font;


    //TIMER
    private long timer = -1;
    private int displayTime = 0;
    private StringBuffer displayTimerString = new StringBuffer("00:00");


    public ScoreBoard(Player player) {
        this.x = 0;
        this.y = GamePanel.getHEIGHT() - GamePanel.getTileSize();
        this.width = GamePanel.getWIDTH();
        this.height = GamePanel.getTileSize();
        this.player = player;
        this.healthImage = this.player.getLeftBuffer()[2];

        font = getFont("./res/font/pixel.otf", 20f);
        ;

        createBuffer();

    }


    public void update() {
        if (GamePanel.getStartGame() && !GamePanel.getPlayerEnemyCollisionState()) {
            if (timer == -1) {
                System.out.println("start TIMER");
                timer = System.currentTimeMillis();
            }
            long deltaTime = System.currentTimeMillis() - timer;
            int maxTimeMinutes = 2;
            //one second has passed
            if (deltaTime >= 1000) {
                displayTime++;
                int minutes = (int) Math.floor(displayTime / 60.0);
                int seconds = displayTime % 60;
                String format = String.format("%02d:%02d", minutes, seconds);
                displayTimerString.replace(0, displayTimerString.length(), format);
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
        g2.setFont(font);
        g2.setColor(new Color(100, 0, 0));
        String text = String.valueOf(displayTimerString);
        int x = this.width - 3 * GamePanel.getTileSize();
        int y = alignY(g2, font);
        g2.drawString(text, x, y);
    }

    private void renderScoreBoard(Graphics2D g2) {
        g2.setColor(Color.gray);
        g2.fillRect(this.x, this.y, this.width, this.height);


    }


    private void renderScore(Graphics2D g2) {
        g2.setFont(font);
        //font color
        g2.setColor(new Color(100, 0, 0));
        String text = "SCORE: " + player.getScore();
        int x = alignX(g2, font, text);
        int y = alignY(g2, font);
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
            g2.drawImage(this.healthImage, x, this.y, GamePanel.getTileSize(), GamePanel.getTileSize(), null);
            x += GamePanel.getTileSize();
        }
    }


    public void resetTimer() {
        timer = -1;
        displayTime = 0;
        displayTimerString.replace(0, displayTimerString.length(), "00:00");
    }


}
