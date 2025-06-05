package dto;

import java.util.ArrayList;

public record GameRenderData (
    ArrayList<EntityRenderData> entities,
    int pelletsLeft,
    int score,
    int lives
){}
