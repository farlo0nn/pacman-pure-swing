package model.entities;

import model.utils.GhostColor;
import utils.MovementDirection;

import java.util.ArrayList;
import java.util.Random;

public class RandomlyMovingGhost extends AnimatedEntity {
    private final ArrayList<Tile> exitPath;

    public Tile lastTurnTile;
    public GhostState state;
    private int exitPathId;
    private GhostColor color;

    public enum GhostState {
        EXITING_BOX,
        ROAMING
    }

    public RandomlyMovingGhost(int x, int y, GhostColor color, ArrayList<Tile> exitPath) {
        super(x, y, 2);
        reset();
        this.color = color;
        this.exitPath = exitPath;
    }

    @Override
    public void reset() {
        super.reset();
        lastTurnTile = null;
        state = GhostState.EXITING_BOX;
        direction = MovementDirection.UP;
        velocityX = 1;
        velocityY = 1;
        exitPathId = 0;
    }

    public void setDirection(MovementDirection direction) {
        this.direction = direction;
    }

    @Override
    public void update() {
        updateFrame();

        if (state == GhostState.EXITING_BOX) {
            moveToExit();
        } else {
            move();
        }
    }

    public void changeDirection(ArrayList<MovementDirection> directions) {
        this.direction = directions.get(new Random().nextInt(directions.size()));
    }
    public void changeDirection(MovementDirection direction) {
        this.direction = direction;
    }


    private void moveToExit() {

        if (exitPathId == exitPath.size()) {
            state = GhostState.ROAMING;
        }
        else {
            Tile next = exitPath.get(exitPathId);
            if (tile.x == next.x || tile.y == next.y) {
                getNextDirection(next);
                lastTurnTile = tile;
                exitPathId++;
            }
        }

        move();
    }

    public void getNextDirection(Tile tile){
        int changeX = tile.x - this.tile.x;
        int changeY = tile.y - this.tile.y;

        if (changeX > 0) direction = MovementDirection.RIGHT;
        else if (changeX < 0) direction = MovementDirection.LEFT;
        else if (changeY > 0) direction = MovementDirection.DOWN;
        else if (changeY < 0) direction = MovementDirection.UP;
    }

    public boolean exitingBox() {
        return state == GhostState.EXITING_BOX;
    }

    @Override
    public void stepBack() {
        super.stepBack();
        lastTurnTile = null;
    }

    public GhostColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "(" + tile.x + "," + tile.y + ")" + color + " " + exitingBox();
    }
}
