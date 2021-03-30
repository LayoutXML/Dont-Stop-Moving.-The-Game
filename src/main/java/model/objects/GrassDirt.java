package model.objects;

import lombok.Getter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
public class GrassDirt extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.GRASS_DIRT;

    public GrassDirt() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/grass_dirt.png", 0.1f);
    }
}
