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

    public void free() {
        if (mesh != null) {
            mesh.free();
        }
    }
}
