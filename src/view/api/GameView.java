package view.api;

import dto.EntityRenderData;
import dto.GameRenderData;

import java.util.ArrayList;
import java.util.function.Consumer;

public interface GameView {
    void render(GameRenderData data);
    void updateHUD();

    void setInputListener(Consumer<Integer> inputConsumer);

    void setStaticTiles(ArrayList<EntityRenderData> staticTiles);
}