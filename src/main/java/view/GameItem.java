package view;

import lombok.Getter;
import lombok.Setter;
import model.OBJLoader;
import model.ObjectType;
import model.exceptions.ResourceException;
import org.joml.Vector2f;
import org.joml.Vector3f;
import view.graphics.Material;
import view.graphics.Mesh;
import view.graphics.Texture;

@Getter
@Setter
public abstract class GameItem {
    private float textureScale = 1;
    private float size = 1;
    private Mesh mesh;
    private boolean solid = true;
    private Vector3f position = new Vector3f();
    private Vector3f rotation = new Vector3f();
    private ObjectType objectType = ObjectType.STONE;

    protected String objectFileName;
    private String textureFileName;
    private float reflectance;

    public GameItem(String objectFileName, String textureFileName, float reflectance) throws ResourceException {
        Texture texture = new Texture(textureFileName);
        Material material = Material.builder()
                .texture(texture)
                .reflectance(reflectance)
                .build();

        mesh = OBJLoader.loadMesh(objectFileName);
        mesh.setMaterial(material);
    }

    public void setPositionFromCoordinates(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setRotationFromCoordinates(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public Vector2f getBoundsX() {
        return new Vector2f(position.x - size / 2, position.x + size / 2);
    }

    public Vector2f getBoundsY() {
        return new Vector2f(position.y - size / 2, position.y + size / 2);
    }

    public Vector2f getBoundsZ() {
        return new Vector2f(position.z - size / 2, position.z + size / 2);
    }

    public void update() {}

    public void free() {
        if (mesh != null) {
            mesh.free();
        }
    }
}
