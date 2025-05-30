package model.entities;

import model.utils.Collidable;


public abstract class Entity implements Collidable {
    protected Tile tile;
    protected Tile spawnTile;

    public Entity(int x, int y) {
        this.tile = new Tile(x, y);
        this.spawnTile = new Tile(x, y);
    }

    @Override
    public Tile getTile() {
        return tile;
    }

    @Override
    public boolean collides(Collidable other) {
        if(this.getTile().equals(other.getTile())){
            return true;
        }
        return false;
    }

    public void reset() {
        this.tile = new Tile(spawnTile.x, spawnTile.y);
    }

    @Override
    public String toString() {
        return "(" + tile.x + "," + tile.y + ")";
    }


}
