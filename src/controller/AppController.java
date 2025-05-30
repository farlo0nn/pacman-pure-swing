package controller;

import dto.GameExitData;
import model.GameLogic;
import model.api.GameModel;

import view.*;
import view.UIManager;

import utils.io.FileManager;
import utils.menu.MainMenuActions;

import utils.game.BoardSize;
import utils.game.GameOverActions;
import utils.game.GameStatus;
import utils.game.ScoresActions;

import java.util.ArrayList;
import java.util.Objects;

public class AppController {

    private final UIManager uiManager;
    private final FileManager fileManager;

    public AppController() {
        this.uiManager = new UIManager();
        this.fileManager = new FileManager();
        showMainMenu();
    }

    private void showMainMenu() {
        uiManager.setPanel(new MenuPanel(this::onMainMenuAction));
    }

    private void onMainMenuAction(MainMenuActions action) {
        switch (action) {
            case MainMenuActions.START:
                showBoardSizeSelector();
                break;
            case MainMenuActions.SCORES:
                showScores();
                break;
            case MainMenuActions.EXIT:
                System.exit(0);
                break;
        }
    }


    public void showScores() {
        ArrayList<String> scores = fileManager.loadScores();
        uiManager.setPanel(new ScoresPanel(scores, this::onScoresSelector));
    }
    private void onScoresSelector(ScoresActions action) {
        switch (action) {
            case TO_MENU -> {
                showMainMenu();
            }
        }
    }

    public void showBoardSizeSelector() {
        uiManager.setPanel(new BoardSizeSelectorPanel(this::onBoardSizeSelected));
    }

    private void onBoardSizeSelected(BoardSize boardSize) {
        showGame(boardSize);
    }

    public void showGame(BoardSize boardSize) {
        GameModel model = new GameLogic(boardSize);
        GameContainer view = new GameContainer(boardSize);
        uiManager.setPanel(view);
        GameController controller = new GameController(view, model, this::onGameStatus);
    }

    private void onGameStatus(GameExitData data) {
        System.out.println("STOPPED");
        if (Objects.requireNonNull(data.status()) == GameStatus.OVER) {

            String username = uiManager.showDialogue();
            fileManager.saveScores(username, data.size(), data.score());
            showGameOver();
        }
    }

    public void showGameOver() {
        uiManager.setPanel(new GameOverPanel(this::onGameOverActions));
    }

    private void onGameOverActions(GameOverActions action){
        switch (action) {
            case GameOverActions.RESTART:
                showBoardSizeSelector();
                break;
            case GameOverActions.TO_MENU:
                showMainMenu();
        }
    }
}
