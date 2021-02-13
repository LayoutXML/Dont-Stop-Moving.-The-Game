package model;

import engine.graphics.Mesh;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
@RequiredArgsConstructor
public class GameItem {
    private final float MOVEMENT_MULTIPLIER = 0.1f;
    private final float SCALE_MULTIPLIER = 0.01f;

    private float scale = 1;
    private final Mesh mesh;
    private final Vector3f position = new Vector3f();
    private final Vector3f rotation = new Vector3f();

    public void setPositionFromCoordinates(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotationFromCoordinates(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void moveForwardByIncrement(int increment) {
        position.y += MOVEMENT_MULTIPLIER * increment;
    }

    public void moveSidewaysByIncrement(int increment) {
        position.x += MOVEMENT_MULTIPLIER * increment;
    }

    public void moveVerticallyByIncrement(int increment) {
        position.z += MOVEMENT_MULTIPLIER * increment;
    }

    public void scaleByIncrement(int increment) {
        float scale = this.scale;
        scale += SCALE_MULTIPLIER * increment;
        if (scale <= SCALE_MULTIPLIER) {
            scale = SCALE_MULTIPLIER;
        }
        this.scale = scale;
    }

    public void free() {
        if (mesh != null) {
            mesh.free();
        }
    }
}
