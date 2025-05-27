package utils.game;

import java.awt.*;

public class UIUtils {
    public static boolean collides(Rectangle a, Rectangle b) {
        return (
                ((a.x < b.x + b.width) &&
                (a.x + a.width > b.x)) &&
                ((a.y < b.y + b.height) &&
                (a.y + a.height > b.y))
        );
    }

}
