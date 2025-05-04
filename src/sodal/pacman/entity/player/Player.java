package sodal.pacman.entity.player;

import sodal.pacman.entity.Entity;
import sodal.pacman.gui.ThePanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Entity {

    public Player(int x, int y, int width, int height, int speed) {
        super(x, y,width,height, speed,"./src/sodal/pacman/entity/player/image/up/1.png");
    }

    @Override
    public void update() {
        move();
    }

    private void move() {
        byte[] dir = ThePanel.getDirection();
        if (dir[0] == 1) {
            this.y -= this.speed;
        } else if (dir[1] == 1) {
            this.y += this.speed;
        } else if (dir[2] == 1) {
            this.x -= this.speed;
        } else if (dir[3] == 1) {
            this.x += this.speed;
        }
    }

    @Override
    public void render(Graphics2D g2) {

        g2.setColor(Color.blue);
        g2.fillRect(this.x, this.y, this.width, this.height);

        g2.drawImage(this.image, this.x, this.y,this.width, this.height,null );

    }
}
