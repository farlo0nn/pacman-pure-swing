package view;
import utils.ImageManager;
import utils.menu.MainMenuActions;
import view.components.ImageLabelButton;

import javax.swing.*;
import java.awt.*;

import java.util.function.Consumer;

public class MenuPanel extends JPanel {

    public MenuPanel(Consumer<MainMenuActions> actionConsumer) {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        setFocusable(true);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        Dimension buttonSize = new Dimension(192*2, 64*2);
        ImageIcon buttonIcon = ImageManager.getButtonIcon();

        ImageLabelButton start = new ImageLabelButton("start", buttonIcon, buttonSize);
        start.setPreferredSize(buttonSize);
        ImageLabelButton scores = new ImageLabelButton("scores", buttonIcon, buttonSize);
        ImageLabelButton exit = new ImageLabelButton("exit", buttonIcon, buttonSize);

        start.addActionListener(e -> actionConsumer.accept(MainMenuActions.START));
        scores.addActionListener(e -> actionConsumer.accept(MainMenuActions.SCORES));
        exit.addActionListener(e -> actionConsumer.accept(MainMenuActions.EXIT));

        for (JButton button : new JButton[]{start, scores, exit}) {
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