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


        //check if player is colliding with any of the enemy rectangles

        for (int i = 0; i < 5; i++) {
            //top

            //top left corner
            if (playerCenterY < enemyRect[i].getY() && playerCenterX < enemyRect[i].getX()) {
                //diagonal distance
                double distance = distance(enemyRect[i].getX(), enemyRect[i].getY());
                if (distance < playerRadius) {
                    enemyCollision = true;
                    backtrack(distance, enemyRect[i].getX(), enemyRect[i].getY());
                }
            }

            //top middle
            else if (playerCenterY < enemyRect[i].getY() && playerCenterX >= enemyRect[i].getX() && playerCenterX <= enemyRect[i].getX() + enemyRect[i].getWidth()) {
                //vertical distance
                double distance = distance(getPlayerCenterX(), enemyRect[i].getY());
                if (distance < playerRadius) {
                    enemyCollision = true;
                    backtrack(distance, getPlayerCenterX(), enemyRect[i].getY());
                }
            }

            //top right corner
            else if (playerCenterY < enemyRect[i].getY() && playerCenterX > enemyRect[i].getX() + enemyRect[i].getWidth()) {
                //diagonal distance
                double distance = distance(enemyRect[i].getX() + enemyRect[i].getWidth(), enemyRect[i].getY());
                if (distance < playerRadius) {
                    enemyCollision = true;
                    backtrack(distance, enemyRect[i].getX() + enemyRect[i].getWidth(), enemyRect[i].getY());
                }
            }

            //middle

            //middle left
            else if (playerCenterX < enemyRect[i].getX() && playerCenterY >= enemyRect[i].getY() && playerCenterY <= enemyRect[i].getY() + enemyRect[i].getHeight()) {
                //horizontal distance
                double distance = distance(enemyRect[i].getX(), getPlayerCenterY());
                if (distance < playerRadius) {
                    enemyCollision = true;
                    backtrack(distance, enemyRect[i].getX(), getPlayerCenterY());
                }
            }

            //middle right
            else if (playerCenterX > enemyRect[i].getX() + enemyRect[i].getWidth() && playerCenterY >= enemyRect[i].getY() && playerCenterY <= enemyRect[i].getY() + enemyRect[i].getHeight()) {
                //horizontal distance
                double distance = distance(enemyRect[i].getX() + enemyRect[i].getWidth(), getPlayerCenterY());
                if (distance < playerRadius) {
                    enemyCollision = true;
                    backtrack(distance, enemyRect[i].getX() + enemyRect[i].getWidth(), getPlayerCenterY());
                }
            }

            //bottom

            //bottom left corner
            else if (playerCenterY > enemyRect[i].getY() + enemyRect[i].getHeight() && playerCenterX < enemyRect[i].getX()) {
                //diagonal distance
                double distance = distance(enemyRect[i].getX(), enemyRect[i].getY() + enemyRect[i].getHeight());
                if (distance < playerRadius) {
                    enemyCollision = true;
                    backtrack(distance, enemyRect[i].getX(), enemyRect[i].getY() + enemyRect[i].getHeight());
                }
            }

            //bottom middle
            else if (playerCenterY > enemyRect[i].getY() + enemyRect[i].getHeight() && playerCenterX >= enemyRect[i].getX() && playerCenterX <= enemyRect[i].getX() + enemyRect[i].getWidth()) {
                //vertical distance
                double distance = distance(playerCenterX, enemyRect[i].getY() + enemyRect[i].getHeight());
                if (distance < playerRadius) {
                    enemyCollision = true;
                    backtrack(distance, playerCenterX, enemyRect[i].getY() + enemyRect[i].getHeight());
                }
            }

            //bottom right
            else if (playerCenterY > enemyRect[i].getY() + enemyRect[i].getHeight() && playerCenterX > enemyRect[i].getX() + enemyRect[i].getWidth()) {
                //diagonal distance
                double distance = distance(enemyRect[i].getX() + enemyRect[i].getWidth(), enemyRect[i].getY() + enemyRect[i].getHeight());
                if (distance < playerRadius) {
                    enemyCollision = true;
                    backtrack(distance, enemyRect[i].getX() + enemyRect[i].getWidth(), enemyRect[i].getY() + enemyRect[i].getHeight());
                }
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
