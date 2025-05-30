package dto;

import utils.game.BoardSize;
import utils.game.GameStatus;

public record GameExitData(
    GameStatus status,
    int score,
    BoardSize size
) {}
