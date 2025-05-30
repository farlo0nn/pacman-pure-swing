package view;

import dto.EntityRenderData;

import utils.MovementDirection;
import utils.EntityType;
import utils.ImageManager;
import view.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.function.Consumer;

public class GamePanel extends JPanel {
    private final int tileSize;
    private HashMap<Map.Entry<Integer, Integer>, TileComponent> viewBoard;
    private char[][] map;
    private final int rows;
    private final int cols;
    private final Consumer<Integer> keyConsumer;
    private final ImageManager imageManager;
    private HashMap<EntityType, HashMap<MovementDirection, Image[]>> animatedFrames;
    private HashMap<EntityType, Image> staticFrames;

    public GamePanel(int tileSize, Consumer<Integer> keyConsumer) {
        this.imageManager = new ImageManager();
        this.animatedFrames = new HashMap<>();
        this.staticFrames = new HashMap<>();

        animatedFrames.put(EntityType.PACMAN, imageManager.getPacmanFrames(tileSize));
        animatedFrames.put(EntityType.RED_GHOST, imageManager.getGhostFrames(EntityType.RED_GHOST, tileSize));
        animatedFrames.put(EntityType.YELLOW_GHOST, imageManager.getGhostFrames(EntityType.YELLOW_GHOST, tileSize));
        animatedFrames.put(EntityType.BLUE_GHOST, imageManager.getGhostFrames(EntityType.BLUE_GHOST, tileSize));
        animatedFrames.put(EntityType.PINK_GHOST, imageManager.getGhostFrames(EntityType.PINK_GHOST, tileSize));
        staticFrames.put(EntityType.WALL, imageManager.getWallImage(tileSize));
        staticFrames.put(EntityType.PELLET, imageManager.getPelletImage(tileSize));
        staticFrames.put(EntityType.POWER_PELLET, imageManager.getPowerPelletImage(tileSize));
        staticFrames.put(EntityType.SCORE_BOOST, imageManager.getScoreBoostImage(tileSize));
        staticFrames.put(EntityType.SPEED_BOOST, imageManager.getSpeedBoostImage(tileSize));
        staticFrames.put(EntityType.LIVES_BOOST, imageManager.getLivesBoostImage(tileSize));

        this.keyConsumer = keyConsumer;
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.GAME_PANEL_HEIGHT));

        rows = getPreferredSize().height/tileSize;
        cols = getPreferredSize().width/tileSize;
        this.tileSize = tileSize;
        setLayout(new GridLayout(rows, cols));
        viewBoard = new HashMap<>();

        initEmptyBoard();

        setFocusable(true);
        requestFocusInWindow();
        setupInputHandling();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }


    private void initEmptyBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TileComponent tile = new TileComponent(EntityType.EMPTY, tileSize);
                Map.Entry<Integer, Integer> key = new AbstractMap.SimpleImmutableEntry<>(col, row);
                viewBoard.put(key, tile);
                this.add(tile);
            }
        }
    }

    private void setupInputHandling() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Integer key = null;
                key = e.getKeyCode();

                if(key != null) {
                    keyConsumer.accept(key);
                }
            }
        });
    }

    public void renderBoard(ArrayList<EntityRenderData> dtos) {
        removeAll();

        viewBoard = new HashMap<>();

        for (EntityRenderData dto : dtos) {
            TileComponent tile;
            if (dto.frame() != -1) {
                tile = new TileComponent(dto.type(), tileSize, animatedFrames.get(dto.type()).get(dto.direction())[dto.frame()]);
            } else {
                tile = new TileComponent(dto.type(), tileSize, staticFrames.get(dto.type()));
            }

            setTile(dto.x(), dto.y(), tile);
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Map.Entry<Integer, Integer> key = new AbstractMap.SimpleImmutableEntry<>(col, row);
                TileComponent tile = viewBoard.getOrDefault(key, new TileComponent(EntityType.EMPTY, tileSize));
                this.add(tile);
            }
        }

        revalidate();
        repaint();
    }


    private boolean isInBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public void setTile(int x, int y, TileComponent tile) {
        Map.Entry<Integer, Integer> key = new AbstractMap.SimpleImmutableEntry<>(x, y);
        viewBoard.put(key, tile);
    }

}
