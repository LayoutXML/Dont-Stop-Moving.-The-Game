package model.objects;

import lombok.Getter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
public class GrassSnow extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.GRASS_SNOW;

    private boolean reducedFriction = true;

    public GrassSnow() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/grass_snow.png", 2f);
    }
}
