package view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joml.Vector2f;
import org.joml.Vector3f;

@Getter
@NoArgsConstructor
public class Camera {
    public static final float MOUSE_SENSITIVITY = 0.3f;

    private float MOVEMENT_SPEED = 0.1f;

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

    public void update(Vector3f movementDirection, Vector2f displayRotation) {
        // TODO: handle collision
        movePosition(MOVEMENT_SPEED * movementDirection.x, MOVEMENT_SPEED * movementDirection.y, MOVEMENT_SPEED * movementDirection.z);
        moveRotation(MOUSE_SENSITIVITY * displayRotation.x, MOUSE_SENSITIVITY * displayRotation.y, 0);
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
