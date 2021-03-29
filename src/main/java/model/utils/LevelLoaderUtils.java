package model.utils;

import model.OBJLoader;
import model.ObjectFromFileWrapper;
import model.ObjectType;
import model.exceptions.ResourceException;
import org.joml.Vector3f;
import view.GameItem;
import view.graphics.Material;
import view.graphics.Mesh;
import view.graphics.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelLoaderUtils {

    /* Level file each line structure:
     * object type
     * Position x
     * Position y
     * Position z
     * Rotation x
     * Rotation y
     * Rotation z
     */

    public static List<GameItem> loadFile(String fileName) throws ResourceException {
        List<String> file = ResourceUtils.readFile(fileName);
        Map<String, List<ObjectFromFileWrapper>> gameObjects = parseFile(file);
        return createGameItems(gameObjects);
    }

    private static Map<String, List<ObjectFromFileWrapper>> parseFile(List<String> file) {
        Map<String, List<ObjectFromFileWrapper>> gameObjects = new HashMap<>();

        file.forEach(line -> {
            String[] tokens = line.split("\\s+");
            if (tokens.length != 7 && tokens.length != 1) {
                try {
                    throw new ResourceException("Level file has invalid structure");
                } catch (ResourceException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (tokens.length == 1) {
                return;
            }

            Vector3f position = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
            Vector3f rotation = new Vector3f(Float.parseFloat(tokens[4]), Float.parseFloat(tokens[5]), Float.parseFloat(tokens[6]));
            ObjectFromFileWrapper objectFromFileWrapper = new ObjectFromFileWrapper(position, rotation);

            List<ObjectFromFileWrapper> list = gameObjects.computeIfAbsent(tokens[0], k -> new ArrayList<>());
            list.add(objectFromFileWrapper);
        });

        return gameObjects;
    }

    private static List<GameItem> createGameItems(Map<String, List<ObjectFromFileWrapper>> gameObjects) throws ResourceException {
        List<GameItem> gameItems = new ArrayList<>();

        for (String type : gameObjects.keySet()) {
            ObjectType objectType;
            try {
                objectType = ObjectType.valueOf(type);
            } catch (IllegalArgumentException e) {
                throw new ResourceException("Unknown object type");
            }

            Texture texture = new Texture(objectType.getTextureFileName());
            Material material = Material.builder()
                    .texture(texture)
                    .reflectance(objectType.getReflectance())
                    .build();

            Mesh mesh = OBJLoader.loadMesh(objectType.getObjectFileName());
            mesh.setMaterial(material);

            gameObjects.get(type).forEach(gameObject -> {
                gameItems.add(GameItem.builder()
                        .size(1)
                        .textureScale(0.5f)
                        .mesh(mesh)
                        .solid(true)
                        .position(gameObject.getPosition())
                        .rotation(gameObject.getRotation())
                        .build());
            });
        }

        return gameItems;
    }
}
