package sodal.pacman.entity.player;

import sodal.pacman.entity.Entity;
import sodal.pacman.entity.enemy.Enemy;
import sodal.pacman.gui.ThePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

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
        double playerCenterX = getPlayerCenterX();
        double playerCenterY = getPlayerCenterY();
        double playerRadius = getPlayerRadius();


        //top
        //top left corner
        if (playerCenterY < enemy.getY() && playerCenterX < enemy.getX()) {
            double distance = diagonalDistance(enemy);
            if (distance < playerRadius) {
                enemyCollision = true;
                backtrack(distance, enemy, 'd');
            }
        }

        //top middle
       else  if(playerCenterY < enemy.getY() && playerCenterX >= enemy.getX() && playerCenterX <= enemy.getX() + enemy.getWidth()) {
            double distance = verticalDistance(enemy);
            if(distance < playerRadius) {
                enemyCollision = true;
                backtrack(distance, enemy, 'v');
            }

        }


    }


    public void backtrack(double distance, Enemy enemy, char region) {
        System.out.println("-----");
        int counter = 1;
        byte dir[] = ThePanel.getDirection();
        double playerRadius = getPlayerRadius();
        while (distance < playerRadius) {

            if (dir[0] == 1) {
                this.y += 1;
            } else if (dir[1] == 1) {
                this.y -= 1;
            } else if (dir[2] == 1) {
                this.x += 1;
            } else if (dir[3] == 1) {
                this.x -= 1;
            }

            if(region == 'd') {
                distance = diagonalDistance(enemy);
               // System.out.println("counter: " + counter );
             //   counter++;
            }
            else if(region == 'v') {
                distance = verticalDistance(enemy);
                System.out.println("counter: " + counter );
                counter++;
            }



        }
        enemyCollision = false;
        System.out.println("-----");
    }

    private double getPlayerCenterX() {
        return  this.x + this.width / 2.0;
    }

    private double getPlayerCenterY() {
        return this.y + this.height / 2.0;
    }

    private double getPlayerRadius() {
        return this.width / 2.0;
    }




    public boolean isEnemyCollision() {
        return enemyCollision;
    }


    public double diagonalDistance( Enemy enemy) {
        double deltaX = Math.abs(getPlayerCenterX() - enemy.getX());
        double deltaY = Math.abs(getPlayerCenterY() - enemy.getY());
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    private double verticalDistance(Enemy enemy) {
        return Math.abs(getPlayerCenterY() -  enemy.getY());
    }


    @Override
    public void render(Graphics2D g2) {

        // g2.setColor(this.color);
        //  g2.drawString("COLLIDE", 50, 50);
        // g2.fillRect(this.x, this.y, this.width, this.height);
        g2.drawImage(this.image, this.x, this.y, this.width, this.height, null);
        g2.setColor(Color.magenta);
        g2.drawOval(this.x + (this.width / 2), this.y + (this.height / 2), 1, 1);


    }
}
