package model.objects;

import lombok.Getter;
import lombok.Setter;
import model.ObjectType;
import model.exceptions.ResourceException;
import org.joml.Vector3f;
import view.GameItem;

@Getter
@Setter
public class BrickYellow extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.BRICK_YELLOW;

    private boolean win = true;

    private Vector3f rotation = new Vector3f();

    public BrickYellow() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/brick_yellow.png", 0.2f);
    }

    public BrickYellow(GameItem gameItem) {
        super(gameItem.getMesh());
    }

    // Update method design pattern
    @Override
    public void update() {
        super.update();

        rotation.x++;
        rotation.y++;
        rotation.z++;

        if (rotation.x >= 360) {
            rotation.x = 0;
        }
        if (rotation.y >= 360) {
            rotation.y = 0;
        }
        if (rotation.z >= 360) {
            rotation.z = 0;
        }
    }
}
