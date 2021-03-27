package view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joml.Vector2f;
import org.joml.Vector3f;

@Getter
@NoArgsConstructor
public class Camera {
    public static final float MOUSE_SENSITIVITY = 0.3f;
    public static final float COLLISION_NEAR = 0.1f;
    public static final float PLAYER_HEIGHT_UNDER = 1.5f;
    public static final float PLAYER_HEIGHT_ABOVE = 0.1f;

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
        boolean zCollision = false;
        boolean yCollision = false;
        for (GameItem gameItem : level.getGameItems()) {
            if (xCollision && zCollision && yCollision) {
                break;
            }

            if (!gameItem.isSolid()) {
                continue;
            }

            Vector2f boundsX = gameItem.getBoundsX();
            Vector2f boundsZ = gameItem.getBoundsZ();
            Vector2f boundsY = gameItem.getBoundsY();

            boolean withinOldBoundsX = position.x >= boundsX.x && position.x <= boundsX.y;
            boolean withinOldBoundsZ = position.z >= boundsZ.x && position.z <= boundsZ.y;

            float playerTopPoint = position.y + PLAYER_HEIGHT_ABOVE + COLLISION_NEAR;
            float playerBottomPoint = position.y - PLAYER_HEIGHT_UNDER - COLLISION_NEAR;
            boolean withinOldBoundsY = (playerTopPoint >= boundsY.x && playerTopPoint <= boundsY.y) || (playerBottomPoint <= boundsY.y && playerBottomPoint >= boundsY.x) || (playerBottomPoint <= boundsY.x && playerTopPoint >= boundsY.y);

            if (!xCollision && withinOldBoundsY && withinOldBoundsZ) {
                if (newPosition.x + COLLISION_NEAR >= boundsX.x && newPosition.x - COLLISION_NEAR <= boundsX.y) {
                    xCollision = true;
                }
            }
            if (!zCollision && withinOldBoundsX && withinOldBoundsY) {
                if (newPosition.z + COLLISION_NEAR >= boundsZ.x && newPosition.z - COLLISION_NEAR <= boundsZ.y) {
                    zCollision = true;
                }
            }
            if (!yCollision && withinOldBoundsX && withinOldBoundsZ) {
                if (newPosition.y + PLAYER_HEIGHT_ABOVE + COLLISION_NEAR >= boundsY.x && newPosition.y - PLAYER_HEIGHT_UNDER - COLLISION_NEAR <= boundsY.y) {
                    yCollision = true;
                }
            }
        }

        if (!xCollision) {
            position.x = newPosition.x;
        }
        if (!zCollision) {
            position.z = newPosition.z;
        }
        if (!yCollision) {
            position.y = newPosition.y;
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
            rotation.x = -90;
        } else if (rotation.x > 90) {
            rotation.x = 90;
        }
    }
}
