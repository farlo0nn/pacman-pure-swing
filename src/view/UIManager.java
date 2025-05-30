package view;

import view.utils.UIConstants;

import javax.swing.*;
import java.awt.*;

public class UIManager {

    private final JFrame frame;

    public UIManager() {
        this.frame = new JFrame("Pacman");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT));
        frame.setBackground(Color.BLACK);
        frame.pack();
        frame.setVisible(true);
    }

    public void setPanel(JPanel panel) {
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
    }

    public String showDialogue() {
        String userName = JOptionPane.showInputDialog(frame, "Enter your name:");
        return userName;
    }
}
