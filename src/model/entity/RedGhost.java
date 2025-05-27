package model.entity;

import utils.ImageManager;
import utils.game.GhostColor;
import utils.game.MovementDirection;
import utils.game.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RedGhost extends AnimatedEntity {

    private Tile target;

    public RedGhost(int x, int y, int size) {
        super(x, y, size);
        reset();
        setFrames();
    }

    @Override
    public void reset() {
        super.reset();
        target = null;
        direction = MovementDirection.STOP;
        velocityX = width/24;
        velocityY = width/24;
    }

    @Override
    protected void setFrames() {
        BufferedImage[] images = ImageManager.getGhostImages(GhostColor.RED, width);

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

    public boolean reachedTarget() {

        if(target == null) return true;

        if (target.equals(tile)) {
            return true;
        }
        else {
            return false;
        }
    }

    public void setTarget(Tile target) {
        this.target = target;
        getNextDirection(target);
    }

    @Override
    public void update() {
        updateFrame();
        move();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(frames.get(direction)[currentFrame], x, y, width, height, null);
    }

    public void getNextDirection(Tile t){
        int changeX = t.x - tile.x;
        int changeY = t.y - tile.y;

        if (changeX > 0) direction = MovementDirection.RIGHT;
        else if (changeX < 0) direction = MovementDirection.LEFT;
        else if (changeY > 0) direction = MovementDirection.DOWN;
        else if (changeY < 0) direction = MovementDirection.UP;
    }
}
