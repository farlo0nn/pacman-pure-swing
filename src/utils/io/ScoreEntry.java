package utils.io;

import utils.game.BoardSize;

public class ScoreEntry implements Comparable<ScoreEntry> {
    String username;
    BoardSize size;
    int score;

    public ScoreEntry(String username, BoardSize size, int score) {
        this.username = username;
        this.size = size;
        this.score = score;

    }

    @Override
    public int compareTo(ScoreEntry other) {
        return other.score - this.score;
    }

    @Override
    public String toString() {
        return username + " " + size + " " + score;
    }
}
