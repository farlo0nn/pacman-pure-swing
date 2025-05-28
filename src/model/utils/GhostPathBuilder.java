package model.utils;

import model.entities.Tile;
import java.util.*;

public class GhostPathBuilder {


    public static ArrayList<Tile> getPath(char[][] map, Tile start, Tile target) {

        HashSet<Tile> visited = new HashSet<>();
        Map<Tile, Integer> distances = new HashMap<>();
        Map<Tile, Tile> parent = new HashMap<>();
        Queue<Tile> queue = new ArrayDeque<>();

        visited.add(start);
        distances.put(start, 0);
        queue.add(start);


        while (!queue.isEmpty()) {
            Tile current = queue.poll();
            if (current.equals(target)) break;

            for (MovementDirection direction : MovementDirection.values()) {
                int nextX = current.x + direction.xDir;
                int nextY = current.y + direction.yDir;

                if (nextY < 0 || nextX < 0 || nextY >= map.length || nextX >= map[0].length || map[nextY][nextX] == 'X') continue;

                Tile tile = new Tile(nextX, nextY);
                Integer nextDistance = distances.get(current) + 1;

                if (!visited.contains(tile) || nextDistance < distances.get(tile) ) {
                    distances.put(tile, nextDistance);
                    parent.put(tile, current);
                    visited.add(tile);
                    queue.add(tile);
                }
            }
        }


        ArrayList<Tile> path = new ArrayList<>();
        Tile current = target;
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }

        // we build our path from the target tile
        path.removeLast();
        Collections.reverse(path);
//        System.out.println(start + " " + path);
        return compressPath(path);
//        return path;
    };

    public static ArrayList<Tile> compressPath(ArrayList<Tile> path) {
        ArrayList<Tile> compressed = new ArrayList<>();
        if (path.isEmpty()) return compressed;

        compressed.add(path.getFirst());

        for (int i = 1; i < path.size() - 1; i++) {
            Tile previous = path.get(i - 1);
            Tile current = path.get(i);
            Tile next = path.get(i + 1);

            int cpx = current.x - previous.x;
            int cpy = current.y - previous.y;
            int ncx = next.x - current.x;
            int ncy = next.y - current.y;

            if (cpx != ncx || cpy != ncy) {
                compressed.add(current);
            }
        }
        Tile target = path.get(path.size()-1);
        compressed.add(target);
        return compressed;
    }

    public static HashMap<Tile, ArrayList<MovementDirection>> getTurnPoints(char[][] map) {
        HashMap<Tile, ArrayList<MovementDirection>> turnPoints = new HashMap<>();

        for (int y = 1; y < map.length - 1; y++){
            for (int x = 1; x < map[0].length - 1; x++){
                if ( map[y][x] == 'X' || map[y][x] == '-') continue;

                HashMap<Tile, MovementDirection> notWalls = new HashMap<>();

                for (MovementDirection direction : MovementDirection.valuesWithoutLast())
                {
                    if (y+direction.yDir == 0 || x+direction.xDir == 0 || y+direction.yDir == map.length || x+direction.xDir == map[0].length) continue;
                    if (map[y+direction.yDir][x+direction.xDir] != 'X' && map[y+direction.yDir][x+direction.xDir] != '-') {
                        notWalls.put(new Tile(x+direction.xDir,y+direction.yDir), direction);
                    }
                }

                if(notWalls.size() == 2) {
                    ArrayList<Tile> keys = new ArrayList<>(notWalls.keySet());

                    if (keys.get(0).x - keys.get(1).x == 0 || keys.get(0).y - keys.get(1).y == 0) continue;
                    turnPoints.put(new Tile(x,y), new ArrayList<>(notWalls.values()));
                }
                else if (notWalls.size() > 2) {
                    turnPoints.put(new Tile(x,y), new ArrayList<>(notWalls.values()));
                }
            }
        }
        return turnPoints;
    }

    public static Tile getClosestTurnPoint(ArrayList<Tile> turnPoints, Tile curTile){
        Tile closestTurnPoint = null;
        double minDist = Double.MAX_VALUE;

        for (Tile turnPoint : turnPoints) {
            double dist = Math.pow(turnPoint.x - curTile.x, 2) + Math.pow(turnPoint.y - curTile.y, 2);
            if (dist < minDist) {
                minDist = dist;
                closestTurnPoint = turnPoint;
            }
        }

        return closestTurnPoint;
    }

}

