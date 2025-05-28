package model.entities;

import java.awt.*;

public class Portal extends Entity {
    Portal other;

    public Portal(int x, int y) {
        super(x,y);
    }

    public void setOther(Portal portal) {
        this.other = portal;
    }

    public Portal getOther() {
        return other;
    }
}
