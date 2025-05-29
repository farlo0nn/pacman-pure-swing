package controller;

import dto.GameRenderData;

import model.GameModel;
import utils.MovementDirection;


import view.GameContainer;
import view.utils.UIConstants;

import controller.utils.BoardLoader;
import controller.utils.BoardSizes;
import controller.utils.timing.GameTimer;

import utils.game.GameStatus;


import java.util.function.Consumer;

public class GameController {
    private final GameContainer gameContainer;
    private final GameModel gameModel;
    private int tileSize;
    private char[][] map;
    private final Consumer<GameStatus> statusConsumer;
    private MovementDirection pacmanRequestedDirection;
    private final GameTimer gameTimer;
    private final Thread gameThread;

    public GameController(BoardSizes boardSize, Consumer<GameStatus> statusConsumer) {
        initSize(boardSize);

        this.gameModel = new GameModel(map, tileSize);
        this.gameContainer = new GameContainer(tileSize, this::onDirectionInput);
        this.statusConsumer = statusConsumer;

        gameTimer = new GameTimer(6);
        gameTimer.addCallback(this::updateGame);

        gameThread = new Thread(gameTimer);

        gameThread.start();
    }

    private void onDirectionInput(MovementDirection direction) {
        pacmanRequestedDirection = direction;
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

    public void updateGame() {

        GameRenderData dto = gameModel.update(pacmanRequestedDirection);
        if (dto == null) {
            stopGame();
            return;
        }
        assert dto != null;
        pacmanRequestedDirection = null;
        gameContainer.update(dto);


    }

    public void stopGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (gameThread != null && gameThread.isAlive() && Thread.currentThread() != gameThread) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                // TODO - add logging logic
                e.printStackTrace();
            }
        }
        this.statusConsumer.accept(GameStatus.OVER);
    }

    public GameContainer getGameContainer() {
        return this.gameContainer;
    }
}
