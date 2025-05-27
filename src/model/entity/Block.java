package model.entity;

import java.awt.*;

public class Block extends Entity {
    Image image;

    public Block(Image image, int x, int y, int size) {
        super(x,y,size);
        this.image = image;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

}