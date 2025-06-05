package view;

import model.utils.ExitListener;
import view.utils.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UIManager {

    private final JFrame menuFrame;
    private final JFrame gameFrame;
    private ExitListener exitListener;
    private JFrame currentFrame;

    public UIManager() {
        this.menuFrame = new JFrame("Menu");
        this.gameFrame = new JFrame("Pacman");

        setupFrame(menuFrame);
        menuFrame.setVisible(true);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupFrame(gameFrame);
        gameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (exitListener!=null) {
                    exitListener.onExitRequested();
                }
                gameFrame.setVisible(false);
                menuFrame.setVisible(true);
            }
        });

        currentFrame = menuFrame;
    }

    public void setExitRequestListener(ExitListener listener) {
        this.exitListener = listener;
    }


    public void setupFrame(JFrame frame) {
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(336, 414));
        frame.setLocationRelativeTo(null);
        frame.setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT));
        frame.pack();
    }

    public void changeFrame() {
        if (currentFrame == menuFrame) {
            menuFrame.setVisible(false);
            gameFrame.setVisible(true);
            menuFrame.setFocusable(false);
            currentFrame = gameFrame;
            currentFrame.setFocusable(true);
        } else {
            gameFrame.setVisible(false);
            gameFrame.setFocusable(false);
            gameFrame.dispose();
            menuFrame.setVisible(true);
            currentFrame = menuFrame;
            currentFrame.setFocusable(true);
        }
    }


    public void setPanel(JPanel panel) {
        currentFrame.setContentPane(panel);
        currentFrame.revalidate();
        currentFrame.repaint();
    }

    public String showDialogue() {
        String userName = JOptionPane.showInputDialog(gameFrame, "Enter your name:");
        return userName;
    }
}
