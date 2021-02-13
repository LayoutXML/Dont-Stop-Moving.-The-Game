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
    private final Mesh mesh;
    private final Vector3f position = new Vector3f();
    private final Vector3f rotation = new Vector3f();
    private float scale = 1;
    private final float movementMultiplier = 0.1f;
    private final float scaleMultiplier = 0.01f;

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
        position.y += movementMultiplier * increment;
    }

    public void moveSidewaysByIncrement(int increment) {
        position.x += movementMultiplier * increment;
    }

    public void moveVerticallyByIncrement(int increment) {
        position.z += movementMultiplier * increment;
    }

    public void scaleByIncrement(int increment) {
        float scale = this.scale;
        scale += scaleMultiplier * increment;
        if (scale <= scaleMultiplier) {
            scale = scaleMultiplier;
        }
        this.scale = scale;
    }

    public void free() {
        if (mesh != null) {
            mesh.free();
        }
    }
}
