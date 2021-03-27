package controller;

import org.joml.Vector3f;
import view.Window;
import lombok.Getter;
import model.exceptions.InitializationException;
import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

@Getter
public class InputManager {
    public static final boolean FLYING_ALLOWED = false;

    private static final int KEY_FORWARD = GLFW_KEY_W;
    private static final int KEY_BACKWARD = GLFW_KEY_S;
    private static final int KEY_LEFT = GLFW_KEY_A;
    private static final int KEY_RIGHT = GLFW_KEY_D;
    private static final int KEY_UP = GLFW_KEY_Q;
    private static final int KEY_DOWN = GLFW_KEY_Z;

    private final Vector2d currentMousePosition = new Vector2d(0, 0);

    private boolean focused;
    private boolean leftMouseButtonPressed;
    private boolean rightMouseButtonPressed;

    public void initialize(Window window) throws InitializationException {
        if (window == null) {
            throw new InitializationException("IE7");
        }

        glfwSetCursorEnterCallback(window.getWindowId(), (windowId, entered) -> focused = entered);
        glfwSetCursorPosCallback(window.getWindowId(), ((windowId, x, y) -> currentMousePosition.set(x, y)));
        glfwSetMouseButtonCallback(window.getWindowId(), ((windowId, button, action, mods) -> {
            leftMouseButtonPressed = false;
            rightMouseButtonPressed = false;
            if (GLFW_PRESS == action) {
                if (GLFW_MOUSE_BUTTON_1 == button) {
                    leftMouseButtonPressed = true;
                } else if (GLFW_MOUSE_BUTTON_2 == button) {
                    rightMouseButtonPressed = true;
                }
            }
        }));
    }

    public void updateDisplayRotation(Window window, Vector2f displayRotation) {
        if (!focused) {
            return; // TODO: improve focus detection
        }

        displayRotation.set(0, 0);

        displayRotation.x = (float) currentMousePosition.y - window.getHeight() / 2; // TODO: invert x axis
        displayRotation.y = (float) currentMousePosition.x - window.getWidth() / 2;

        glfwSetCursorPos(window.getWindowId(), (double) window.getWidth() / 2, (double) window.getHeight() / 2);
    }

    public void updateMovementDirection(Window window, Vector3f movementDirection) {
        movementDirection.set(0, 0, 0);

        if (isKeyPressed(window, KEY_FORWARD)) {
            movementDirection.z = -1;
        } else if (isKeyPressed(window, KEY_BACKWARD)) {
            movementDirection.z = 1;
        }

        if (isKeyPressed(window, KEY_LEFT)) {
            movementDirection.x = -1;
        } else if (isKeyPressed(window, KEY_RIGHT)) {
            movementDirection.x = 1;
        }

        if (FLYING_ALLOWED) {
            if (isKeyPressed(window, KEY_UP)) {
                movementDirection.y = 1;
            } else if (isKeyPressed(window, KEY_DOWN)) {
                movementDirection.y = -1;
            }
        } else {
            movementDirection.y = -1;
        }
    }

    public boolean isKeyPressed(Window window, int key) {
        if (window == null) {
            return false;
        }
        return GLFW_PRESS == glfwGetKey(window.getWindowId(), key);
    }
}
