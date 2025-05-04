package sodal.pacman.entity.player;

import sodal.pacman.entity.Entity;
import sodal.pacman.gui.ThePanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Entity {

    public Player(int x, int y, int speed) {
        super(x, y, speed);
    }

    @Override
    public void update() {
        move();
    }

    private void move() {
        boolean up = ThePanel.getUp();
        boolean down = ThePanel.getDown();
        boolean left = ThePanel.getLeft();
        boolean right = ThePanel.getRight();
        if (up) {
            this.y -= this.speed;
        } else if (down) {
            this.y += this.speed;
        } else if (left) {
            this.x -= this.speed;
        } else if (right) {
            this.x += this.speed;
        }
    }

    @Override
    public void render(Graphics2D g2) {

        g2.setColor(Color.blue);
        g2.fillRect(this.x, this.y, this.size, this.size);

    }
}
