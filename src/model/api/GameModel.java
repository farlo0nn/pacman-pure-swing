package model.api;

import dto.EntityRenderData;
import dto.GameExitData;
import dto.GameRenderData;
import utils.EntityType;
import utils.MovementDirection;

import java.util.ArrayList;
import java.util.function.Consumer;

public interface GameModel {

    void setSpeedBoosted(boolean b);

    void updatePacman(MovementDirection pacmanRequestedDirection);
    void updateGhosts();
    GameRenderData update();

    void spawnBoost();
    void setBoostsListener(Consumer<EntityType> boostsListener);

    GameExitData getGameInfo();
    ArrayList<EntityRenderData> getStaticDTO();
}
