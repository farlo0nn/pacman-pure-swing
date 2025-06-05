package utils;

public enum EntityType {
    EMPTY,
    WALL,
    PELLET,
    POWER_PELLET,
    PACMAN,
    RED_GHOST,
    YELLOW_GHOST,
    BLUE_GHOST,
    PINK_GHOST,
    PORTAL,
    SCORE_BOOST,
    LIVES_BOOST,
    SPEED_BOOST;

    public static boolean isGhost(EntityType type) {
        if (
                type == RED_GHOST ||
                type == YELLOW_GHOST ||
                type == BLUE_GHOST ||
                type == PINK_GHOST
        ) return true;
        return false;
    }
}
