package model.entities;

import utils.MovementDirection;

public class Pacman extends AnimatedEntity {

    private MovementDirection requestedDirection;

    public Pacman(int x, int y) {
        super(x, y, 3);
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        direction = MovementDirection.RIGHT;
        requestedDirection = MovementDirection.NONE;
        velocityX = 1;
        velocityY = 1;
    }

    public void setRequestedDirection(MovementDirection direction) {
        this.requestedDirection = direction;
    }

    public MovementDirection getRequestedDirection() {
        return requestedDirection;
    }

    public void changeDirection() {
        if (requestedDirection == MovementDirection.NONE) return;
        direction = requestedDirection;
        requestedDirection = MovementDirection.NONE;
    }

    public void stop() {
        this.direction = MovementDirection.NONE;
        frameCounter = 0;
        currentFrame = 0;
    }


    @Override
    public void update() {
        updateFrame();
        move();
    }

    public Tile getFutureTile() {
        int futureXCoord = tile.x;
        int futureYCoord = tile.y;

        futureXCoord+=velocityX * requestedDirection.xDir;
        futureYCoord+=velocityY * requestedDirection.yDir;
        return new Tile(futureXCoord, futureYCoord);
    }

    public void teleport (Tile t) {
        requestedDirection = MovementDirection.NONE;
        this.tile.x = t.x + direction.xDir;
        this.tile.y = t.y;
    }
}