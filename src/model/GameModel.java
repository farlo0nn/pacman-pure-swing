package model;

import dto.EntityRenderData;
import dto.GameRenderData;

import utils.EntityType;
import utils.game.GameStatus;
import model.utils.GhostColor;
import model.utils.GhostPathBuilder;
import model.utils.MovementDirection;

import model.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class GameModel {

    private final char[][] map;
    private int tileSize;
    private HashSet<Block> walls;
    private RedGhost redGhost;
    private ArrayList<RandomlyMovingGhost> ghosts;
    private HashSet<Block> pellets;
    private HashSet<Block> powerPellets;
    private ArrayList<Portal> portals;
    private Pacman pacman;
    private int score;
    private int lives;
    private int pelletsLeft;
    private GameStatus status;
    private final HashMap<Tile, ArrayList<MovementDirection>> turnPoints;

    public GameModel(char[][] map, int tileSize) {
        this.map = map;
        this.turnPoints = GhostPathBuilder.getTurnPoints(map);
        initEntities(map);
        this.lives = 3;
        this.score = 0;
        this.status = GameStatus.RUNNING;
    }

    public void wallCollisions() {
        boolean collisionInRequestedDirection = false;

        for (Block wall : walls) {

            if (redGhost.getTile().equals(wall.getTile())) {
                redGhost.stepBack();
            }

            if (pacman.getTile().equals(wall.getTile())) {
                pacman.stepBack();
                pacman.stop();
            }

            else if (pacman.getFutureTile().equals(wall.getTile())) {
                collisionInRequestedDirection = true;
            }

            for (RandomlyMovingGhost ghost : ghosts) {
                if (ghost.getTile().equals(wall.getTile())) {
                    ghost.stepBack();
                }
            }
        }

        if (!collisionInRequestedDirection) {
            pacman.changeDirection();
        }
    }

    private void portalCollisions() {
        for (Portal portal : portals) {
            if (pacman.getTile().equals(portal.getTile())) {
                pacman.teleport(portal.getOther().getTile());
            }
            for (RandomlyMovingGhost ghost : ghosts) {
                if (ghost.getTile().equals(portal.getTile())) {
                    ghost.changeDirection(MovementDirection.opposite(ghost.getDirection()));
                    ghost.lastTurnTile = new Tile(ghost.getTile().x, ghost.getTile().y);
                }
            }
        }
    }

    private void updateGhosts() {

        //randomly moving ghosts update
        for (RandomlyMovingGhost ghost : ghosts) {
            if (!ghost.exitingBox() && !ghost.getTile().equals(ghost.lastTurnTile) && turnPoints.containsKey(ghost.getTile())){
                ghost.changeDirection(turnPoints.get(ghost.getTile()));
                ghost.lastTurnTile = new Tile(ghost.getTile().x, ghost.getTile().y);
            }

            ghost.update();

        }

        // red ghost update
        if (redGhost.reachedTarget()) {
            ArrayList<Tile> path = GhostPathBuilder.getPath(
                    map,
                    redGhost.getTile(),
                    pacman.getTile()
            );

            if (!path.isEmpty()){
                Tile target = path.getFirst();
                redGhost.setTarget(target);
            }
        }

        redGhost.update();
    }

    private void updatePellets() {

        ArrayList<Block> pelletsToRemove = new ArrayList<>();
        for (Block pellet : pellets) {
            if (pacman.getTile().equals(pellet.getTile())){
                pelletsToRemove.add(pellet);
                score+=10;
            }
        }

        pelletsToRemove.forEach(pellets::remove);
        pelletsLeft-=pelletsToRemove.size();

        ArrayList<Block> powerPelletsToRemove = new ArrayList<>();
        for (Block powerPellet : powerPellets) {
            if (pacman.getTile().equals(powerPellet.getTile())){
                powerPelletsToRemove.add(powerPellet);
                score+=50;
            }
        }

        powerPelletsToRemove.forEach(powerPellets::remove);
        pelletsLeft-=powerPelletsToRemove.size();
    }

    public GameRenderData update(MovementDirection pacmanRequestedDirection) {

        if (status != GameStatus.RUNNING) return null;

        if (pacmanRequestedDirection != null) {
            pacman.setRequestedDirection(pacmanRequestedDirection);
        }

        if (lives == 0) {
            status = GameStatus.OVER;
            return null;
        }

        if (pelletsLeft == 0) {
            initEntities(map);
        }

        System.out.println(pacman);
        updateGhosts();
        pacman.update();

        wallCollisions();
        portalCollisions();

        updatePellets();

        for (RandomlyMovingGhost ghost : ghosts) {
            if (pacman.getTile().equals(ghost.getTile())) {
                // TODO
                resetAfterCollision();
            }
        }

        if (pacman.getTile().equals(redGhost.getTile())) {
            // TODO
            resetAfterCollision();
        }

        return toRenderDTO();
    }

    public void resetAfterCollision(){
        pacman.reset();
        ghosts.forEach(Entity::reset);
        redGhost.reset();
        lives--;

    }

    public GameRenderData toRenderDTO() {
        ArrayList<EntityRenderData> entities = new ArrayList<>();

//        for (RandomlyMovingGhost ghost : ghosts) {
//            EntityType type;
//            switch (ghost.getColor()) {
//                case GhostColor.YELLOW -> type = EntityType.YELLOW_GHOST;
//                case GhostColor.PINK -> type = EntityType.PINK_GHOST;
//                default -> type = EntityType.BLUE_GHOST;
//            }
//            entities.add(new EntityRenderData(
//                    type,
//                    ghost.getTile().x,
//                    ghost.getTile().y,
//                    ghost.getCurrentFrame()
//            ));
//        }

        for (Block wall : walls) {
            entities.add( new EntityRenderData(
                    EntityType.WALL,
                    wall.getTile().x,
                    wall.getTile().y,
                    -1
            ));
        }

        for (Block pellet : pellets) {
            entities.add( new EntityRenderData(
                    EntityType.PELLET,
                    pellet.getTile().x,
                    pellet.getTile().y,
                    -1
            ));
        }

        for (Block powerPellet : powerPellets) {
            entities.add( new EntityRenderData(
                    EntityType.POWER_PELLET,
                    powerPellet.getTile().x,
                    powerPellet.getTile().y,
                    -1
            ));
        }

        entities.add( new EntityRenderData(
                EntityType.PACMAN,
                pacman.getTile().x,
                pacman.getTile().y,
                pacman.getCurrentFrame()
        ));

        entities.add( new EntityRenderData(
                EntityType.RED_GHOST,
                redGhost.getTile().x,
                redGhost.getTile().y,
                redGhost.getCurrentFrame()
        ));


        return new GameRenderData(map, entities, score, lives);
    }

    public GameStatus getStatus() {
        return status;
    }

    public void initEntities(char[][] map) {
        ghosts = new ArrayList<RandomlyMovingGhost>();
        pellets = new HashSet<>();
        powerPellets = new HashSet<>();
        portals = new ArrayList<>();
        walls = new HashSet<>();


        pelletsLeft = 0;

        for (int rowId = 0; rowId < map.length; rowId++) {
            char[] row = map[rowId];
            for (int colId = 0; colId < map[0].length; colId++) {
                char value = row[colId];
                int x = colId ;
                int y = rowId ;
                if (value == 'X') {
                    walls.add(new Block(x, y));
                } else if (value == 'P') {
                    System.out.println("PACMAN INIT");
                    pacman = new Pacman(x, y);
                    System.out.println(pacman);
                } else if (value == '.') {
                    pellets.add(new Block(x, y));
                    pelletsLeft++;
                } else if (value == 'o') {
                    powerPellets.add(new Block(x, y));
                    pelletsLeft++;
                } else if (value == 'r') {
                    redGhost = new RedGhost(x, y);
                } else if (value == 'p') {
                    addGhost(colId, rowId, GhostColor.PINK);
                } else if (value == 'b') {
                    addGhost(colId, rowId, GhostColor.BLUE);
                } else if (value == 'y') {
                    addGhost(colId, rowId, GhostColor.YELLOW);
                } else if (value == 'O') {
                    portals.add(new Portal(x, y));
                }
            }
        }
    }

    public void addGhost(int colId, int rowId, GhostColor color ){
        Tile tile = new Tile(rowId, colId);
        ArrayList<Tile> exitPath = GhostPathBuilder.getPath(map, tile, GhostPathBuilder.getClosestTurnPoint(new ArrayList<Tile>(turnPoints.keySet()), tile));

        for (Tile tile1 : exitPath) {
            System.out.println(tile1);
        }

        ghosts.add(new RandomlyMovingGhost(colId, rowId, color, exitPath));
    }
}

