package view;

import lombok.Getter;
import lombok.Setter;
import model.LevelFromFileWrapper;
import model.exceptions.ResourceException;
import model.utils.LevelLoaderUtils;
import org.joml.Vector3f;
import view.graphics.Mesh;
import view.lights.DirectionalLight;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Level {
    private List<GameItem> gameItems = new ArrayList<>();
    private List<GameItem> interactiveGameItems = new ArrayList<>();
    private Map<Mesh, List<GameItem>> meshes = new HashMap<>();
    private Skybox skybox;
    private Lights lights;
    private Vector3f startingPosition;
    private boolean levelLoaded;

    public Level(String fileName) throws ResourceException {
        LevelFromFileWrapper levelFile = LevelLoaderUtils.loadFile(fileName);
        addGameItems(levelFile.getGameItems());

        this.startingPosition = levelFile.getCameraPosition();

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
        this.interactiveGameItems.addAll(gameItems.stream().filter(GameItem::isSolid).collect(Collectors.toList()));
    }

    // Update method design pattern
    public void update() {
        if (!levelLoaded) {
            return;
        }
        interactiveGameItems.forEach(GameItem::update);
    }

    public void free() {
        for (Mesh mesh : meshes.keySet()) {
            mesh.free();
        }
    }
}
