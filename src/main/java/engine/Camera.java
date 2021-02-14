package engine;

import lombok.Getter;
import org.joml.Vector3f;

@Getter
public class Camera {
    private final float MOVEMENT_MULTIPLIER = 0.1f;
    private final float SCALE_MULTIPLIER = 0.01f;

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

    public void moveForwardByIncrement(int increment) {
        position.y += MOVEMENT_MULTIPLIER * increment;
    }

    public void moveSidewaysByIncrement(int increment) {
        position.x += MOVEMENT_MULTIPLIER * increment;
    }

    public void moveVerticallyByIncrement(int increment) {
        position.z += MOVEMENT_MULTIPLIER * increment;
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
