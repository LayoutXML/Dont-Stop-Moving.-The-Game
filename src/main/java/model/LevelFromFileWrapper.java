package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Vector3f;
import view.GameItem;

import java.util.List;

@Getter
@AllArgsConstructor
public class LevelFromFileWrapper {
    private final List<GameItem> gameItems;
    private final Vector3f cameraPosition;
}
