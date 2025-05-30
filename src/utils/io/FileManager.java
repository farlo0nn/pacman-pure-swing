package utils.io;

import utils.game.BoardSize;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class FileManager {

    public FileManager() {
    }

    public ArrayList<String> loadScores() {
        ArrayList<String> scores = new ArrayList<>();
        try (FileReader reader = new FileReader(new File(Objects.requireNonNull(this.getClass().getResource("/scores/scores.txt")).getPath()))) {

            int i;
            StringBuilder stringBuilder = new StringBuilder();
            while((i = reader.read()) != -1){
                if ((char)i == '\n'){
                    String s = stringBuilder.toString();
                    stringBuilder = new StringBuilder();
                    scores.add(s);
                }
                else {
                    stringBuilder.append((char) i);
                }
            }

        } catch (IOException e){
            System.err.println("Failed to load score: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return scores;
    }

    public void saveScores(String username, BoardSize size, int score) {
        try (FileWriter writer = new FileWriter(new File(Objects.requireNonNull(this.getClass().getResource("/scores/scores.txt")).getPath()), true)){
            writer.write(username + " " +size.toString() + " " +String.valueOf(score) + "\n");
        } catch (IOException e) {
            System.err.println("Failed to save score: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
