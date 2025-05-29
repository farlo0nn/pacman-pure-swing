package dto;

import utils.EntityType;
import utils.MovementDirection;

public record EntityRenderData(
    EntityType type,
    int x,
    int y,
    MovementDirection direction,
    int frame
){}