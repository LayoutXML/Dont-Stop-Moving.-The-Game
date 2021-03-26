package view;

import view.graphics.Mesh;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Scene {
    private Map<Mesh, List<GameItem>> meshes = new HashMap<>();
    private Skybox skybox;
    private Lights lights;

    public void addGameItems(GameItem[] gameItems) {
        if (gameItems == null || gameItems.length == 0) {
            return;
        }

        for (GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            meshes.computeIfAbsent(mesh, k -> new ArrayList<>()).add(gameItem);
        }
    }

    public void addGameItems(List<GameItem> gameItems) {
        if (gameItems == null || gameItems.size() == 0) {
            return;
        }

        for (GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            meshes.computeIfAbsent(mesh, k -> new ArrayList<>()).add(gameItem);
        }
    }

    public void free() {
        for (Mesh mesh : meshes.keySet()) {
            mesh.free();
        }
    }
}
