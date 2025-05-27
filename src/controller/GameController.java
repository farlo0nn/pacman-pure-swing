package controller;

import utils.*;
import utils.game.BoardSizes;
import utils.game.GameOverActions;
import utils.game.GameStatus;
import utils.game.ScoresActions;
import utils.menu.MainMenuActions;
import view.*;

import javax.swing.*;
import java.awt.*;

public class GameController {

    JFrame frame;

    public GameController(){
        this.frame = new JFrame("pacman");
        initFrame();
        showMainMenu();
        frame.pack();
        frame.setVisible(true);
    }

    private void initFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.WINDOW_HEIGHT));
    }

    private void showMainMenu() {
        setPanel(new MenuPanel(this::onMainMenuAction));
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
                frame.dispose();
                System.exit(0);
                break;
        }
    }


    public void showScores() {
        setPanel(new ScoresPanel(this::onScoresSelector));
    }
    private void onScoresSelector(ScoresActions action) {
        switch (action) {
            case TO_MENU -> {
                showMainMenu();
            }
        }
    }



    public void showBoardSizeSelector() {
        setPanel(new BoardSizeSelectorPanel(this::onBoardSizeSelected));
    }

    private void onBoardSizeSelected(BoardSizes boardSize) {
        showGame(boardSize);
    }


    public void showGame(BoardSizes boardSize) {
        setPanel(new GamePanel(boardSize, this::onGameStatus));
    }

    private void onGameStatus(GameStatus status) {
        switch (status){
            case GameStatus.PAUSE:
                // TODO
                break;
            case GameStatus.GAME_OVER:
                showGameOver();
        }
    }

    public void showGameOver() {
        setPanel(new GameOverPanel(this::onGameOverActions));
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


    private void setPanel(JPanel panel) {
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
    }
}
