package model.objects;

import lombok.Getter;
import lombok.Setter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
@Setter
public class Water extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.WATER;

    public Water() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/water.png", 10f);
    }

    public Water(GameItem gameItem) {
        super(gameItem.getMesh());
    }
}
