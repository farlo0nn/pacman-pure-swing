package model.entity;

import model.Collidable;

import java.awt.*;

public abstract class Entity implements Collidable {
    protected int x;
    protected int y;
    protected int spawnX;
    protected int spawnY;
    protected int width;
    protected int height;

    // size used, because I want every enemy to fit into
    // one tile, so size here is basically a tile size
    public Entity(int x, int y, int size) {
        this(x,y,size,size);
    }
    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.spawnX = x;
        this.spawnY = y;
        this.width = width;
        this.height = height;
    }

    public abstract void render(Graphics g);

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getXCoord() { return x; }
    public int getYCoord() { return y; }
    public int getSpawnX() { return spawnX; }
    public int getSpawnY() { return spawnY; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void reset() {
        setPosition(spawnX, spawnY);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
