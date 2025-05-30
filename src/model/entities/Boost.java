package model.entities;

import model.utils.BoostType;

public class Boost extends Entity {
    private final BoostType type;

    public Boost(int x, int y, BoostType type) {
        super(x, y);
        this.type = type;
    }

    public BoostType getType() {
        return this.type;
    }
}
