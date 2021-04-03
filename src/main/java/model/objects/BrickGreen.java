package model.objects;

import lombok.Getter;
import lombok.Setter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
@Setter
public class BrickGreen extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.BRICK_GREEN;

    public BrickGreen() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/brick_green.png", 0.2f);
    }

    public BrickGreen(GameItem gameItem) {
        super(gameItem.getMesh());
    }
}
