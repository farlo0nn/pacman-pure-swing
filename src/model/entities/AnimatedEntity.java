package model.entities;

import utils.MovementDirection;

public abstract class AnimatedEntity extends Entity {
    protected int currentFrame;
    protected int animationDelay = 1;
    protected int frameCounter = 0;
    protected MovementDirection direction;
    protected int velocityX;
    protected int velocityY;
    protected int frameNumber;

    public AnimatedEntity(int x, int y, int frameNumber) {
        super(x, y);
        this.frameNumber = frameNumber;
    }

    protected void updateFrame(){
        frameCounter++;
        if (frameCounter >= animationDelay) {
            currentFrame = (currentFrame + 1) % (frameNumber);
            frameCounter = 0;
        }
    }

    public void move() {
        move(true);
    }


    public void move(boolean forward) {
        this.tile.x += this.velocityX * (direction.xDir * (forward ? 1 : -1));
        this.tile.y += this.velocityY * (direction.yDir * (forward ? 1 : -1));

    }

    public MovementDirection getDirection() {
        return direction;
    }

    public Tile getTile() {
        return this.tile;
    }

    public void stepBack() {
        move(false);
    }

    @Override
    public void reset() {
        super.reset();
        currentFrame = 0;
    }

    @Override
    public String toString() {
        return "(" + tile.x + "," + tile.y + ")" + " [" + direction + "]";
    }

    public abstract void update();
    public int getCurrentFrame() {
        switch (getDirection()) {
            case NONE -> {
                return 0;
            }
            default -> {
                return currentFrame % frameNumber;
            }
        }
    }
}
