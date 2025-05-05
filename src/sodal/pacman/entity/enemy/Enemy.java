package sodal.pacman.entity.enemy;

import sodal.pacman.entity.Entity;

import java.awt.*;

public class Enemy extends Entity {
    public Enemy(int x, int y, int width, int height, int speed) {
        super(x, y, width, height, speed,"./src/sodal/pacman/entity/enemy/image/blinky.png");
    }




    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) {
       // g2.setColor(Color.blue);
       // g2.fillRect(this.x, this.y, this.width, this.height);

        g2.drawImage(this.image, this.x, this.y,this.width, this.height,null );
    }
}
