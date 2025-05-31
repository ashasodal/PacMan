package sodal.pacman.entity.food;

import javax.swing.plaf.PanelUI;
import java.awt.*;

public class Food {
    private final byte WIDTH = 4;
    private final byte HEIGHT = 4;

    private static final byte SIZE = 4;

    private int x;
    private int y;

    private static final Color color = new Color(234, 130, 229);

    private Rectangle rect;


    public Food(int x, int y) {
        this.x = x;
        this.y = y;
        this.rect = new Rectangle(x,y,WIDTH,HEIGHT);
    }


    public void render(Graphics2D g2) {
        g2.setColor(color);
        g2.fillRect(this.x,this.y,this.WIDTH, this.HEIGHT);
    }

    public static byte getSize() {
        return SIZE; // or height because WIDTH = HEIGHT
    }


}
