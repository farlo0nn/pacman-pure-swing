//import controller.AppController;
import controller.AppController;
import javax.swing.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        SwingUtilities.invokeLater(AppController::new);
    }
}
