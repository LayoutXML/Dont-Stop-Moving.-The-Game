package engine.impl;

import engine.GameLogic;
import engine.graphics.Mesh;
import engine.RenderEngine;
import engine.Window;
import model.GameItem;
import model.exceptions.InitializationException;

import static org.lwjgl.glfw.GLFW.*;

public class GameLogicImpl implements GameLogic {

    private final float[] positions = new float[]{
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f
    };
    private final float[] colors = new float[]{
            0.5f, 0.0f, 0.0f,
            0.0f, 0.5f, 0.0f,
            0.0f, 0.0f, 0.5f,
            0.0f, 0.5f, 0.5f
    };
    private final int[] indexes = new int[]{0, 1, 3, 3, 1, 2};

    private int forwardDirection = 0;
    private int sidewaysDirection = 0;
    private int verticalDirection = 0;
    private int scale = 0;
    private final RenderEngine renderEngine = new RenderEngine();
    private GameItem[] gameItems;

    @Override
    public void initialize(Window window) throws InitializationException {
        renderEngine.initialize(window);
        Mesh mesh = new Mesh(positions, colors, indexes);
        GameItem gameItem = new GameItem(mesh);
        gameItem.setPositionFromCoordinates(0, 0, -2);
        gameItems = new GameItem[]{gameItem};
    }

    @Override
    public void input(Window window) {
        if (window == null) {
            return;
        }

        forwardDirection = 0;
        if (window.isKeyPressed(GLFW_KEY_W)) {
            forwardDirection = 1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            forwardDirection = -1;
        }

        sidewaysDirection = 0;
        if (window.isKeyPressed(GLFW_KEY_A)) {
            sidewaysDirection = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            sidewaysDirection = 1;
        }

        verticalDirection = 0;
        if (window.isKeyPressed(GLFW_KEY_Q)) {
            verticalDirection = 1;
        } else if (window.isKeyPressed(GLFW_KEY_Z)) {
            verticalDirection = -1;
        }

        scale = 0;
        if (window.isKeyPressed(GLFW_KEY_PERIOD)) {
            verticalDirection = 1;
        } else if (window.isKeyPressed(GLFW_KEY_COMMA)) {
            verticalDirection = -1;
        }
    }

    @Override
    public void update(float updateInterval) {
        for (GameItem gameItem : gameItems) {
            gameItem.moveForwardByIncrement(forwardDirection);
            gameItem.moveSidewaysByIncrement(sidewaysDirection);
            gameItem.moveVerticallyByIncrement(verticalDirection);
            gameItem.scaleByIncrement(scale);

            float rotation = gameItem.getRotation().z + 1.5f;
            if (rotation > 360) {
                rotation = 0;
            }
            gameItem.setRotationFromCoordinates(0, 0, rotation);
        }
    }

    @Override
    public void render(Window window) {
        renderEngine.render(window, gameItems);
    }

    @Override
    public void free() {
        renderEngine.free();
        for (GameItem gameItem : gameItems) {
            gameItem.free();
        }
    }
}
