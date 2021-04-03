package model.objects;

import lombok.Getter;
import lombok.Setter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
@Setter
public class Snow extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.SNOW;

    private boolean reducedFriction = true;

    public Snow() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/snow.png", 2f);
    }

    public Snow(GameItem gameItem) {
        super(gameItem.getMesh());
    }
}
