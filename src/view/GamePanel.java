package view;

import dto.EntityRenderData;

import model.utils.MovementDirection;
import utils.EntityType;
import view.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GamePanel extends JPanel {
    private int tileSize;
    private TileComponent[][] viewBoard;
    private char[][] map;
    private final int rows;
    private final int cols;
    private final Consumer<MovementDirection> directionConsumer;

    public GamePanel(int tileSize, Consumer<MovementDirection> directionConsumer) {

        this.directionConsumer = directionConsumer;
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.GAME_PANEL_HEIGHT));

        rows = getPreferredSize().height/tileSize;
        cols = getPreferredSize().width/tileSize;
        this.tileSize = tileSize;
        setLayout(new GridLayout(rows, cols));
        viewBoard = new TileComponent[rows][cols];

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
                viewBoard[row][col] = tile;
                this.add(tile);
            }
        }
    }

    private void setupInputHandling() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                MovementDirection requestedDirection = MovementDirection.NONE;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> requestedDirection = MovementDirection.UP;
                    case KeyEvent.VK_DOWN -> requestedDirection = MovementDirection.DOWN;
                    case KeyEvent.VK_LEFT -> requestedDirection = MovementDirection.LEFT;
                    case KeyEvent.VK_RIGHT -> requestedDirection = MovementDirection.RIGHT;
                }

                if(requestedDirection != MovementDirection.NONE) {
                    directionConsumer.accept(requestedDirection);
                }
            }
        });
    }

    public void renderBoard(ArrayList<EntityRenderData> dtos) {
        removeAll();

        for (EntityRenderData dto : dtos) {
            this.viewBoard[dto.y()][dto.x()] = new TileComponent(dto.type(), tileSize);
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                this.add(viewBoard[row][col]);
            }
        }

        revalidate();
        repaint();
    }


    private boolean isInBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public void setTile(int y, int x, TileComponent tileComponent) {
        viewBoard[y][x] = tileComponent;
    }

}
