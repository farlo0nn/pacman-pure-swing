package model.entity;

import utils.ImageManager;
import utils.game.GhostColor;
import utils.game.MovementDirection;
import utils.game.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class RandomlyMovingGhost extends AnimatedEntity {
    private final BufferedImage[] images;
    private final ArrayList<Tile> exitPath;

    public Tile lastTurnTile;
    public GhostState state;
    private int exitPathId;

    public enum GhostState {
        EXITING_BOX,
        ROAMING
    }

    public RandomlyMovingGhost(int x, int y, int size, GhostColor color, ArrayList<Tile> exitPath) {
        super(x, y, size);
        reset();
        this.exitPath = exitPath;
        this.images = ImageManager.getGhostImages(color, size);
        setFrames();
    }

    @Override
    public void reset() {
        super.reset();
        lastTurnTile = null;
        state = GhostState.EXITING_BOX;
        direction = MovementDirection.UP;
        velocityX = width/24;
        velocityY = width/24;
        exitPathId = 0;
    }

    @Override
    protected void setFrames() {

        frames.put(MovementDirection.RIGHT, new BufferedImage[]{
                images[0], images[1]
        });

        frames.put(MovementDirection.LEFT, new BufferedImage[]{
                images[4], images[5]
        });

        frames.put(MovementDirection.UP, new BufferedImage[]{
                images[6], images[7]
        });

        frames.put(MovementDirection.DOWN, new BufferedImage[]{
                images[2], images[3]
        });

        frames.put(MovementDirection.STOP, new BufferedImage[]{
                images[0]
        });

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

    @Override
    public void render(Graphics g) {
        g.drawImage(frames.get(direction)[currentFrame], x, y, width, height, null);
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

            if (fullyFitsInTile() && (tile.x == next.x || tile.y == next.y)) {
                getNextDirection(next);
                lastTurnTile = tile;

                exitPathId++;
            }
        }

        move();
    }

    public void getNextDirection(Tile w){
        int changeX = w.x - tile.x;
        int changeY = w.y - tile.y;

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
}
