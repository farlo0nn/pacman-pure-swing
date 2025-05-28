package dto;

import utils.EntityType;

public record EntityRenderData(
    EntityType type,
    int x,
    int y,
    int frame
){}