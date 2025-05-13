package sodal.pacman.entity.enemy;

import sodal.pacman.entity.Entity;

import java.awt.*;

public class Enemy extends Entity {


    private Rectangle[] rect;
    protected int x, y;

    private byte[] direction;

    public Enemy(int x, int y, int width, int height, int speed) {
        super(width, height, speed, "./src/sodal/pacman/entity/enemy/image/clyde.png");
        this.x = x;
        this.y = y;
        createRectangles();
        direction = new byte[4];

    }


    private void createRectangles() {
        rect = new Rectangle[5];
       /* rect[0] = new Rectangle(this.x, this.y, this.width *3, this.height*2);
        rect[1] = new Rectangle(this.x + this.width/2, this.y -this.height , this.width *2, this.height);*/
        rect[0] = new Rectangle(this.x, this.y + 11, this.width, this.height - 11);

       /* rect[1] = new Rectangle(this.x + 2, this.y + 6, this.width - 4, 5);
        rect[2] = new Rectangle(this.x + 4, this.y + 4, this.width - 8, 2);
        rect[3] = new Rectangle(this.x + 6, this.y + 2, this.width - 12, 2);
        rect[4] = new Rectangle(this.x + 10, this.y, this.width - 20, 2);*/
    }


    public void moveInOppositeDirection() {
        System.out.println("WRONG!!!!!!");
        if (direction[0] == 1) {

        } else if (direction[1] == 1) {

        } else if (direction[2] == 1) {

            x += 1;
            for (int i = 0; i < 1; i++) {
                this.rect[i].x += 1;
            }

        } else if (direction[3] == 1) {

        }

    }


    public void move() {

        direction[2] = 1;
        x -= speed;

        for (int i = 0; i < 1; i++) {
            this.rect[i].x -= speed;
        }
    }


    public Rectangle[] getRect() {
        return rect;
    }


    @Override
    public void update() {
        move();


    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(this.image, this.x, this.y, this.width, this.height, null);
        this.paintRect(g2);
    }


    public void paintRect(Graphics2D g2) {

        // Set a semi-transparent blue color (alpha range: 0.0f - fully transparent to 1.0f - fully opaque)
        float alpha = 1.0f; // 50% transparent
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(255, 200, 100, (int) (255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[0].x, this.rect[0].y, this.rect[0].width, this.rect[0].height);


       /* g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(3, 100, 1, (int) (255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[1].x, this.rect[1].y, this.rect[1].width, this.rect[1].height);


        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(3, 20, 100, (int) (255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[2].x, this.rect[2].y, this.rect[2].width, this.rect[2].height);


        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(3, 100, 1, (int) (255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[3].x, this.rect[3].y, this.rect[3].width, this.rect[3].height);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(180, 100, 200, (int) (255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[4].x, this.rect[4].y, this.rect[4].width, this.rect[4].height);*/


// Optional: reset composite to full opacity for other drawings
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

    }
}
