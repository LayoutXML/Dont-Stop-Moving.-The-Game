package engine.impl;

import engine.*;
import engine.graphics.Mesh;
import model.GameItem;
import model.Texture;
import model.exceptions.InitializationException;
import model.exceptions.ResourceException;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class GameLogicImpl implements GameLogic {

    private final float MOUSE_SENSITIVITY = 0.1f;
    private final float CAMERA_POSITION_STEP = 0.1f;

    private final RenderEngine renderEngine = new RenderEngine();
    private final Camera camera = new Camera();
    private final Vector3f cameraMovement = new Vector3f();

    private GameItem[] gameItems;

    @Override
    public void initialize(Window window) throws InitializationException, ResourceException {
        if (window == null) {
            throw new InitializationException("IE7");
        }

        renderEngine.initialize(window);

        Texture texture = new Texture("src/textures/grassblock.png");
        Mesh mesh = OBJLoader.loadMesh("/cube.obj");
        mesh.setTexture(texture);

        GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.5f);
        gameItem.setPositionFromCoordinates(0, 0, -4);
        GameItem gameItem1 = new GameItem(mesh);
        gameItem1.setScale(0.5f);
        gameItem1.setPositionFromCoordinates(1, 1, -4);
        GameItem gameItem2 = new GameItem(mesh);
        gameItem2.setScale(0.5f);
        gameItem2.setPositionFromCoordinates(0, 0, -5);

        gameItems = new GameItem[]{gameItem, gameItem1, gameItem2};
    }

    @Override
    public void input(InputManager inputManager, Window window) {
        if (inputManager == null || window == null) {
            return;
        }

        cameraMovement.set(0, 0, 0);

        if (inputManager.isKeyPressed(window, GLFW_KEY_W)) {
            cameraMovement.z = -1;
        } else if (inputManager.isKeyPressed(window, GLFW_KEY_S)) {
            cameraMovement.z = 1;
        }

        if (inputManager.isKeyPressed(window, GLFW_KEY_A)) {
            cameraMovement.x = -1;
        } else if (inputManager.isKeyPressed(window, GLFW_KEY_D)) {
            cameraMovement.x = 1;
        }

        if (inputManager.isKeyPressed(window, GLFW_KEY_Q)) {
            cameraMovement.y = 1;
        } else if (inputManager.isKeyPressed(window, GLFW_KEY_Z)) {
            cameraMovement.y = -1;
        }
    }

    @Override
    public void update(InputManager inputManager, float updateInterval) {
        camera.movePosition(CAMERA_POSITION_STEP * cameraMovement.x, CAMERA_POSITION_STEP * cameraMovement.y, CAMERA_POSITION_STEP * cameraMovement.z);
        if (inputManager.isRightMouseButtonPressed()) {
            Vector2f rotation = inputManager.getDisplay();
            camera.moveRotation(MOUSE_SENSITIVITY * rotation.x, MOUSE_SENSITIVITY * rotation.y, 0);
        }
    }

    @Override
    public void render(Window window) {
        renderEngine.render(window, camera, gameItems);
    }

    @Override
    public void free() {
        renderEngine.free();
        for (GameItem gameItem : gameItems) {
            gameItem.free();
        }
    }
}
