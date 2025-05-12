package sodal.pacman.entity.enemy;

import sodal.pacman.entity.Entity;

import java.awt.*;

public class Enemy extends Entity {


    private Rectangle[] rect;
    protected int x,y;
    public Enemy(int x, int y, int width, int height, int speed) {
        super(width, height, speed,"./src/sodal/pacman/entity/enemy/image/clyde.png");
        this.x = x;
        this.y = y;
        rect = new Rectangle[5];
        rect[0] = new Rectangle(this.x, this.y + 11, this.width, this.height - 11);

       // rect[1] = new Rectangle(this.x + 2, this.y + 6, this.width-4, 5);
       /* rect[2] = new Rectangle(this.x + 4, this.y + 4, this.width-8, 2);
        rect[3] = new Rectangle(this.x + 6, this.y + 2, this.width-12, 2);
        rect[4] = new Rectangle(this.x + 10, this.y , this.width-20, 2);*/
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
        float alpha = 1.0f; // 50% transparent





        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(3, 10, 100, (int)(255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[0].x, this.rect[0].y, this.rect[0].width, this.rect[0].height);


      /* g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(3, 1, 100, (int)(255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[1].x, this.rect[1].y, this.rect[1].width, this.rect[1].height);



       /* g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(3, 20, 100, (int)(255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[2].x, this.rect[2].y, this.rect[2].width, this.rect[2].height);


        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(150, 10, 20, (int)(255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[3].x, this.rect[3].y, this.rect[3].width, this.rect[3].height);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(new Color(180, 100, 200, (int)(255 * alpha))); // Blue with transparency
        g2.fillRect(this.rect[4].x, this.rect[4].y, this.rect[4].width, this.rect[4].height);*/


// Optional: reset composite to full opacity for other drawings
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

    }
}
