//import controller.AppController;
import controller.AppController;
import controller.utils.BoardLoader;
import model.entities.Tile;
import model.utils.GhostPathBuilder;

import javax.swing.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(AppController::new);
    }
}
