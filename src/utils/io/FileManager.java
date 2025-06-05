package utils.io;

import utils.game.BoardSize;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FileManager {

    public List<ScoreEntry> loadScores() {
        File file = new File(Objects.requireNonNull(this.getClass().getResource("/scores/scores.bin")).getPath());

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<ScoreEntry>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("failed to load scores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveScores(String username, BoardSize size, int newScore) {

        File file = new File(Objects.requireNonNull(this.getClass().getResource("/scores/scores.bin")).getPath());

        List<ScoreEntry> entries = loadScores();
        ScoreEntry newEntry = new ScoreEntry(username.strip(), size, newScore);

        boolean updated = false;
        for (ScoreEntry entry : entries) {
            if (entry.username.equals(newEntry.username) && entry.mapSize == newEntry.mapSize) {
                if (newEntry.score > entry.score) {
                    entry.score = newEntry.score;
                }
                updated = true;
                break;
            }
        }

        if (!updated) {
            entries.add(newEntry);
        }

        Collections.sort(entries);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(entries);
        } catch (IOException e) {
            System.out.println("failed to save scores: " + e.getMessage());
        }
    }
}
