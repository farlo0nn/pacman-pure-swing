package dto;

import java.util.ArrayList;

public record GameRenderData (
    char[][] map,
    ArrayList<EntityRenderData> entities,
    int score,
    int lives
){}
