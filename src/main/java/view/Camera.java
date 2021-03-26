package view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joml.Vector3f;

@Getter
@NoArgsConstructor
public class Camera {

    private final Vector3f position = new Vector3f();
    private final Vector3f rotation = new Vector3f();

    public void setPositionFromCoordinates(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setRotationFromCoordinates(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void movePosition(float x, float y, float z) {
        if (x != 0) {
            position.x += Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            position.z += Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }
        if (z != 0) {
            position.x += Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            position.z += Math.cos(Math.toRadians(rotation.y)) * z;
        }
        position.y += y;
    }

    public void moveRotation(float x, float y, float z) {
        rotation.x += x;
        rotation.y += y;
        rotation.z += z;
    }
}
