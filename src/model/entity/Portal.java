package model.entity;

import java.awt.*;

public class Portal extends Entity {
    Portal other;

    public Portal(int x, int y, int width, int height) {
        super(x,y,width, height);
    }

    public void setOther(Portal portal) {
        this.other = portal;
    }

    public Portal getOther() {
        return other;
    }

    @Override
    public void render(Graphics g) {
        g.drawLine(x, y, x, y+height);
    }
}
