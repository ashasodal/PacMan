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
        if (!enemyCollision) {
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
    }

    public void checkCollision() {
        Enemy enemy = ThePanel.getRedGhost();
        double playerCenterX = this.x + (this.width / 2.0);
        double playerCenterY = this.y + (this.height / 2.0);
        double playerRadius = this.width / 2.0;


        //player is above

        //top left
      /*  if (playerCenterX < enemy.getX() && playerCenterY < enemy.getY()) {
            double distance = diagonalDistance(enemy.getX(), enemy.getY());
            //collision
            if (distance <= playerRadius) {
                enemyCollision = true;
            }
        }*/

        //top middle
        if (playerCenterX >= enemy.getX() && (playerCenterX <= enemy.getX() + enemy.getWidth()) && playerCenterY < enemy.getY()) {
            double distance = Math.abs(playerCenterY - enemy.getY());
            //collision
            if (distance <= playerRadius) {
                enemyCollision = true;
                //backtrack

            }

        }
        //top right
       /* else if (playerCenterX > enemy.getX() + enemy.getWidth() && playerCenterY < enemy.getY()) {
            double distance = diagonalDistance(enemy.getX() + enemy.getWidth(), enemy.getY());
            //collision
            if (distance <= playerRadius) {
                enemyCollision = true;
            }
        }*/


    }

    public boolean isEnemyCollision() {
        return enemyCollision;
    }





    public double diagonalDistance(double enemyXCorner, double enemyYCorner) {

        double playerCenterX = this.x + (this.width) / 2.0;
        double playerCenterY = this.y + (this.height) / 2.0;

        double xDistance = Math.abs(playerCenterX - enemyXCorner);
        double yDistance = Math.abs(playerCenterY - enemyYCorner);


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
