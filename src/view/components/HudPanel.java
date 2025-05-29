package view.components;

import dto.GameRenderData;
import view.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class HudPanel extends JPanel {

    int score;
    int lives;

    public HudPanel() {

        setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.HUD_HEIGHT));
        setBackground(Color.BLACK);
        setLayout(new GridLayout(1, 2));
        update(0, 0);
    }

    public void update(int score, int lives) {
        removeAll();

        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Sans", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        scoreLabel.setBackground(Color.BLACK);
        scoreLabel.setOpaque(true);

        add(scoreLabel);

        JLabel livesLabel = new JLabel("Lives: " + lives);
        livesLabel.setFont(new Font("Sans", Font.BOLD, 16));
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        livesLabel.setBackground(Color.BLACK);
        livesLabel.setOpaque(true);

        add(livesLabel);

        revalidate();
        repaint();
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, 25);
    }

}
