package tests;
import model.utils.BoardLoader;
import model.entities.Tile;
import model.utils.GhostPathBuilder;
import org.junit.jupiter.api.Test;
import utils.game.BoardSize;
import utils.io.FileManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class Tests {

    @Test
    void pathFind() {
        char[][] map = BoardLoader.loadBoard("/board/boards/small.csv");
        ArrayList<Tile> path = GhostPathBuilder.getPath(map, new Tile(14,11), new Tile(13, 23));
        for (Tile tile : path) {
            System.out.println(tile);
        }

        assertEquals(1,1);

    }

    @Test
    void saveScore() {
        FileManager fileManager = new FileManager();
        fileManager.saveScores("Farlo0n", BoardSize.SMALL, 140);
    }

    @Test
    void loadScores() {
        FileManager fileManager = new FileManager();
        ArrayList<String> scores = fileManager.loadScores();
        for (String score : scores) {
            System.out.println(score);
        }
    }
}
