package sodal.pacman.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Menu extends JPanel implements KeyListener {


    private JFrame frame;
    private Rectangle play;
    private Rectangle about;
    private Rectangle highscore;


    public Menu(JFrame frame, int width, int height) {
        this.frame = frame;
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.YELLOW);


        play = new Rectangle(100, 100, 100, 50);


        this.setFocusable(true);
        this.addKeyListener(this);
        this.requestFocusInWindow();

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(play.x, play.y, play.width, play.height);


        g2.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("pressed");
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

          /*  JPanel p = new JPanel();
            p.setPreferredSize(this.getSize());
            p.setBackground(Color.BLUE);*/

            GamePanel gamePanel = new GamePanel();
            frame.remove(this);
            frame.add(gamePanel);
            gamePanel.addKeyListener(gamePanel);
            gamePanel.setFocusable(true);
            gamePanel.requestFocusInWindow();
            frame.validate();


            System.out.println("added blue");
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
