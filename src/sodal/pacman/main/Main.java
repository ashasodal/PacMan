package sodal.pacman.main;
import sodal.pacman.gui.TheFrame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new TheFrame();

       // new Main().readHighScoreFile();



    }


    private void writeToHighscoreFile() {
        try {
            FileWriter myWriter = new FileWriter("src/sodal/pacman/highscore/highscore.txt");
            myWriter.write("Files in Java might be tricky, but it is fun enough!");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }



    private void readHighScoreFile() {
        try {
            File myObj = new File("src/sodal/pacman/highscore/highscore.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.equals("No high score yet.")) {
                    //update highscore
                    System.out.println("update highscore!!!");
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}