package utils.game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BoardLoader {

    public static char[][] loadBoard(String filePath) {
        List<char[]> lines = new ArrayList<>();

        try (InputStream inputStream = Objects.requireNonNull(BoardLoader.class.getResource(filePath)).openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.toCharArray());
            }
        } catch (IOException e) {
            // TODO log
            e.printStackTrace();
        }

        char[][] maze = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            maze[i] = lines.get(i);
        }

        return maze;
    }
}