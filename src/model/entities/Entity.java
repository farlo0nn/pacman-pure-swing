package model.entities;

import model.utils.Collidable;


public abstract class Entity implements Collidable {
    protected Tile tile;
    protected Tile spawnTile;

    public Entity(int x, int y) {
        this.tile = new Tile(x, y);
        this.spawnTile = new Tile(x, y);
    }

    public void setPosition(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    public Tile getSpawnTile() {
        return spawnTile;
    }

    public void reset() {
        setPosition(spawnTile);
    }

    @Override
    public String toString() {
        return "(" + tile.x + "," + tile.y + ")";
    }
}
