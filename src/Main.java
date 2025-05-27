import controller.GameController;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        for (String f : fonts) System.out.println(f);

        SwingUtilities.invokeLater(() -> new GameController());
    }
}
