package model;

import controller.InputManager;
import model.exceptions.InitializationException;
import model.exceptions.ResourceException;
import org.joml.Vector2f;
import org.joml.Vector3f;
import view.*;
import view.graphics.Material;
import view.graphics.Mesh;
import view.graphics.Texture;
import view.lights.DirectionalLight;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class GameLogic {
    private final float MOUSE_SENSITIVITY = 0.3f;
    private final float MOVEMENT_SPEED = 0.1f;

    private final RenderEngine renderEngine = new RenderEngine();
    private final Camera camera = new Camera();
    private final InputManager inputManager = new InputManager();
    private final Window window = new Window();
    private final Vector3f cameraMovement = new Vector3f();

    private Scene scene;
    private Status status;

    public GameLogic() throws ResourceException, InitializationException {
        initialize();
    }

    public void initialize() throws InitializationException, ResourceException {
        window.initialize();
        renderEngine.initialize(window);
        inputManager.initialize(window);

        scene = new Scene();
        status = new Status();

        Texture texture = new Texture("src/textures/blocks/stone.png");
        Mesh mesh = OBJLoader.loadMesh("/cube.obj");
        Material material = new Material();
        material.setTexture(texture);
        material.setReflectance(.1f);
        mesh.setMaterial(material);

        /*float[][] mockCoordinates = {
                {0, -1, -1}, {-1, -1, -1}, {1, -1, -1},
                {0, -1, -2}, {-1, -1, -2}, {1, -1, -2},
                {0, -1, -3}, {-1, -1, -3}, {1, -1, -3},
                {0, -1, -4}, {-1, -1, -4}, {1, -1, -4},
                {0, -1, -5}, {-1, -1, -5}, {1, -1, -5},
                {0, 0, -3}
        };*/
        float[][] mockCoordinates = {
                {1, 0, 0}
        };
        List<GameItem> gameItems = new ArrayList<>();
        for (float[] mockCoordinate : mockCoordinates) {
            GameItem gameItem = new GameItem();
            gameItem.setMesh(mesh);
            gameItem.setScale(0.5f);
            gameItem.setPositionFromCoordinates(mockCoordinate[0], mockCoordinate[1], mockCoordinate[2]);
            gameItems.add(gameItem);
        }
        scene.addGameItems(gameItems);

        // Setup  SkyBox
        Skybox skyBox = new Skybox("/skybox.obj", "src/textures/skybox.png");
        skyBox.setScale(50f);
        scene.setSkybox(skyBox);

        // Setup Lights
        Lights sceneLight = new Lights();
        scene.setLights(sceneLight);

        // Ambient Light
        sceneLight.setAmbient(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(-1, 0, 0);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));

        /*GameItem gameItem = new GameItem(mesh);
        gameItem.setScale(0.5f);
        gameItem.setPositionFromCoordinates(0, -1, -1);

        GameItem gameItem1 = new GameItem(mesh);
        gameItem1.setScale(0.5f);
        gameItem1.setPositionFromCoordinates(0, -1, -2);

        GameItem gameItem2 = new GameItem(mesh);
        gameItem2.setScale(0.5f);
        gameItem2.setPositionFromCoordinates(0, -1, -3);*/

//        gameItems = new GameItem[]{gameItem, gameItem1, gameItem2};

        // TODO: refactor
        /*Vector3f lightPosition = new Vector3f(1, 1, 1);
        PointLight pointLight = new PointLight(lightPosition, new Vector3f(0, 0, 1), 1f, new Attenuation(0f, 0f, 1f));
        pointLights = new PointLight[]{pointLight};

        Vector3f lightPosition = new Vector3f(0, 0.0f, 10f);
        pointLight = new PointLight(new Vector3f(1, 1, 1), lightPosition, 1f, new Attenuation(0f, 0f, 0.02f));
        Vector3f coneDir = new Vector3f(0, 0, -1);
        float cutoff = (float) Math.cos(Math.toRadians(140));
        SpotLight spotLight = new SpotLight(pointLight, coneDir, cutoff);
        spotLights= new SpotLight[]{spotLight, new SpotLight(spotLight)};*/

        status.startTimer();
    }

    public void input() {
        inputManager.mouseInput();

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

    public void update() {
        // TODO: handle collision
        camera.movePosition(MOVEMENT_SPEED * cameraMovement.x, MOVEMENT_SPEED * cameraMovement.y, MOVEMENT_SPEED * cameraMovement.z);
        if (inputManager.isRightMouseButtonPressed()) {
            Vector2f rotation = inputManager.getDisplay();
            camera.moveRotation(MOUSE_SENSITIVITY * rotation.x, MOUSE_SENSITIVITY * rotation.y, 0);
        }
    }

    public void render() {
        status.render(window);
        renderEngine.render(window, camera, scene, status);
        window.update();
    }

    public boolean endGame() {
        return window.windowShouldClose();
    }

    public void free() {
        status.free();
        scene.free();
        renderEngine.free();
    }
}
