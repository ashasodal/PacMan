package sodal.pacman.gui;

import javax.swing.*;

public class TheFrame {

    private static JFrame frame;

    public TheFrame() {
       // ThePanel panel = new ThePanel();
         frame = new JFrame();
        Menu menu = new Menu(frame, GamePanel.getWIDTH(), GamePanel.getHEIGHT());
        frame.add(menu);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //start game
       // panel.getGameLoop().start();

    }


    public static JFrame getFrame() {
        return frame;
    }
}
