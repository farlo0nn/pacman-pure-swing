package model.entities;

import utils.MovementDirection;

public class RedGhost extends AnimatedEntity {

    private Tile target;

    public RedGhost(int x, int y) {
        super(x, y, 2);
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        target = null;
        direction = MovementDirection.NONE;
        velocityX = 1;
        velocityY = 1;
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

    public void getNextDirection(Tile t){
        int changeX = t.x - tile.x;
        int changeY = t.y - tile.y;

        if (changeX > 0) direction = MovementDirection.RIGHT;
        else if (changeX < 0) direction = MovementDirection.LEFT;
        else if (changeY > 0) direction = MovementDirection.DOWN;
        else if (changeY < 0) direction = MovementDirection.UP;
    }
}
