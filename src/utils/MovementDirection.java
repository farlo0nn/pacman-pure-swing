package utils;

import java.util.Arrays;
import java.util.Random;

public enum MovementDirection {
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP(0, -1),
    DOWN(0, 1),
    NONE(0, 0);

    public final int xDir;
    public final int yDir;

    MovementDirection(int xDir, int yDir) {
        this.xDir = xDir;
        this.yDir = yDir;
    }

    public int[] asArray() {
        return new int[]{xDir, yDir};
    }

    public static MovementDirection random()  {
        MovementDirection[] values = MovementDirection.values();
        return values[new Random().nextInt(values.length-1)];
    }

    public static MovementDirection[] valuesWithoutLast() {
        return Arrays.copyOf(values(), values().length - 1);
    }

    public static MovementDirection opposite(MovementDirection direction) {
        switch (direction) {
            case LEFT -> {
                return RIGHT;
            }
            case RIGHT -> {
                return LEFT;
            }
            case UP -> {
                return DOWN;
            }
            case DOWN -> {
                return UP;
            }
            default -> {
                return NONE;
            }
        }
    }
}
