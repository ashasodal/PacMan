package sodal.pacman.entity.player;

import sodal.pacman.entity.Entity;
import sodal.pacman.entity.enemy.Enemy;
import sodal.pacman.gui.ThePanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Entity {

    private Color color = Color.RED;

    private volatile boolean enemyCollision = false;

    public Player(int x, int y, int width, int height, int speed) {
        super(x, y, width, height, speed, "./src/sodal/pacman/entity/player/image/up/up1.png");
    }

    @Override
    public void update() {
        move();
        checkCollision();
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

        this.rect.setLocation(this.x, this.y);
    }

    public void checkCollision() {
        Enemy enemy = ThePanel.getRedGhost();
        double enemyCenterX = enemy.getX() + (enemy.getWidth()) / 2.0;
        double enemyCenterY = enemy.getY() + (enemy.getHeight()) / 2.0;
        //distance between the center of player and enemy
        double distance = distance(enemyCenterX, enemyCenterY);

        double playerRadius = this.width / 2.0;
        double enemyRadius = (enemy.getWidth()) / 2.0;

        //collision!!!
        if (distance < (playerRadius + enemyRadius)) {
            enemyCollision = true;
            while (distance < (playerRadius + enemyRadius)) {
                //which direction player moved when colliding

                //up
                if (ThePanel.getDirection()[0] == 1) {
                    //move down
                    this.y += 1;

                }
                //down
                else if (ThePanel.getDirection()[1] == 1) {
                    //move up
                    this.y -= 1;

                }
                //left
                else if (ThePanel.getDirection()[2] == 1) {
                    this.x += 1; //move right

                }
                //right
                else if (ThePanel.getDirection()[3] == 1) {
                    this.x -= 1; // move left

                }
                distance = distance(enemyCenterX,enemyCenterY);
            }
            enemyCollision = false;

        }

    }

    public boolean isEnemyCollision() {
        return enemyCollision;
    }

    public double distance(double  enemyCenterX , double enemyCenterY ) {

        double playerCenterX = this.x + (this.width) / 2.0;
        double playerCenterY = this.y + (this.height) / 2.0;

        double xDistance = Math.abs(playerCenterX - enemyCenterX);
        double yDistance = Math.abs(playerCenterY - enemyCenterY);


        double a = xDistance * xDistance;
        double b = yDistance * yDistance;

        return Math.sqrt(a + b);
    }

    @Override
    public void render(Graphics2D g2) {

       // g2.setColor(this.color);
      //  g2.drawString("COLLIDE", 50, 50);
        // g2.fillRect(this.x, this.y, this.width, this.height);
        g2.drawImage(this.image, this.x, this.y, this.width, this.height, null);


    }
}
