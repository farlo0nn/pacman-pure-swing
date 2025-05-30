package view;

import dto.GameRenderData;
import utils.MovementDirection;
import view.components.HudPanel;
import view.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class GameContainer extends JPanel {

    private GamePanel gamePanel;
    private HudPanel hudPanel;

    public GameContainer(int tileSize, Consumer<Integer> keyConsumer) {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT));
        GridBagConstraints gbc = new GridBagConstraints();

        gamePanel = new GamePanel(tileSize, keyConsumer);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
//        gbc.gridheight = 1;
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

    public void update(GameRenderData dto) {
        hudPanel.update(dto.score(), dto.lives());
        gamePanel.renderBoard(dto.entities());
    }

    public void updateHud() {
        hudPanel.update();
    }
}
