package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.joml.Vector3f;
import view.GameItem;

import java.util.List;

@Getter
@AllArgsConstructor
public class LevelFromFileWrapper {
    private List<GameItem> gameItems;
    private Vector3f cameraPosition;
}
