package view;

import dto.GameRenderData;
import utils.game.BoardSize;
import view.api.GameView;
import view.components.HudPanel;
import view.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class GameContainer extends JPanel implements GameView {

    private final GamePanel gamePanel;
    private final HudPanel hudPanel;

    public GameContainer(BoardSize size) {
        int tileSize = 0;
        switch (size) {
            case SMALL -> tileSize = 24;
            case MEDIUM -> tileSize = 16;
            case LARGE -> tileSize = 12;
        }
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT));
        GridBagConstraints gbc = new GridBagConstraints();

        gamePanel = new GamePanel(tileSize);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.9;
        gbc.fill = GridBagConstraints.BOTH;
        add(gamePanel, gbc);

        hudPanel = new HudPanel();
        gbc.gridy = 1;
        gbc.weighty = 0.1;
        add(hudPanel, gbc);

        setBackground(Color.BLACK);
    }

    @Override
    public void render(GameRenderData dto) {
        hudPanel.update(dto.score(), dto.lives());
        gamePanel.renderBoard(dto.entities());
    }

    @Override
    public void updateHUD() {
        hudPanel.update();
    }

    @Override
    public void setInputListener(Consumer<Integer> inputConsumer){
        this.gamePanel.setInputConsumer(inputConsumer);
    }

}
