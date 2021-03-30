package model.objects;

import lombok.Getter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
public class BrickPurple extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.BRICK_PURPLE;

    public BrickPurple() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/brick_purple.png", 0.2f);
    }
}
