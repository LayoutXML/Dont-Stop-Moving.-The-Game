package model.objects;

import lombok.Getter;
import model.ObjectType;
import model.exceptions.ResourceException;
import org.joml.Vector3f;
import view.GameItem;

@Getter
public class Lava extends GameItem {
    private float textureScale = 0.5f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.LAVA;

    private Vector3f originalPosition;
    private Vector3f currentPosition;

    private final float offset = (float) Math.random();
    private boolean movingUp = Math.random() > 0.5;

    public Lava() throws ResourceException {
        super("/cube.obj", "src/textures/blocks/lava.png", 0f);
    }

    @Override
    public void update() {
        super.update();

        if (currentPosition.y >= originalPosition.y + offset / 2) {
            movingUp = false;
        } else if (currentPosition.y <= originalPosition.y - offset / 2) {
            movingUp = true;
        }

        if (movingUp) {
            currentPosition.y += 0.01f;
        } else {
            currentPosition.y -= 0.01f;
        }
    }

    @Override
    public void setPosition(Vector3f position) {
        this.originalPosition = new Vector3f(position);
        this.currentPosition = position;
    }

    @Override
    public Vector3f getPosition() {
        return this.currentPosition;
    }
}
