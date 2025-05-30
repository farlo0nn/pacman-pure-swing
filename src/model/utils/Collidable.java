package model.utils;

import model.entities.Tile;

public interface Collidable {
    Tile getTile();
    boolean collides(Collidable other);
}
