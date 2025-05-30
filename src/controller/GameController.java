package controller;

import controller.interfaces.GameModelListener;
import controller.interfaces.GameViewListener;
import dto.GameExitData;
import dto.GameRenderData;

import model.api.GameModel;
import utils.EntityType;
import utils.MovementDirection;

import view.api.GameView;

import controller.utils.timing.GameTimer;

import utils.game.GameStatus;


import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class GameController implements GameViewListener, GameModelListener {
    private final GameView gameView;
    private final GameModel gameModel;
    private int tileSize;
    private char[][] map;
    private final Consumer<GameExitData> statusConsumer;
    private MovementDirection pacmanRequestedDirection;
    private final ArrayList<GameTimer> timers;
    Thread boostThread;

    public GameController(GameView gameView, GameModel gameModel, Consumer<GameExitData> statusConsumer) {
        this.gameView = gameView;
        this.gameModel = gameModel;
        this.statusConsumer = statusConsumer;
        this.timers = new ArrayList<>();

        this.gameModel.setBoostsListener(this::onBoostSpawned);
        this.gameView.setInputListener(this::onInput);

        timers.add(new GameTimer(50, this::updateGame));
        timers.add(new GameTimer(200, this::updateGhosts));
        timers.add(new GameTimer(75, this::updatePacman));
        timers.add(new GameTimer(1000, this::updateHud));
        timers.add(new GameTimer(5000, this::spawnBoost));

        for (GameTimer timer : timers) {
            timer.start();
        }
    }

    @Override
    public void onInput(Integer keyCode) {
        switch (keyCode) {
            case 27 -> stopGame();
            case 37 -> pacmanRequestedDirection = MovementDirection.LEFT;
            case 38 -> pacmanRequestedDirection = MovementDirection.UP;
            case 39 -> pacmanRequestedDirection = MovementDirection.RIGHT;
            case 40 -> pacmanRequestedDirection = MovementDirection.DOWN;
        }
    }

    @Override
    public void onBoostSpawned(EntityType boostType) {
        if (Objects.requireNonNull(boostType) == EntityType.SPEED_BOOST) {
            boostThread = new Thread(() -> {
                synchronized (gameModel) {
                    gameModel.setSpeedBoosted(true);
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                synchronized (gameModel) {
                    gameModel.setSpeedBoosted(false);
                }
            });
            boostThread.start();
        }
    }

    private void updateGame() {
        synchronized (gameModel) {
            GameRenderData dto = gameModel.update();
            if (dto == null) {
                stopGame();
                return;
            }
            synchronized (gameView) {
                SwingUtilities.invokeLater(() -> {
                    gameView.render(dto);});
            }
        }

    }

    private void updatePacman() {
        synchronized (gameModel) {
            this.gameModel.updatePacman(pacmanRequestedDirection);
            pacmanRequestedDirection = null;
        }
    }

    private void spawnBoost() {
        synchronized (gameModel) {
            gameModel.spawnBoost();
        }
    }

    private void updateGhosts() {
        synchronized (gameModel) {
            gameModel.updateGhosts();
        }
    }

    private void updateHud() {
        synchronized (gameView) {
            SwingUtilities.invokeLater(gameView::updateHUD);
        }
    }


    public void stopGame() {

        for (GameTimer timer : timers) {
            if (timer != null) {
                timer.stop();
            }
        }

        GameExitData exitDTO = gameModel.getGameInfo();
        this.statusConsumer.accept(exitDTO);
    }
}
