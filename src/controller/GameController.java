package controller;

import dto.GameRenderData;

import model.GameModel;
import utils.EntityType;
import utils.MovementDirection;


import view.GameContainer;
import view.utils.UIConstants;

import controller.utils.BoardLoader;
import controller.utils.BoardSizes;
import controller.utils.timing.GameTimer;

import utils.game.GameStatus;


import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class GameController {
    private final GameContainer gameContainer;
    private final GameModel gameModel;
    private int tileSize;
    private char[][] map;
    private final Consumer<GameStatus> statusConsumer;
    private MovementDirection pacmanRequestedDirection;
    private final ArrayList<GameTimer> timers;
    Thread boostThread;

    public GameController(BoardSizes boardSize, Consumer<GameStatus> statusConsumer) {
        initSize(boardSize);

        this.gameModel = new GameModel(map, tileSize, this::onBoostSpawn);
        this.gameContainer = new GameContainer(tileSize, this::onInput);
        this.statusConsumer = statusConsumer;
        this.timers = new ArrayList<>();

        timers.add(new GameTimer(50, this::updateGame));
        timers.add(new GameTimer(200, this::updateGhosts));
        timers.add(new GameTimer(75, this::updatePacman));
        timers.add(new GameTimer(1000, this::updateHud));
        timers.add(new GameTimer(5000, this::spawnBoost));

        for (GameTimer timer : timers) {
            timer.start();
        }
    }

    private void onInput(Integer keyCode) {
        switch (keyCode) {
            case 27 -> stopGame();
            case 37 -> pacmanRequestedDirection = MovementDirection.LEFT;
            case 38 -> pacmanRequestedDirection = MovementDirection.UP;
            case 39 -> pacmanRequestedDirection = MovementDirection.RIGHT;
            case 40 -> pacmanRequestedDirection = MovementDirection.DOWN;
        }
    }

    private void onBoostSpawn(EntityType boostType) {
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

    private void initSize(BoardSizes boardSize) {
        switch (boardSize) {
            case SMALL -> {
                tileSize = UIConstants.WINDOW_WIDTH/28;
                map = BoardLoader.loadBoard("/board/boards/small.csv");
                break;
            }
            case MEDIUM -> {
                tileSize = UIConstants.WINDOW_WIDTH/42;
                map = BoardLoader.loadBoard("/board/boards/medium.csv");
                break;
            }
            case LARGE -> {
                tileSize = UIConstants.WINDOW_WIDTH/56;
                map = BoardLoader.loadBoard("/board/boards/large.csv");
                break;
            }
        }
    }

    private void updateGame() {
        synchronized (gameModel) {
            GameRenderData dto = gameModel.update();
            if (dto == null) {
                stopGame();
                return;
            }
            synchronized (gameContainer) {
                SwingUtilities.invokeLater(() -> {gameContainer.update(dto);});
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
        synchronized (gameContainer) {
            SwingUtilities.invokeLater(gameContainer::updateHud);
        }
    }


    public void stopGame() {

        for (GameTimer timer : timers) {
            if (timer != null) {
                timer.stop();
            }
        }

        this.statusConsumer.accept(GameStatus.OVER);
    }

    public GameContainer getGameContainer() {
        return this.gameContainer;
    }
}
