package tests;
import model.utils.BoardLoader;
import model.entities.Tile;
import model.utils.GhostPathBuilder;
import org.junit.jupiter.api.Test;

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
}
