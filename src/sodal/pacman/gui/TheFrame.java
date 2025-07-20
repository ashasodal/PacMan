package sodal.pacman.gui;

import javax.swing.*;

public class TheFrame {

    public TheFrame() {
       // ThePanel panel = new ThePanel();
        JFrame frame = new JFrame();
        Menu menu = new Menu(frame,ThePanel.getWIDTH(), ThePanel.getHEIGHT());
        frame.add(menu);
        frame.pack();
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //start game
       // panel.getGameLoop().start();


    }
}
