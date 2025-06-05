package view;

import utils.EntityType;

import javax.swing.*;
import java.awt.*;

public class TileComponent extends JPanel {
    private EntityType type;
    private int size;
    private Image image;
    private JLabel label;
    public int x;
    public int y;

    public TileComponent(EntityType type, int size) {
        this.label = new JLabel();
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
    public void setType(EntityType type) {
        this.type = type;
        setTexture();
    }

    private void setTexture() {
        label.setIcon(null);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        switch (type) {
            case WALL -> label.setBackground(Color.BLUE);
            case PELLET -> label.setBackground(Color.DARK_GRAY);
            case POWER_PELLET -> label.setBackground(Color.GRAY);
            case PACMAN -> label.setBackground(Color.YELLOW);
            case YELLOW_GHOST -> label.setBackground(Color.ORANGE);
            case BLUE_GHOST -> label.setBackground(Color.CYAN);
            case PINK_GHOST -> label.setBackground(Color.PINK);
            case RED_GHOST -> label.setBackground(Color.RED);
            case PORTAL -> label.setBackground(Color.CYAN);
            default -> label.setBackground(Color.BLACK);
        }
        add(label, BorderLayout.CENTER);
    }

    private void setTexture(Image image) {
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

    public void updateImage(Image image) {
        if (image == null) {
            label.setIcon(null);
            return;
        }
        label.setIcon(new ImageIcon(image));
        label.setBackground(Color.BLACK);
    }
}

