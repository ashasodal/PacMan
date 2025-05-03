package sodal.pacman.gui;

import javax.swing.*;
import java.awt.*;

public class ThePanel extends JPanel {

    private final int TILE_SIZE = 20;
    private final int WIDTH = 35*TILE_SIZE;
    private final int HEIGHT = 23*TILE_SIZE;


    public ThePanel() {
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setOpaque(true);
        this.setDoubleBuffered(true);
    }


    public Component getPanel() {
        return this;
    }

}
