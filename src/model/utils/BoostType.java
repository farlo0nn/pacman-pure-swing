package model.utils;

import java.util.Random;

public enum BoostType {
    SCORE,
    LIVES,
    SPEED;

    public static BoostType random()  {
        BoostType[] values = BoostType.values();
        return values[new Random().nextInt(values.length-1)];
    }
}
