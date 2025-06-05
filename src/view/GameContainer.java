package view;

import dto.EntityRenderData;
import dto.GameRenderData;
import utils.game.BoardSize;
import view.api.GameView;
import view.components.HudPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GameContainer extends JPanel implements GameView {

    private final GamePanel gamePanel;
    private final HudPanel hudPanel;

    public GameContainer(BoardSize size) {
        int tileSize = 0;
        switch (size) {
            case SMALL -> tileSize =48;
            case MEDIUM -> tileSize = 24;
            case LARGE -> tileSize = 16;
        }

        gamePanel = new GamePanel(tileSize);
        hudPanel = new HudPanel();

        setLayout(new BorderLayout());
        add(hudPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
    }

    @Override
    public void render(GameRenderData dto) {
        hudPanel.update(dto.score(), dto.lives());
        gamePanel.renderBoard(dto.entities());
        if (dto.pelletsLeft() == 0) {
            gamePanel.initBasicBoard();
        }
    }

    @Override
    public void updateHUD() {
        hudPanel.update();
    }

    @Override
    public void setInputListener(Consumer<Integer> inputConsumer){
        this.gamePanel.setInputConsumer(inputConsumer);
    }

    @Override
    public void setStaticTiles(ArrayList<EntityRenderData> staticTiles) {
        this.gamePanel.setStaticTiles(staticTiles);
    }

}
