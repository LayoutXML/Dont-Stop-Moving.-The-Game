package view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.ObjectType;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Camera {
    public static final float MOUSE_SENSITIVITY = 0.3f;
    public static final float COLLISION_NEAR = 0.1f;
    public static final float PLAYER_HEIGHT_UNDER = 1.5f;
    public static final float PLAYER_HEIGHT_ABOVE = 0.1f;
    public static final List<ObjectType> REDUCED_FRICTION_OBJECTS = Arrays.asList(ObjectType.ICE, ObjectType.SNOW, ObjectType.GRASS_SNOW);

    private float MOVEMENT_SPEED = 0.1f;

    private final Vector3f position = new Vector3f();
    private final Vector3f rotation = new Vector3f();

    private static final int JUMP_PROGRESS_MAX = 25;
    private int jumpProgress = 0;

    private static final int FRICTION_PROGRESS_MAX_REGULAR = 7;
    private static final int FRICTION_PROGRESS_MAX_REDUCED = 150;
    private int frictionProgressX = 0;
    private int frictionProgressZ = 0;
    private boolean frictionReduced = false;

    private boolean jumping = false;
    private boolean onGround = false;

    public void setPosition(Vector3f position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
    }

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

    public Vector3f update(Vector3f movementDirection, Vector2f displayRotation, boolean jump, Level level) {
        updateJump(jump);
        updateRotation(displayRotation);
        updateFriction(movementDirection);
        return updatePosition(movementDirection, level);
    }

    private void updateJump(boolean jump) {
        if (jumpProgress >= JUMP_PROGRESS_MAX) {
            jumpProgress = 0;
        }
        if (jumpProgress == 0) {
            jumping = onGround && jump;
        }
        if (jumping) {
            jumpProgress++;
        }
    }

    private void updateRotation(Vector2f displayRotation) {
        moveRotation(MOUSE_SENSITIVITY * displayRotation.x, MOUSE_SENSITIVITY * displayRotation.y, 0);
    }

    private void updateFriction(Vector3f movementDirection) {
        float maxFrictionProgressX = getMaxFrictionProgress(frictionProgressX);
        if (maxFrictionProgressX > 0) {
            if (frictionProgressX >= maxFrictionProgressX) {
                frictionProgressX = 0;
            }
        } else {
            if (frictionProgressX <= maxFrictionProgressX) {
                frictionProgressX = 0;
            }
        }

        float maxFrictionProgressZ = getMaxFrictionProgress(frictionProgressZ);
        if (maxFrictionProgressZ > 0) {
            if (frictionProgressZ >= maxFrictionProgressZ) {
                frictionProgressZ = 0;
            }
        } else {
            if (frictionProgressZ <= maxFrictionProgressZ) {
                frictionProgressZ = 0;
            }
        }

        if (movementDirection.x != 0) {
            frictionProgressX = (int) movementDirection.x;
        } else if (frictionProgressX > 0) {
            frictionProgressX++;
        } else if (frictionProgressX < 0) {
            frictionProgressX--;
        }

        if (movementDirection.z != 0) {
            frictionProgressZ = (int) movementDirection.z;
        } else if (frictionProgressZ > 0) {
            frictionProgressZ++;
        } else if (frictionProgressZ < 0) {
            frictionProgressZ--;
        }
    }

    private Vector3f updatePosition(Vector3f movementDirection, Level level) {
        Vector3f previousPosition = new Vector3f(position);
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

            boolean withinOldBoundsX = position.x + COLLISION_NEAR >= boundsX.x && position.x - COLLISION_NEAR <= boundsX.y;
            boolean withinOldBoundsZ = position.z + COLLISION_NEAR >= boundsZ.x && position.z - COLLISION_NEAR <= boundsZ.y;

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

                    if (movementDirection.y < 0) {
                        onGround = true;
                        frictionReduced = REDUCED_FRICTION_OBJECTS.contains(gameItem.getObjectType());
                    }
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
            onGround = false;
            frictionReduced = false;
        }

        return new Vector3f(position.x - previousPosition.x, position.y - previousPosition.y, position.z - previousPosition.z);
    }

    public Vector3f calculatePosition(Vector3f movementDirection) {
        Vector3f newPosition = new Vector3f(position);

        float maxFrictionProgressX = getMaxFrictionProgress(frictionProgressX);
        float frictionDifferenceX = maxFrictionProgressX - frictionProgressX;
        float x = MOVEMENT_SPEED * movementDirection.x;
        if (frictionDifferenceX != 0 && frictionProgressX != 0) {
            x += (maxFrictionProgressX > 0 ? 1 : -1) * (frictionDifferenceX / maxFrictionProgressX) / 100;
        }

        float maxFrictionProgressZ = getMaxFrictionProgress(frictionProgressZ);
        float frictionDifferenceZ = maxFrictionProgressZ - frictionProgressZ;
        float z = MOVEMENT_SPEED * movementDirection.z;
        if (frictionDifferenceZ != 0 && frictionProgressZ != 0) {
            z += (maxFrictionProgressZ > 0 ? 1 : -1) * (frictionDifferenceZ / maxFrictionProgressZ) / 100;
        }

        float y = 0;
        float jumpDifference = JUMP_PROGRESS_MAX - jumpProgress;
        if (jumpProgress == 0) {
            y = MOVEMENT_SPEED * movementDirection.y;
        } else if (jumpDifference != 0) {
            y = jumpDifference / (10 * JUMP_PROGRESS_MAX);
        }

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

    private float getMaxFrictionProgress(int currentProgress) {
        if (frictionReduced) {
            if (currentProgress >= 0) {
                return FRICTION_PROGRESS_MAX_REDUCED;
            }
            return -FRICTION_PROGRESS_MAX_REDUCED;
        }

        if (currentProgress >= 0) {
            return FRICTION_PROGRESS_MAX_REGULAR;
        }
        return -FRICTION_PROGRESS_MAX_REGULAR;
    }
}
