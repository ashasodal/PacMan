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

    private Rectangle rect;

    public Player(int x, int y, int width, int height, int speed) {
        super(x, y, width, height, speed, "./src/sodal/pacman/entity/player/image/up/up1.png");
        rect = new Rectangle(this.x, this.y, this.width, this.height);
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

        Rectangle[] enemyRect = ThePanel.getRedGhost().getRect();

        double playerCenterX = getPlayerCenterX();
        double playerCenterY = getPlayerCenterY();
        double playerRadius = getPlayerRadius();


        //top

        //top left corner
        if (playerCenterY < enemyRect[0].getY() && playerCenterX < enemyRect[0].getX()) {
            //diagonal distance
            double distance = distance(enemyRect[0].getX(), enemyRect[0].getY());
            if (distance < playerRadius) {
                enemyCollision = true;
                backtrack(distance, enemyRect[0].getX(), enemyRect[0].getY());
            }
        }

        //top middle
        else if (playerCenterY < enemyRect[0].getY() && playerCenterX >= enemyRect[0].getX() && playerCenterX <= enemyRect[0].getX() + enemyRect[0].getWidth()) {
            //vertical distance
            double distance = distance(getPlayerCenterX(), enemyRect[0].getY());
            if (distance < playerRadius) {
                enemyCollision = true;
                backtrack(distance, getPlayerCenterX(), enemyRect[0].getY());
            }
        }

        //top right corner
        else if (playerCenterY < enemyRect[0].getY() && playerCenterX > enemyRect[0].getX() + enemyRect[0].getWidth()) {
            //diagonal distance
            double distance = distance(enemyRect[0].getX() + enemyRect[0].getWidth(), enemyRect[0].getY());
            if (distance < playerRadius) {
                enemyCollision = true;
                backtrack(distance, enemyRect[0].getX() + enemyRect[0].getWidth(), enemyRect[0].getY());
            }
        }


        //middle

        //middle left
        else if (playerCenterX < enemyRect[0].getX() && playerCenterY >= enemyRect[0].getY() && playerCenterY <= enemyRect[0].getY() + enemyRect[0].getHeight()) {
            //horizontal distance
            double distance = distance(enemyRect[0].getX(), getPlayerCenterY());
            if (distance < playerRadius) {
                enemyCollision = true;
                backtrack(distance, enemyRect[0].getX(), getPlayerCenterY());
            }
        }

        //middle right
        else if (playerCenterX > enemyRect[0].getX() + enemyRect[0].getWidth() && playerCenterY >= enemyRect[0].getY() && playerCenterY <= enemyRect[0].getY() + enemyRect[0].getHeight()) {
            //horizontal distance
            double distance = distance(enemyRect[0].getX() + enemyRect[0].getWidth(), getPlayerCenterY());
            if (distance < playerRadius) {
                enemyCollision = true;
                backtrack(distance, enemyRect[0].getX() + enemyRect[0].getWidth(), getPlayerCenterY());
            }
        }


        //bottom

        //bottom left corner
        else if (playerCenterY > enemyRect[0].getY() + enemyRect[0].getHeight() && playerCenterX < enemyRect[0].getX()) {
            //diagonal distance
            double distance = distance(enemyRect[0].getX(), enemyRect[0].getY() + enemyRect[0].getHeight());
            if (distance < playerRadius) {
                enemyCollision = true;
                backtrack(distance, enemyRect[0].getX(), enemyRect[0].getY() + enemyRect[0].getHeight());
            }
        }

        //bottom middle
        else if (playerCenterY > enemyRect[0].getY() + enemyRect[0].getHeight() && playerCenterX >= enemyRect[0].getX() && playerCenterX <= enemyRect[0].getX() + enemyRect[0].getWidth()) {
            //vertical distance
            double distance = distance(playerCenterX, enemyRect[0].getY() + enemyRect[0].getHeight());
            if (distance < playerRadius) {
                enemyCollision = true;
                backtrack(distance, playerCenterX, enemyRect[0].getY() + enemyRect[0].getHeight());
            }
        }

        //bottom right

        else if (playerCenterY > enemyRect[0].getY() + enemyRect[0].getHeight() && playerCenterX > enemyRect[0].getX() + enemyRect[0].getWidth()) {
            //diagonal distance
            double distance = distance(enemyRect[0].getX() + enemyRect[0].getWidth(), enemyRect[0].getY() + enemyRect[0].getHeight());
            if (distance < playerRadius) {
                enemyCollision = true;
                backtrack(distance, enemyRect[0].getX() + enemyRect[0].getWidth(), enemyRect[0].getY() + enemyRect[0].getHeight());
            }
        }

    }


    public void backtrack(double distance, double enemyX, double enemyY) {
        double playerRadius = getPlayerRadius();
        while (distance < playerRadius) {
            moveInOppositeDirection();
            distance = distance(enemyX, enemyY);
        }
        enemyCollision = false;
    }

    private void moveInOppositeDirection() {
        byte dir[] = ThePanel.getDirection();
        if (dir[0] == 1) {
            this.y += 1;
        } else if (dir[1] == 1) {
            this.y -= 1;
        } else if (dir[2] == 1) {
            this.x += 1;
        } else if (dir[3] == 1) {
            this.x -= 1;
        }
        this.rect.setLocation(this.x, this.y);
    }

    private double getPlayerCenterX() {
        return this.x + this.width / 2.0;
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


    public double distance(double enemyX, double enemyY) {
        double deltaX = Math.abs(getPlayerCenterX() - enemyX);
        double deltaY = Math.abs(getPlayerCenterY() - enemyY);
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
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
