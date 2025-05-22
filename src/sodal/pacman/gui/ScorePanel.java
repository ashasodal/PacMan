package sodal.pacman.gui;

import javax.swing.*;
import java.awt.*;

public class ScorePanel {

    private int x, y;
    private int width,height;
    private int points = 0;
    private int health = 3;

   public ScorePanel() {
       this.x = 0;
       this.y = ThePanel.getHEIGHT() - ThePanel.getTileSize();
       this.width = ThePanel.getWIDTH();
       this.height = ThePanel.getTileSize();

    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.magenta);
        g2.fillRect(this.x,this.y,this.width,this.height);
    }



}
