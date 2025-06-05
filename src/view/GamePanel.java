package view;

import dto.EntityRenderData;

import utils.MovementDirection;
import utils.EntityType;
import utils.ImageManager;
import view.utils.Coord;
import view.utils.Pair;
import view.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.function.Consumer;

public class GamePanel extends JPanel {
    private final int tileSize;
    private final TileComponent[][] viewBoard;
    private HashMap<EntityType, Pair<Coord, EntityType>> hoveredCells;
    private final int rows;
    private final int cols;
    private Consumer<Integer> inputConsumer;
    private HashMap<EntityType, HashMap<MovementDirection, BufferedImage[]>> animatedFrames;
    private HashMap<EntityType, BufferedImage> staticFrames;
    private Dimension currentSize;
    private ArrayList<EntityRenderData> staticTiles;
    private final ImageManager imageManager;

    public GamePanel(int tileSize) {

        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.GAME_PANEL_HEIGHT));
        hoveredCells = new HashMap<>();
        imageManager = new ImageManager();

        initFrames(tileSize);

        rows = getPreferredSize().height/tileSize;
        cols = getPreferredSize().width/tileSize;
        this.tileSize = tileSize;
        setLayout(new GridLayout(rows, cols));
        viewBoard = new TileComponent[rows][cols];

        setFocusable(true);
        requestFocusInWindow();
        setupInputHandling();
    }

    public void initFrames(int width, int height){

        this.animatedFrames = new HashMap<>();
        this.staticFrames = new HashMap<>();

        animatedFrames.put(EntityType.PACMAN, imageManager.getPacmanFrames(width, height));
        animatedFrames.put(EntityType.RED_GHOST, imageManager.getGhostFrames(EntityType.RED_GHOST, width, height));
        animatedFrames.put(EntityType.YELLOW_GHOST, imageManager.getGhostFrames(EntityType.YELLOW_GHOST, width, height));
        animatedFrames.put(EntityType.BLUE_GHOST, imageManager.getGhostFrames(EntityType.BLUE_GHOST, width, height));
        animatedFrames.put(EntityType.PINK_GHOST, imageManager.getGhostFrames(EntityType.PINK_GHOST, width, height));
        staticFrames.put(EntityType.WALL, imageManager.getWallImage(width, height));
        staticFrames.put(EntityType.PELLET, imageManager.getPelletImage(width, height));
        staticFrames.put(EntityType.POWER_PELLET, imageManager.getPowerPelletImage(width, height));
        staticFrames.put(EntityType.SCORE_BOOST, imageManager.getBoostImage(EntityType.SCORE_BOOST, width, height));
        staticFrames.put(EntityType.SPEED_BOOST, imageManager.getBoostImage(EntityType.SPEED_BOOST, width, height));
        staticFrames.put(EntityType.LIVES_BOOST, imageManager.getBoostImage(EntityType.LIVES_BOOST, width, height));
    }

    public void initFrames(int tileSize) {
        initFrames(tileSize, tileSize);
    }

    public void setInputConsumer(Consumer<Integer> inputConsumer){
        this.inputConsumer = inputConsumer;
    }


    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    private void setupInputHandling() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Integer key = null;
                key = e.getKeyCode();
                inputConsumer.accept(key);
            }
        });
    }

    public void renderBoard(ArrayList<EntityRenderData> dtos) {

        if (!this.getSize().equals(this.currentSize)) {

            currentSize = this.getSize();
            initFrames(currentSize.width/cols, currentSize.height/this.rows);

            for (Pair<Coord, EntityType> p : hoveredCells.values()) {
                viewBoard[p.getLeft().y()][p.getLeft().x()].setType(p.getRight());
                viewBoard[p.getLeft().y()][p.getLeft().x()].updateImage(staticFrames.get(p.getRight()));
            }

            for (TileComponent[] tileComponents : viewBoard) {
                for (int col = 0; col < viewBoard[0].length; col++) {
                    if (tileComponents[col] != null) {
                        EntityType currentType = tileComponents[col].getType();
                        if (currentType != EntityType.EMPTY && staticFrames.containsKey(currentType)) {
                            tileComponents[col].updateImage(staticFrames.get(currentType));
                        }
                    }
                }
            }

        }

        for(EntityRenderData dto : dtos) {
            if (viewBoard[dto.y()][dto.x()] != null) {
                if (EntityType.isGhost(dto.type())) {
                    Pair<Coord, EntityType> p;
                    if ((p = hoveredCells.get(dto.type())) != null) {
                        viewBoard[p.getLeft().y()][p.getLeft().x()].setType(p.getRight());
                        viewBoard[p.getLeft().y()][p.getLeft().x()].updateImage(staticFrames.get(p.getRight()));
                    }
                    if (!EntityType.isGhost(viewBoard[dto.y()][dto.x()].getType())) {
                        hoveredCells.put(dto.type(), new Pair<>( new Coord(dto.x(), dto.y()),viewBoard[dto.y()][dto.x()].getType()));
                    }
                } else if (dto.type() == EntityType.PACMAN) {
                    Pair<Coord, EntityType> p;
                    if ((p = hoveredCells.get(dto.type())) != null) {
                        viewBoard[p.getLeft().y()][p.getLeft().x()].setType(EntityType.EMPTY);
                        viewBoard[p.getLeft().y()][p.getLeft().x()].updateImage(null);
                    }
                    hoveredCells.put(dto.type(), new Pair<>( new Coord(dto.x(), dto.y()),EntityType.EMPTY));
                }

                Image img = dto.frame() != -1
                        ? animatedFrames.get(dto.type()).get(dto.direction())[dto.frame()]
                        : staticFrames.get(dto.type());
                viewBoard[dto.y()][dto.x()].setType(dto.type());
                viewBoard[dto.y()][dto.x()].updateImage(img);
            }
        }

    }

    public void setStaticTiles(ArrayList<EntityRenderData> staticTiles) {
        this.staticTiles = staticTiles;
        initBasicBoard();
    }

    public void initBasicBoard() {
        removeAll();
        for(int rows = 0; rows < viewBoard.length; rows ++) {
            for(int cols = 0; cols < viewBoard[0].length; cols ++) {
                viewBoard[rows][cols] = new TileComponent(EntityType.EMPTY,tileSize);
                this.add(viewBoard[rows][cols]);
            }
        }

        for (EntityRenderData dto : staticTiles) {
            viewBoard[dto.y()][dto.x()].setType(dto.type());
            viewBoard[dto.y()][dto.x()].updateImage(staticFrames.get(dto.type()));
        }

    }
}
