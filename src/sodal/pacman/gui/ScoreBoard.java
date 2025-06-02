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
        g2.setColor(Color.red);
      //  g2.setFont(new Font(F));

        g2.setColor(new Color(100,0,0));
        float fontSize = 20f;
        int x = this.x + this.width - 5 * ThePanel.getTileSize();
        int y = this.y + this.height - (int)fontSize/2; // line UNDER the string

        try {
          renderPixelFont(g2,fontSize,x,y);
        }
        catch (FontFormatException | IOException e ) {
          renderRegularFont(g2,(int)fontSize,x,y);
        }


    }


    private void renderPixelFont(Graphics2D g2, float fontSize, int x, int y) throws IOException, FontFormatException{
        Font pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File("./res/font/pixel.otf")).deriveFont(fontSize);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(pixelFont);
        // Set color and font
        g2.setFont(pixelFont);
        // Draw the string
        g2.drawString("SCORE: " + player.getScore(), x, y);

    }

    private void renderRegularFont(Graphics2D g2,int fontSize, int x, int y ) {
        g2.setColor(new Color(100,0,0));
        Font font = new Font(Font.SERIF, Font.BOLD, fontSize );
        g2.setFont(font);
        g2.drawString("SCORE: " + player.getScore(), x, y);
    }


    private void renderHealth(Graphics2D g2) {
        int x = this.x;
        for (int i = 0; i < player.getHealth(); i++) {
            g2.drawImage(this.healthImage, x, this.y, ThePanel.getTileSize(), ThePanel.getTileSize(), null);
            x += ThePanel.getTileSize();
        }
    }


}
