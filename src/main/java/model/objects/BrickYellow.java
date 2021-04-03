package model.objects;

import lombok.Getter;
import lombok.Setter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
@Setter
public class BrickYellow extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.BRICK_YELLOW;

    public BrickYellow() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/brick_yellow.png", 0.2f);
    }

    public BrickYellow(GameItem gameItem) {
        super(gameItem.getMesh());
    }
}
