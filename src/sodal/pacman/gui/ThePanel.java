package sodal.pacman.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThePanel extends JPanel implements ActionListener {

    private final int TILE_SIZE = 20;
    private final int WIDTH = 35*TILE_SIZE;
    private final int HEIGHT = 23*TILE_SIZE;


    //game loop
    private Timer timer;
    private static int delay = 17;
    private int counter = 0;
    private int time = 0;



    public ThePanel() {
        this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        this.setOpaque(true);
        this.setDoubleBuffered(true);

        //game loop
        timer = new Timer(delay, this);

    }


    public Component getPanel() {
        return this;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        render();
    }

    private void update() {

    }

    private void render() {
        repaint();
    }

    public Timer getTimer() {
        return timer;
    }


    private void FPS() {
        counter++;
        if(counter == 60) {
            time++;
            System.out.println(time);
            counter = 0;
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        FPS();
        g2.dispose();


    }



}
