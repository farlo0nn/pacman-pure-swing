package view;

import utils.ImageManager;
import utils.game.BoardSize;
import view.components.ImageLabelButton;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;



public class BoardSizeSelectorPanel extends JPanel {

    public BoardSizeSelectorPanel(Consumer<BoardSize> boardSizeCallback) {

        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        setFocusable(true);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        Dimension buttonSize = new Dimension(192*2, 64*2);
        ImageIcon buttonIcon = ImageManager.getButtonIcon();

        ImageLabelButton small = new ImageLabelButton("small", buttonIcon, buttonSize);
        ImageLabelButton medium = new ImageLabelButton("medium", buttonIcon, buttonSize);
        ImageLabelButton large = new ImageLabelButton("large", buttonIcon, buttonSize);

        small.addActionListener(e -> boardSizeCallback.accept(BoardSize.SMALL));
        medium.addActionListener(e -> boardSizeCallback.accept(BoardSize.MEDIUM));
        large.addActionListener(e -> boardSizeCallback.accept(BoardSize.LARGE));

        for (JButton button : new JButton[]{small, medium, large}) {
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