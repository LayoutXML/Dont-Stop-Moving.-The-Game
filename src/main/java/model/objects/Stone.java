package model.objects;

import lombok.Getter;
import model.ObjectType;
import model.exceptions.ResourceException;
import view.GameItem;

@Getter
public class Stone extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.STONE;

    public Stone() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/stone.png", 0.2f);
    }
}