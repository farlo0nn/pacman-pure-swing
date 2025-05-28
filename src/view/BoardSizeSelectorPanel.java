package view;

import utils.ImageManager;
import controller.utils.BoardSizes;
import view.components.ImageLabelButton;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;



public class BoardSizeSelectorPanel extends JPanel {
    public static final int TILE_SIZE = 24;
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    public BoardSizeSelectorPanel(Consumer<BoardSizes> boardSizeCallback) {

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

        small.addActionListener(e -> boardSizeCallback.accept(BoardSizes.SMALL));
        medium.addActionListener(e -> boardSizeCallback.accept(BoardSizes.MEDIUM));
        large.addActionListener(e -> boardSizeCallback.accept(BoardSizes.LARGE));

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