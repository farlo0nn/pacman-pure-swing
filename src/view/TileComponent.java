package view;

import utils.EntityType;

import javax.swing.*;
import java.awt.*;

public class TileComponent extends JPanel {
    private EntityType type;
    private int size;
    private Image image;
    public int x;
    public int y;

    public TileComponent(EntityType type, int size) {
        this.type = type;
        this.size = size;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(size, size));
        updateAppearance();
    }

    public TileComponent(EntityType type, int size, Image image) {
        this.type = type;
        this.size = size;
        this.image = image;
        setSize(new Dimension(size, size));
        updateAppearance();

    }

    public EntityType getType() {
        return type;
    }

    private void setTexture() {
        switch (type) {
            case WALL -> setBackground(Color.BLUE);
            case PELLET -> setBackground(Color.DARK_GRAY);
            case POWER_PELLET -> setBackground(Color.GRAY);
            case PACMAN -> setBackground(Color.YELLOW);
            case YELLOW_GHOST -> setBackground(Color.ORANGE);
            case BLUE_GHOST -> setBackground(Color.CYAN);
            case PINK_GHOST -> setBackground(Color.PINK);
            case RED_GHOST -> setBackground(Color.RED);
            case PORTAL -> setBackground(Color.CYAN);
            default -> setBackground(Color.BLACK);
        }
    }

    private void setTexture(Image image) {
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(image));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        add(label, BorderLayout.CENTER);
    }

    private void updateAppearance() {
        removeAll();
        setBackground(Color.BLACK);

        if (image == null) {
            setTexture();
        } else {
            setTexture(image);
        }

        revalidate();
        repaint();
    }

}

