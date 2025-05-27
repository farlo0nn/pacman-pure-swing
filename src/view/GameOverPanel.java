package view;

import utils.ImageManager;
import utils.game.GameOverActions;
import view.components.ImageLabelButton;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;


public class GameOverPanel extends JPanel {


        public GameOverPanel(Consumer<GameOverActions> actionCallback) {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        setFocusable(true);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        Dimension buttonSize = new Dimension(192*2, 64*2);
        ImageIcon buttonIcon = ImageManager.getButtonIcon();

        ImageLabelButton restart = new ImageLabelButton("restart", buttonIcon, buttonSize);
        ImageLabelButton toMenu = new ImageLabelButton("to menu", buttonIcon, buttonSize);

        restart.addActionListener(e -> actionCallback.accept(GameOverActions.RESTART));
        toMenu.addActionListener(e -> actionCallback.accept(GameOverActions.TO_MENU));

        for (JButton button : new JButton[]{restart, toMenu}) {
            button.setMaximumSize(buttonSize);
            button.setPreferredSize(buttonSize);
            button.setMinimumSize(buttonSize);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(button);
            buttonPanel.add(Box.createVerticalStrut(30));
        }

        add(buttonPanel);

    }

}