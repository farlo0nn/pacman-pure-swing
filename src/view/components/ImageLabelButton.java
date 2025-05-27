package view.components;

import utils.ImageManager;

import javax.swing.*;
import java.awt.*;

public class ImageLabelButton extends JButton {
    private JLabel label;

    public ImageLabelButton(String labelText, ImageIcon icon, Dimension dim) {
        super();
        this.setLayout(new BorderLayout());
        Image scaledImage = icon.getImage().getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH);

        this.setIcon(new ImageIcon((scaledImage)));
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.BOTTOM);

        label = new JLabel(labelText, SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Arial", Font.BOLD, 36));
        JPanel labelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        labelWrapper.setOpaque(false);
        labelWrapper.add(label);
        this.add(labelWrapper, BorderLayout.CENTER);

        this.setContentAreaFilled(false);
        this.setBorderPainted(false);

        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setOpaque(false);
        this.setRolloverEnabled(false);
    }
}

