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
    private int timer = 0;

    public ScoreBoard(Player player) {
        this.x = 0;
        this.y = ThePanel.getHEIGHT() - ThePanel.getTileSize();
        this.width = ThePanel.getWIDTH();
        this.height = ThePanel.getTileSize();
        this.player = player;

        this.healthImage = this.player.getLeftBuffer()[2];

        createBuffer();

    }


    private void createBuffer() {
        try {
            this.healthImage = ImageIO.read(new File("./res/image/scoreboard/health.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.gray);
        g2.fillRect(this.x, this.y, this.width, this.height);

        renderHealth(g2);
        renderScore(g2);

    }


    private void renderScore(Graphics2D g2) {
        //font color
        g2.setColor(new Color(100,0,0));
        float fontSize = 20f;
        Font font = getFont("./res/font/pixel.otf" , fontSize);
        g2.setFont(font);
        String text = "SCORE: " + player.getScore();

        //alignment
        FontMetrics metrics = g2.getFontMetrics(font);
        //x align
        int textWidth = metrics.stringWidth(text);
        int x = (this.width - textWidth) / 2;

        //y align
        int textHeight = metrics.getHeight();            // total height of font (ascent + descent)
        int ascent = metrics.getAscent();                // ascent is how far baseline is from top
        int y = this.y + (this.height - textHeight) / 2 + ascent;

        g2.drawString(text, x, y);
        
    }




    private Font getFont(String path, float fontSize) {
        Font font;
        try {
           font = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(fontSize);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        }
        catch (IOException | FontFormatException e) {
           font =  new Font(Font.SERIF, Font.BOLD, (int)fontSize );
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
