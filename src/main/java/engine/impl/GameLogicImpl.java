package engine.impl;

import engine.*;
import engine.graphics.Material;
import engine.graphics.Mesh;
import engine.lights.Attenuation;
import engine.lights.DirectionalLight;
import engine.lights.PointLight;
import engine.lights.SpotLight;
import model.GameItem;
import engine.graphics.Texture;
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

    private Vector3f ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
    private PointLight[] pointLights;
    private SpotLight[] spotLights;
    private DirectionalLight directionalLight;
    private float lightAngle = -90;
    private float spotLightAngle = 0;
    private float spotIncrement = 1;

    @Override
    public void initialize(Window window) throws InitializationException, ResourceException {
        if (window == null) {
            throw new InitializationException("IE7");
        }

        renderEngine.initialize(window);

        Texture texture = new Texture("src/textures/grassblock.png");
        Mesh mesh = OBJLoader.loadMesh("/cube.obj");
        Material material = new Material();
        material.setTexture(texture);
        material.setReflectance(1f);
        mesh.setMaterial(material);

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

        // TODO: refactor
        Vector3f lightPosition = new Vector3f(1, 1, 1);
        PointLight pointLight = new PointLight(lightPosition, new Vector3f(0, 0, 1), 1f, new Attenuation(0f, 0f, 1f));
        pointLights = new PointLight[]{pointLight};

        lightPosition = new Vector3f(0, 0.0f, 10f);
        pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, 1f, new Attenuation(0f, 0f, 0.02f));
        Vector3f coneDir = new Vector3f(0, 0, -1);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight = new SpotLight(pointLight, coneDir, cutoff);
        spotLights= new SpotLight[]{spotLight, new SpotLight(spotLight)};

        lightPosition = new Vector3f(-1, 0, 0);
        directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, 1f);
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
        renderEngine.render(window, camera, gameItems, ambientLight, pointLights, spotLights, directionalLight);
    }

    @Override
    public void free() {
        renderEngine.free();
        for (GameItem gameItem : gameItems) {
            gameItem.free();
        }
    }
}
