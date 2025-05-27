package model.entity;

import utils.game.MovementDirection;
import utils.game.Tile;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public abstract class AnimatedEntity extends Entity {
    protected HashMap<MovementDirection, BufferedImage[]> frames;
    protected int currentFrame;
    protected int animationDelay = 10;
    protected int frameCounter = 0;
    protected MovementDirection direction;
    protected int velocityX;
    protected int velocityY;
    protected Tile tile;

    public AnimatedEntity(int x, int y, int size) {
        super(x, y, size);
        tile = new Tile(x/width, y/height);
        frames = new HashMap<MovementDirection, BufferedImage[]>();
    }

    protected abstract void setFrames();
    protected void updateFrame(){
        frameCounter++;
        if (frameCounter >= animationDelay) {
            currentFrame = (currentFrame + 1) % (frames.get(direction).length);
            frameCounter = 0;
        }
    }

    public void move() {
        move(true);
    }

    public boolean fullyFitsInTile() {
        boolean alignedX = x%width == 0;
        boolean alignedY = y%height == 0;
        return alignedX && alignedY;
    }


    public void move(boolean forward) {
        this.x += this.velocityX * (direction.xDir * (forward ? 1 : -1));
        this.y += this.velocityY * (direction.yDir * (forward ? 1 : -1));

        if (fullyFitsInTile()) {
            tile.x = x / width;
            tile.y = y / height;
        }
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
    public void setPosition(int x, int y) {
        super.setPosition(x,y);
        if (fullyFitsInTile()) {
            tile.x = x / width;
            tile.y = y / height;
        }
    }

    @Override
    public void reset() {
        tile = new Tile(x/width, y/height);
        super.reset();
        currentFrame = 0;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")" + "[" + tile.x + "," + tile.y + "]" + "<" + x%width + " " + y%height + ">" + "'" + direction + "'";
    }

    public abstract void update();
}
