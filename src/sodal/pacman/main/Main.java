package sodal.pacman.main;
import sodal.pacman.gui.TheFrame;

public class Main {
    public static void main(String[] args) {
        new TheFrame();

        int age = 25;

        System.out.println(String.format("%02d", age));

    }
}