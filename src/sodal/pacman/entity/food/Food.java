package sodal.pacman.entity.food;

import javax.swing.plaf.PanelUI;
import java.awt.*;

public class Food {
    private  byte width;
    private  byte height;

    private static  byte size = 4;

    private int x;
    private int y;

    private static final Color color = new Color(234, 130, 229);

    private Rectangle rect;


    public Food(int x, int y) {
        this.x = x;
        this.y = y;
        width = 4;
        height = 4;
        size = width; // size = width = height
        this.rect = new Rectangle(x,y,width,height);
    }


    public void render(Graphics2D g2) {
        g2.setColor(color);
        g2.fillRect(this.x,this.y,this.width, this.height);
    }

    public static byte getSize() {
        return size; // or height because WIDTH = HEIGHT
    }


    public Rectangle getFoodRect() {
        return rect;
    }

    public void setSize(byte width, byte height) {
        this.width = width;
        this.height = height;
    }




}
