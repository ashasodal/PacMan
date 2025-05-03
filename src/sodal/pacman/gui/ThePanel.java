package sodal.pacman.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThePanel extends JPanel implements ActionListener {

    private final int TILE_SIZE = 35;

    private final int numOfTilesWidth = 20;
    private final int numOfTilesHeight = 14;
    private final int WIDTH = numOfTilesWidth*TILE_SIZE;
    private final int HEIGHT = numOfTilesHeight*TILE_SIZE;


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


    private void grid(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        int x = 0;
        int y = 0;
        for(int i = 0; i < this.numOfTilesHeight; i++) {
            for(int j = 0; j < this.numOfTilesWidth; j++) {
                g2.drawRect(x,y,TILE_SIZE,TILE_SIZE);
                x += TILE_SIZE;
            }
            x = 0;
            y += TILE_SIZE;
        }
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //paint
        grid(g2);





        g2.dispose();

    }



}
