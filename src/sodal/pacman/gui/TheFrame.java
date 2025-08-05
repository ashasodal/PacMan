package sodal.pacman.gui;

import javax.swing.*;

public class TheFrame {

    private static JFrame frame;

    public TheFrame() {
        frame = new JFrame("Pac-Man");
        Menu menu = new Menu(GamePanel.getWIDTH(), GamePanel.getHEIGHT());
        frame.add(menu);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public static JFrame getFrame() {
        return frame;
    }
}
