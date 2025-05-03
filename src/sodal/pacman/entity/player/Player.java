package sodal.pacman.entity.player;

import sodal.pacman.entity.Entity;

import java.awt.*;

public class Player extends Entity {


    public Player(int x, int y, int speed) {
        super(x, y,speed);
    }



    @Override
    public void update(Graphics2D g2) {

    }

    @Override
    public void render(Graphics2D g2) {

        g2.setColor(Color.blue);
        g2.fillRect(this.getxPos(), this.getyPos(),this.getSize(), this.getSize());

    }
}
