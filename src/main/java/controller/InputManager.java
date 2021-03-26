package controller;

import view.Window;
import lombok.Getter;
import model.exceptions.InitializationException;
import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

@Getter
public class InputManager {
    private final Vector2d previousMousePosition = new Vector2d(-1, -1);
    private final Vector2d currentMousePosition = new Vector2d(0, 0);
    private final Vector2f display = new Vector2f();

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

    public void mouseInput() {
        display.set(0, 0);
        if (focused && previousMousePosition.x >= 0 && previousMousePosition.y >= 0) {
            double xDelta = currentMousePosition.x - previousMousePosition.x;
            if (xDelta != 0) {
                display.y = (float) xDelta;
            }

            double yDelta = currentMousePosition.y - previousMousePosition.y;
            if (yDelta != 0) {
                display.x = (float) yDelta;
            }
        }
        previousMousePosition.set(currentMousePosition);
    }

    public boolean isKeyPressed(Window window, int key) {
        if (window == null) {
            return false;
        }
        return GLFW_PRESS == glfwGetKey(window.getWindowId(), key);
    }
}
