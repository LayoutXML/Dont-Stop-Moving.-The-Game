package model.utils;

import model.LevelFromFileWrapper;
import model.OBJLoader;
import model.ObjectFromFileWrapper;
import model.ObjectType;
import model.exceptions.ResourceException;
import model.objects.*;
import org.joml.Vector3f;
import view.GameItem;
import view.graphics.Material;
import view.graphics.Mesh;
import view.graphics.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static LevelFromFileWrapper loadFile(String fileName) throws ResourceException {
        List<String> file = ResourceUtils.readFile(fileName);
        if (file.size() < 1) {
            throw new ResourceException("Level file is too short");
        }

        file = cleanupFile(file);

        Vector3f cameraPosition = parseCameraPosition(file.remove(0));

        Map<String, List<ObjectFromFileWrapper>> gameObjects = parseFile(file);
        List<GameItem> gameItems = createGameItems(gameObjects);

        return new LevelFromFileWrapper(gameItems, cameraPosition);
    }

    private static List<String> cleanupFile(List<String> file) throws ResourceException {
        List<String> cleanFile = file.stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty() && !line.startsWith("//"))
                .collect(Collectors.toList());
        if (cleanFile.size() < 1) {
            throw new ResourceException("Level file is too short");
        }
        return cleanFile;
    }

    private static Vector3f parseCameraPosition(String cameraPositionLine) throws ResourceException {
        String[] tokens = cameraPositionLine.split("\\s+");
        if (tokens.length != 3) {
            throw new ResourceException("Level file has invalid structure");
        }

        return new Vector3f(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
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

            gameObjects.get(type).forEach(gameObject -> {
                try {
                    GameItem gameItem = createGameItemByType(objectType);
                    gameItem.setPosition(gameObject.getPosition());
                    gameItem.setRotation(gameItem.getRotation());

                    gameItems.add(gameItem);
                } catch (ResourceException e) {
                    e.printStackTrace();
                }
            });
        }

        return gameItems;
    }

    private static GameItem createGameItemByType(ObjectType objectType) throws ResourceException {
        switch (objectType) {
            case BRICK_BLUE:
                return new BrickBlue();
            case BRICK_GRAY:
                return new BrickGray();
            case BRICK_GREEN:
                return new BrickGreen();
            case BRICK_LIGHTGREEN:
                return new BrickLightGreen();
            case BRICK_PURPLE:
                return new BrickPurple();
            case BRICK_RED:
                return new BrickRed();
            case BRICK_YELLOW:
                return new BrickYellow();
            case DIRT:
                return new Dirt();
            case GRASS:
                return new Grass();
            case GRASS_DIRT:
                return new GrassDirt();
            case GRASS_SNOW:
                return new GrassSnow();
            case ICE:
                return new Ice();
            case LAVA:
                return new Lava();
            case SNOW:
                return new Snow();
            case STONE_DARKGRAY:
                return new StoneDarkGray();
            case STONE_LIGHTGRAY:
                return new StoneLightGray();
            case WATER:
                return new Water();
            case STONE:
            default:
                return new Stone();
        }
    }
}
