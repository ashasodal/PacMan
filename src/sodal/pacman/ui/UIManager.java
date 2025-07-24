package sodal.pacman.ui;

import javax.swing.*;
import java.awt.event.KeyListener;

public class UIManager {


    public static void switchTo(JFrame frame, JPanel from, JPanel to) {
        from.removeKeyListener((KeyListener) from);
        frame.remove(from);

        frame.add(to);
        to.addKeyListener((KeyListener) to);
        to.setFocusable(true);
        to.requestFocusInWindow();

        frame.validate();
        frame.repaint();
    }
}
