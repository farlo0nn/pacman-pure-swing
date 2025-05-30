package view.api;

import dto.GameRenderData;

import java.util.function.Consumer;

public interface GameView {
    void render(GameRenderData data);
    void updateHUD();

    void setInputListener(Consumer<Integer> inputConsumer);
}