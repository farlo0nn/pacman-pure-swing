package utils.io;

import utils.game.BoardSize;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FileManager {

    public FileManager() {
    }

    public List<String> loadScores() {
        File file = new File(Objects.requireNonNull(this.getClass().getResource("/scores/scores.txt")).getPath());

        List<String> scores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader((new FileReader(file)))) {

            String line;
            while((line = reader.readLine()) != null){
                scores.add(line);
            }

        } catch (IOException e){
            System.err.println("Failed to load score: " + e.getMessage());
            throw new RuntimeException(e);
        }

        return scores;
    }

    public void saveScores(String username, BoardSize size, int newScore) {

        File file = new File(Objects.requireNonNull(this.getClass().getResource("/scores/scores.txt")).getPath());

        List<ScoreEntry> entries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    String user = parts[0];
                    BoardSize boardSize = BoardSize.valueOf(parts[1]);
                    int score = Integer.parseInt(parts[2]);
                    entries.add(new ScoreEntry(user, boardSize, score));
                }
            }
        } catch (IOException e) {
            System.err.println("failed to load scores: " + e.getMessage());
        }

        ScoreEntry newEntry = new ScoreEntry(username, size, newScore);

        boolean updatedEntry = false;

        for (ScoreEntry entry : entries) {
            if (
                newEntry.username.equals(entry.username) &&
                newEntry.size.equals(entry.size) &&
                newEntry.score > entry.score
            ) {
                entry.score = newEntry.score;
                updatedEntry = true;
            }
        }

        if (!updatedEntry) {
            entries.add(newEntry);
        }

        Collections.sort(entries);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (ScoreEntry entry : entries) {
                writer.write(entry.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("failed to save scores: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
