package view;


import utils.ImageManager;
import utils.EntityType;

import javax.swing.*;
import java.awt.*;

public class TileComponent extends JPanel {
    private EntityType type;
    private int size;
    public int x;
    public int y;

    public TileComponent(EntityType type, int size) {
        this.type = type;
        this.size = size;
        setPreferredSize(new Dimension(size, size));
        updateAppearance();
    }

    public void setType(EntityType newType) {
        this.type = newType;
        updateAppearance();
    }

    public EntityType getType() {
        return type;
    }

    private void updateAppearance() {
        removeAll();
        setBackground(Color.BLACK);

        switch (type) {
            case WALL -> setBackground(Color.BLUE);
//            case PELLET -> add(new JLabel(new ImageIcon(ImageManager.getPelletImage(size))));
//            case POWER_PELLET -> add(new JLabel(new ImageIcon(ImageManager.getPowerPelletImage(size))));
            case PACMAN -> setBackground(Color.YELLOW);
            case RED_GHOST -> setBackground(Color.RED);
            case PORTAL -> setBackground(Color.CYAN);
            default -> setBackground(Color.BLACK);
        }

        revalidate();
        repaint();
    }

}

