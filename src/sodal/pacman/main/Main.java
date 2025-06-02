package sodal.pacman.main;
import sodal.pacman.gui.TheFrame;

public class Main {
    public static void main(String[] args) {
        new TheFrame();


        StringBuffer sb = new StringBuffer("00:00");
        System.out.println(sb);
        sb.replace(0, 2, "01");
        System.out.println(sb);
    }
}