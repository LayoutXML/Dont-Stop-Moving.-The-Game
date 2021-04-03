package model.objects;

import lombok.Getter;
import lombok.Setter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
@Setter
public class BrickRed extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.BRICK_RED;

    private boolean dangerous = true;

    public BrickRed() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/brick_red.png", 0.2f);
    }

    public BrickRed(GameItem gameItem) {
        super(gameItem.getMesh());
    }
}
