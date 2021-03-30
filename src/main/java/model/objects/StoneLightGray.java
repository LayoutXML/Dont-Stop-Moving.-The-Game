package model.objects;

import lombok.Getter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
public class StoneLightGray extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.STONE_LIGHTGRAY;

    public StoneLightGray() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/stone_lightgray.png", 0.2f);
    }
}
