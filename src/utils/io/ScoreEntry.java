package utils.io;

import utils.game.BoardSize;

import java.io.Serializable;

public class ScoreEntry implements Serializable, Comparable<ScoreEntry> {
    public String username;
    public BoardSize mapSize;
    public int score;

    public ScoreEntry(String username, BoardSize size, int score) {
        this.username = username;
        this.mapSize = size;
        this.score = score;

    }

    @Override
    public int compareTo(ScoreEntry other) {
        return other.score - this.score;
    }

    @Override
    public String toString() {
        return username + " " + mapSize + " " + score;
    }
}
