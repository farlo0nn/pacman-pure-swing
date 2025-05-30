package view.components;

import javax.swing.*;
import java.awt.*;

public class ToastMessage extends JDialog {

    public ToastMessage(JFrame parent, String message) {
        super(parent, false);
        setUndecorated(true);
        setLayout(new BorderLayout());

        JLabel label = new JLabel(message);
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        label.setBackground(new Color(255, 255, 204));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));

        add(label, BorderLayout.CENTER);
        pack();

        int x = parent.getX() + (parent.getWidth() - getWidth()) / 2;
        int y = parent.getY() + parent.getHeight() - getHeight() - 50;
        setLocation(x, y);

        setVisible(true);
    }

    public void close() {
        SwingUtilities.invokeLater(this::dispose);
    }
}
