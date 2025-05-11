package sodal.pacman.entity.enemy;

import sodal.pacman.entity.Entity;

import java.awt.*;

public class Enemy extends Entity {


    private Rectangle[] rect;
    public Enemy(int x, int y, int width, int height, int speed) {
        super(x, y, width, height, speed,"./src/sodal/pacman/entity/enemy/image/clyde.png");
        rect = new Rectangle[4];
        rect[0] = new Rectangle(this.x, this.y + 11, this.width, this.height - 11);
        rect[1] = new Rectangle(this.x + 2, this.y + 6, this.width-4, 5);
    }


    public Rectangle[] getRect() {
        return rect;
    }


    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2) {


        g2.drawImage(this.image, this.x, this.y,this.width, this.height,null );

        // Set a semi-transparent blue color (alpha range: 0.0f - fully transparent to 1.0f - fully opaque)
        float alpha = 0.9f; // 50% transparent
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(123, 0, 255, (int)(255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[1].x, this.rect[1].y, this.rect[1].width, this.rect[1].height);




        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(3, 100, 2, (int)(255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[0].x, this.rect[0].y, this.rect[0].width, this.rect[0].height);

// Optional: reset composite to full opacity for other drawings
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

    }
}
