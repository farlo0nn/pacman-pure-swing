package model;

import dto.GameExitData;
import model.api.GameModel;
import model.entities.*;
import model.utils.BoardLoader;
import utils.game.BoardSize;
import model.utils.GhostColor;
import model.utils.GhostPathBuilder;
import model.utils.BoostType;

import dto.EntityRenderData;
import dto.GameRenderData;

import utils.EntityType;
import utils.game.GameStatus;
import utils.MovementDirection;

import java.util.*;
import java.util.function.Consumer;

public class GameLogic implements GameModel {

    private char[][] map;
    private BoardSize boardSize;
    private HashSet<Block> walls;
    private RedGhost redGhost;
    private ArrayList<RandomlyMovingGhost> ghosts;
    private HashSet<Block> pellets;
    private HashSet<Block> powerPellets;
    private ArrayList<Portal> portals;
    private ArrayList<Boost> boosts;
    private Pacman pacman;
    private int score;
    private int lives;
    private int pelletsLeft;
    private GameStatus status;
    private final HashMap<Tile, ArrayList<MovementDirection>> turnPoints;
    private boolean speedBoosted;
    private Consumer<EntityType> boostsListener;
    private boolean pacmanLastUpdated;

    public GameLogic(BoardSize boardSize) {
        this.boardSize = boardSize;
        initSize(boardSize);
        this.turnPoints = GhostPathBuilder.getTurnPoints(map);
        initEntities(map);
        this.lives = 3;
        this.score = 0;
        this.status = GameStatus.RUNNING;
    }

    public void setBoostsListener(Consumer<EntityType> boostsListener) {
        this.boostsListener = boostsListener;
    }

    private void initSize(BoardSize boardSize) {
        switch (boardSize) {
            case SMALL -> {
                this.map = BoardLoader.loadBoard("/board/boards/small.csv");
                break;
            }
            case MEDIUM -> {
                this.map = BoardLoader.loadBoard("/board/boards/medium.csv");
                break;
            }
            case LARGE -> {
                this.map = BoardLoader.loadBoard("/board/boards/large.csv");
                break;
            }
        }
    }

    public void spawnBoost() {
        if (Math.random()<=0.25) {
            Tile boostSpawn = ghosts.get(new Random().nextInt(ghosts.size())).getTile();
            boosts.add(new Boost(boostSpawn.x, boostSpawn.y, BoostType.random()));
        }
    }

    private void wallCollisions() {
        boolean collisionInRequestedDirection = false;

        for (Block wall : walls) {

            if (redGhost.collides(wall)) {
                redGhost.stepBack();
            }

            if (pacman.collides(wall)) {
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

    private void boostsCollisions() {
        Iterator<Boost> iterator = boosts.iterator();
        while (iterator.hasNext()) {
            Boost boost = iterator.next();
            if (pacman.collides(boost)) {
                switch (boost.getType()){
                    case SPEED -> boostsListener.accept(EntityType.SPEED_BOOST);
                    case SCORE -> score+=200;
                    case LIVES -> lives+=1;
                }
                iterator.remove();
            }
        }
    }


    public void updateGhosts() {
        for (RandomlyMovingGhost ghost : ghosts) {
            if (!ghost.exitingBox() && !ghost.getTile().equals(ghost.lastTurnTile) && turnPoints.containsKey(ghost.getTile())){
                ghost.changeDirection(turnPoints.get(ghost.getTile()));
                ghost.lastTurnTile = new Tile(ghost.getTile().x, ghost.getTile().y);
            }

            for (Portal portal : portals) {
                if (ghost.collides(portal)) {
                    ghost.changeDirection(MovementDirection.opposite(ghost.getDirection()));
                    ghost.lastTurnTile = new Tile(ghost.getTile().x, ghost.getTile().y);
                }
            }

            ghost.update();
        }

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

    public void updatePacman(MovementDirection pacmanRequestedDirection) {
        if (pacmanRequestedDirection != null) {
            pacman.setRequestedDirection(pacmanRequestedDirection);
        }

        if (!pacmanLastUpdated || speedBoosted) {
            pacman.update();
            pacmanLastUpdated = true;
        }
        else {
            pacmanLastUpdated = false;
        }

        for (Portal portal : portals) {
            if (pacman.collides(portal)) {
                pacman.teleport(portal.getOther().getTile());
            }
        }


        for (RandomlyMovingGhost ghost : ghosts) {
            if (pacman.getTile().equals(ghost.getTile())) {
                resetAfterCollision();
            }
        }

        if (pacman.getTile().equals(redGhost.getTile())) {
            resetAfterCollision();
        }

    }

    private void pelletCollisions() {

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

    public GameRenderData update() {

        if (status != GameStatus.RUNNING) return null;

        if (lives == 0) {
            status = GameStatus.OVER;
            return null;
        }

        if (pelletsLeft == 0) {
            initEntities(map);
        }

        wallCollisions();
        boostsCollisions();
        pelletCollisions();

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

        for (Block wall : walls) {
            entities.add( new EntityRenderData(
                    EntityType.WALL,
                    wall.getTile().x,
                    wall.getTile().y,
                    MovementDirection.NONE,
                    -1
            ));
        }

        for (Block pellet : pellets) {
            entities.add( new EntityRenderData(
                    EntityType.PELLET,
                    pellet.getTile().x,
                    pellet.getTile().y,
                    MovementDirection.NONE,
                    -1
            ));
        }

        for (Block powerPellet : powerPellets) {
            entities.add( new EntityRenderData(
                    EntityType.POWER_PELLET,
                    powerPellet.getTile().x,
                    powerPellet.getTile().y,
                    MovementDirection.NONE,
                    -1
            ));
        }

        for (Boost boost : boosts) {

            EntityType type;
            switch (boost.getType()){
                case LIVES -> type = EntityType.LIVES_BOOST;
                case SCORE -> type = EntityType.SCORE_BOOST;
                default -> type = EntityType.SPEED_BOOST;
            }

            entities.add( new EntityRenderData(
                    type,
                    boost.getTile().x,
                    boost.getTile().y,
                    MovementDirection.NONE,
                    -1
            ));
        }

        for (RandomlyMovingGhost ghost : ghosts) {
            EntityType type;
            switch (ghost.getColor()) {
                case GhostColor.YELLOW -> type = EntityType.YELLOW_GHOST;
                case GhostColor.PINK -> type = EntityType.PINK_GHOST;
                default -> type = EntityType.BLUE_GHOST;
            }
            entities.add(new EntityRenderData(
                    type,
                    ghost.getTile().x,
                    ghost.getTile().y,
                    ghost.getDirection(),
                    ghost.getCurrentFrame()
            ));
        }

        entities.add( new EntityRenderData(
                EntityType.PACMAN,
                pacman.getTile().x,
                pacman.getTile().y,
                pacman.getDirection(),
                pacman.getCurrentFrame()
        ));

        entities.add( new EntityRenderData(
                EntityType.RED_GHOST,
                redGhost.getTile().x,
                redGhost.getTile().y,
                redGhost.getDirection(),
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
        boosts = new ArrayList<>();
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
                    pacman = new Pacman(x, y);
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

        if (portals.size() % 2 != 0){
            //TODO LOG

        }
        else {
            for (int i = 0; i < portals.size() - 1; i+=2) {
                portals.get(i).setOther(portals.get(i+1));
                portals.get(i+1).setOther(portals.get(i));
            }
        }
    }

    public void addGhost(int colId, int rowId, GhostColor color ){
        Tile tile = new Tile(colId, rowId);
        ArrayList<Tile> exitPath = GhostPathBuilder.getPath(map, tile, GhostPathBuilder.getClosestTurnPoint(new ArrayList<Tile>(turnPoints.keySet()), tile));
        RandomlyMovingGhost ghost = new RandomlyMovingGhost(colId, rowId, color, exitPath);
        ghosts.add(ghost);
    }

    @Override
    public void setSpeedBoosted(boolean boosted){
        this.speedBoosted = boosted;
    }

    @Override
    public GameExitData getGameInfo(){
        return new GameExitData(
                GameStatus.OVER,
                score,
                boardSize
        );
    }
}