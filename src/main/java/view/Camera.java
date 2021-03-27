package view;

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

    public void update(Vector3f movementDirection, Vector2f displayRotation, Level level) {
        updatePosition(movementDirection, level);
        updateRotation(displayRotation);
    }

    private void updatePosition(Vector3f movementDirection, Level level) {
        Vector3f newPosition = calculatePosition(movementDirection);

        boolean xCollision = false;
        boolean yCollision = false;
        boolean zCollision = false;
        for (GameItem gameItem : level.getGameItems()) {
            if (xCollision && yCollision && zCollision) {
                break;
            }

            if (!gameItem.isSolid()) {
                continue;
            }

            Vector2f boundsX = gameItem.getBoundsX();
            Vector2f boundsY = gameItem.getBoundsY();
            Vector2f boundsZ = gameItem.getBoundsZ();

            boolean withinOldBoundsX = position.x >= boundsX.x && position.x <= boundsX.y;
            boolean withinOldBoundsY = position.y >= boundsY.x && position.y <= boundsY.y;
            boolean withinOldBoundsZ = position.z >= boundsZ.x && position.z <= boundsZ.y;

            if (!xCollision && withinOldBoundsY && withinOldBoundsZ) {
                if (newPosition.x >= boundsX.x && newPosition.x <= boundsX.y) {
                    xCollision = true;
                }
            }
            if (!yCollision && withinOldBoundsX && withinOldBoundsZ) {
                if (newPosition.y >= boundsY.x && newPosition.y <= boundsY.y) {
                    yCollision = true;
                }
            }
            if (!zCollision && withinOldBoundsX && withinOldBoundsY) {
                if (newPosition.z >= boundsZ.x && newPosition.z <= boundsZ.y) {
                    zCollision = true;
                }
            }
        }

        if (!xCollision) {
            position.x = newPosition.x;
        }
        if (!yCollision) {
            position.y = newPosition.y;
        }
        if (!zCollision) {
            position.z = newPosition.z;
        }
    }

    private void updateRotation(Vector2f displayRotation) {
        moveRotation(MOUSE_SENSITIVITY * displayRotation.x, MOUSE_SENSITIVITY * displayRotation.y, 0);
    }

    public Vector3f calculatePosition(Vector3f movementDirection) {
        Vector3f newPosition = new Vector3f(position);
        float x = MOVEMENT_SPEED * movementDirection.x;
        float y = MOVEMENT_SPEED * movementDirection.y;
        float z = MOVEMENT_SPEED * movementDirection.z;

        if (x != 0) {
            newPosition.x += Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * x;
            newPosition.z += Math.cos(Math.toRadians(rotation.y - 90)) * x;
        }
        if (z != 0) {
            newPosition.x += Math.sin(Math.toRadians(rotation.y)) * -1.0f * z;
            newPosition.z += Math.cos(Math.toRadians(rotation.y)) * z;
        }
        newPosition.y += y;

        return newPosition;
    }

    public void moveRotation(float x, float y, float z) {
        rotation.x += x;
        rotation.y += y;
        rotation.z += z;

        if (rotation.x < -90) {
            rotation.x = - 90;
        } else if (rotation.x > 90) {
            rotation.x = 90;
        }
    }
}
