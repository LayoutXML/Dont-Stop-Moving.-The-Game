package model.objects;

import lombok.Getter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
public class Ice extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.ICE;

    public Ice() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/ice.png", 10f);
    }
}
