package view.components;

import view.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class HudPanel extends JPanel {

    private final int scoreIndex = 0;
    private final int timeIndex = 1;
    private final int livesIndex = 2;

    public HudPanel() {

        setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.HUD_HEIGHT));
        setBackground(Color.BLACK);
        setLayout(new GridLayout(1, 3));
        JLabel scoreLabel = new JLabel();
        scoreLabel.setFont(new Font("Sans", Font.BOLD, 16));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        scoreLabel.setBackground(Color.BLACK);
        scoreLabel.setOpaque(true);

        add(scoreLabel, scoreIndex);

        JLabel timeLabel = new JLabel("0");
        timeLabel.setFont(new Font("Sans", Font.BOLD, 16));
        timeLabel.setForeground(Color.YELLOW);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setBackground(Color.BLACK);
        timeLabel.setOpaque(true);

        add(timeLabel, timeIndex);

        JLabel livesLabel = new JLabel();
        livesLabel.setFont(new Font("Sans", Font.BOLD, 16));
        livesLabel.setForeground(Color.YELLOW);
        livesLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        livesLabel.setBackground(Color.BLACK);
        livesLabel.setOpaque(true);

        add(livesLabel, livesIndex);

        revalidate();
        repaint();
    }

    public void update() {
        if (getComponent(timeIndex) instanceof JLabel label) { label.setText(String.valueOf(Integer.parseInt(label.getText()) + 1)); }
    }

    public void update(int score, int lives) {
        if (getComponent(scoreIndex) instanceof JLabel label) { label.setText("SCORE " + String.valueOf(score)); }
        if (getComponent(livesIndex) instanceof JLabel label) { label.setText("LIVES " + String.valueOf(lives)); }

    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(0, 25);
    }

}
