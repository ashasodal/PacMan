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

        int x = this.x;
        for (int i = 0; i < player.getHealth(); i++) {
            g2.drawImage(this.healthImage, x, this.y, ThePanel.getTileSize(), ThePanel.getTileSize(), null);
            x += ThePanel.getTileSize();
        }

    }


}
