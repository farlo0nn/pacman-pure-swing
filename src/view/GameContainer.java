package view;

import controller.utils.BoardSizes;
import dto.GameRenderData;
import model.utils.MovementDirection;
import utils.game.GameStatus;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class GameContainer extends JPanel {

    private GamePanel gamePanel;

    public GameContainer(int tileSize, Consumer<MovementDirection> directionConsumer) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // GamePanel (main game area)
        gamePanel = new GamePanel(tileSize, directionConsumer);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.95;
        add(gamePanel, gbc);

        // Score label
        JLabel scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Sans", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        scoreLabel.setBackground(Color.BLACK);
        scoreLabel.setOpaque(true);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.weighty = 0.05;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(scoreLabel, gbc);

        // Lives label
        JLabel livesLabel = new JLabel("Lives: 3");
        livesLabel.setFont(new Font("Sans", Font.BOLD, 16));
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        livesLabel.setBackground(Color.BLACK);
        livesLabel.setOpaque(true);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(livesLabel, gbc);

        setBackground(Color.BLACK);
    }

    public void update(GameRenderData dto) {
        gamePanel.renderBoard(dto.entities());
    }
}
