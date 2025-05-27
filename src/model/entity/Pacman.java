package model.entity;

import utils.ImageManager;
import utils.game.MovementDirection;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Pacman extends AnimatedEntity {

    private MovementDirection requestedDirection;

    public Pacman(int x, int y, int size) {
        super(x, y, size);
        reset();
        setFrames();
    }

    @Override
    public void reset() {
        super.reset();
        direction = MovementDirection.RIGHT;
        requestedDirection = MovementDirection.STOP;
        velocityX = width/12;
        velocityY = height/12;
    }

    public void setRequestedDirection(MovementDirection direction) {
        this.requestedDirection = direction;
    }

    public MovementDirection getRequestedDirection() {
        return requestedDirection;
    }

    public void changeDirection() {
        if (requestedDirection == MovementDirection.STOP) return;
        direction = requestedDirection;
        requestedDirection = MovementDirection.STOP;
    }

    public void stop() {
        this.direction = MovementDirection.STOP;
        frameCounter = 0;
        currentFrame = 0;
    }

    @Override
    protected void setFrames() {
        BufferedImage[] images = ImageManager.getPacmanImages(width);

        frames.put(MovementDirection.RIGHT, new BufferedImage[]
        {
            images[0], images[1], images[2]
        });

        frames.put(MovementDirection.LEFT, new BufferedImage[]
        {
            images[0], images[3], images[4]
        });

        frames.put(MovementDirection.UP, new BufferedImage[]
        {
            images[0], images[5], images[6]
        });

        frames.put(MovementDirection.DOWN, new BufferedImage[]
        {
            images[0], images[7], images[8]
        });

        frames.put(MovementDirection.STOP, new BufferedImage[]{
            images[0]
        });
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

    public Rectangle getFutureBounds() {
        int futureXCoord = x;
        int futureYCoord = y;

        futureXCoord+=velocityX * requestedDirection.xDir;
        futureYCoord+=velocityY * requestedDirection.yDir;
        return new Rectangle(futureXCoord, futureYCoord, width, height);
    }

    public void teleport (int x, int y) {
        requestedDirection = MovementDirection.STOP;
        this.x = x + width*direction.xDir;
        this.y = y;
        System.out.println("After teleport " + x + " " + y );
    }
}
