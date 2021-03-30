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
        Vector3f lightPosition = new Vector3f(-50, -100, -50);
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

    }

   /* private void setupItems() throws ResourceException {
        Texture stoneTexture = new Texture("src/textures/blocks/stone.png");
        Mesh mesh = OBJLoader.loadMesh("/cube.obj");
        Material material = new Material();
        material.setTexture(stoneTexture);
        material.setReflectance(.1f);
        mesh.setMaterial(material);

        float[][] mockCoordinates = {
                {0, -1, -1}, {-1, -1, -1}, {1, -1, -1},
                {0, -1, -2}, {-1, -1, -2}, {1, -1, -2},
                {0, -1, -3}, {-1, -1, -3}, {1, -1, -3},
                {0, -1, -4}, {-1, -1, -4}, {1, -1, -4},
                {0, -1, -5}, {-1, -1, -5}, {1, -1, -5},
                {0, 1, -3}, {-1, 0, -3}, {1, 2, -3}
        };
        List<GameItem> gameItems = new ArrayList<>();
        for (float[] mockCoordinate : mockCoordinates) {
            GameItem gameItem = new GameItem();
            gameItem.setMesh(mesh);
            gameItem.setTextureScale(0.5f);
            gameItem.setPositionFromCoordinates(mockCoordinate[0], mockCoordinate[1], mockCoordinate[2]);
            gameItems.add(gameItem);
        }
        addGameItems(gameItems);
    }*/

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

    }

    public void free() {
        for (Mesh mesh : meshes.keySet()) {
            mesh.free();
        }
    }
}
