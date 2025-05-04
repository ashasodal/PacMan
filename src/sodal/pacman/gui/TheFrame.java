package sodal.pacman.gui;

import javax.swing.*;

public class TheFrame {

    public TheFrame() {
        ThePanel panel = new ThePanel();
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


    }
}
