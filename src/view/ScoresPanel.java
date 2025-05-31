package view;

import utils.ImageManager;
import utils.game.ScoresActions;
import view.components.ImageLabelButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class ScoresPanel extends JPanel {

    public ScoresPanel(List<String> scores, Consumer<ScoresActions> scoresActionsConsumer) {

        setBackground(Color.BLACK);
        setLayout(new GridLayout(2,1));

        setFocusable(true);

        JTextArea textArea = new JTextArea(400, 40);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 20));

        for (String score : scores) {
            textArea.append(score + "\n");
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBackground(Color.BLACK);
        scrollPane.getViewport().setBackground(Color.BLACK);

        this.add(scrollPane);

        JPanel buttonPanel = new JPanel(new GridBagLayout());

        buttonPanel.setBackground(Color.BLACK);

        Dimension buttonSize = new Dimension(192*2, 64*2);
        ImageIcon buttonIcon = ImageManager.getButtonIcon();

        ImageLabelButton returnButton = new ImageLabelButton("return", buttonIcon, buttonSize);

        returnButton.addActionListener(e -> scoresActionsConsumer.accept(ScoresActions.TO_MENU));

        returnButton.setPreferredSize(buttonSize);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(returnButton, gbc);

        this.add(buttonPanel);

    }

}