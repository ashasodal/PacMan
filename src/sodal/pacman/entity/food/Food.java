package sodal.pacman.entity.food;

import javax.swing.plaf.PanelUI;
import java.awt.*;

public class Food {
    private volatile byte width;
    private volatile byte height;

    private static final  byte SIZE = 4;

    private int x;
    private int y;

   // private static final Color color = new Color(234, 130, 229);
   private static final Color color = Color.GREEN;



    public Food(int x, int y) {
        this.x = x;
        this.y = y;
        width = 4;
        height = 4;

    }


    public void render(Graphics2D g2) {
        g2.setColor(color);
        g2.fillRect(this.x,this.y,this.width, this.height);
    }

    public static byte getSize() {
        return SIZE; // or height because WIDTH = HEIGHT
    }




    public void setSize(byte width, byte height) {
        this.width = width;
        this.height = height;
    }


    public boolean isEaten() {
        return width == 0; // or height == 0.
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public byte getWidth() {
        return width;
    }

    public byte getHeight() {
        return height;
    }




}
