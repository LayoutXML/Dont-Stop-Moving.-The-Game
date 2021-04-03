package view;

import lombok.Getter;
import lombok.Setter;
import model.LevelFromFileWrapper;
import model.OBJLoader;
import model.exceptions.ResourceException;
import model.utils.LevelLoaderUtils;
import org.joml.Vector3f;
import view.graphics.Material;
import view.graphics.Mesh;
import view.graphics.Texture;
import view.lights.DirectionalLight;

import java.util.*;

@Getter
@Setter
public class Level {
    private List<GameItem> gameItems = new ArrayList<>();
    private Map<Mesh, List<GameItem>> meshes = new HashMap<>();
    private Skybox skybox;
    private Lights lights;
    private Vector3f startingPosition;
    private boolean levelLoaded;

    public Level(String fileName) throws ResourceException {
        LevelFromFileWrapper levelFile =  LevelLoaderUtils.loadFile(fileName);
        addGameItems(levelFile.getGameItems());

        this.startingPosition = levelFile.getCameraPosition();

//        setupItems();

        // Setup  SkyBox
        Skybox skyBox = new Skybox("/skybox.obj", "src/textures/skybox.png");
        skyBox.setTextureScale(50f);
        setSkybox(skyBox);

        // Setup Lights
        Lights sceneLight = new Lights();
        setLights(sceneLight);

        // Ambient Light
        sceneLight.setAmbient(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightPosition = new Vector3f(-50, 4, -50);
        sceneLight.setDirectionalLight(new DirectionalLight(new Vector3f(1, 1, 1), lightPosition, lightIntensity));

        levelLoaded = true;
        System.out.println("level loaded");

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

    }

    public void addGameItems(GameItem[] gameItems) {
        if (gameItems == null || gameItems.length == 0) {
            return;
        }

        for (GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            meshes.computeIfAbsent(mesh, k -> new ArrayList<>()).add(gameItem);
        }

        this.gameItems.addAll(Arrays.asList(gameItems));
    }

    public void addGameItems(List<GameItem> gameItems) {
        if (gameItems == null || gameItems.size() == 0) {
            return;
        }

        for (GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            meshes.computeIfAbsent(mesh, k -> new ArrayList<>()).add(gameItem);
        }

        this.gameItems.addAll(gameItems);
    }

    public void update(Vector3f cameraPosition) {
        if (!levelLoaded) {
            return;
        }
        gameItems.forEach(GameItem::update);
    }

    public void free() {
        for (Mesh mesh : meshes.keySet()) {
            mesh.free();
        }
    }
}
