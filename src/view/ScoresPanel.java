package view;

import utils.ImageManager;
import utils.game.ScoresActions;
import view.components.ImageLabelButton;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;



public class ScoresPanel extends JPanel {
    public static final int TILE_SIZE = 24;
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    public ScoresPanel(Consumer<ScoresActions> scoresActionsConsumer) {

        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        setFocusable(true);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        Dimension buttonSize = new Dimension(192*2, 64*2);
        ImageIcon buttonIcon = ImageManager.getButtonIcon();

        ImageLabelButton return_ = new ImageLabelButton("return", buttonIcon, buttonSize);

        return_.addActionListener(e -> scoresActionsConsumer.accept(ScoresActions.TO_MENU));

        return_.setMaximumSize(buttonSize);
        return_.setPreferredSize(buttonSize);
        return_.setMinimumSize(buttonSize);
        return_.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(return_);
        buttonPanel.add(Box.createVerticalStrut(30));

        add(buttonPanel);

    }

}