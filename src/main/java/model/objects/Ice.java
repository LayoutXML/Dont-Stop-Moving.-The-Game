package model.objects;

import lombok.Getter;
import lombok.Setter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
@Setter
public class Ice extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.ICE;

    private boolean reducedFriction = true;

    public Ice() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/ice.png", 10f);
    }
}
