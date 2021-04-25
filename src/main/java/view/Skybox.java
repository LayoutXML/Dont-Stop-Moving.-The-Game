package view;

import lombok.Getter;
import lombok.Setter;
import model.OBJLoader;
import model.exceptions.ResourceException;
import org.joml.Vector3f;
import view.graphics.Material;
import view.graphics.Mesh;
import view.graphics.Texture;

@Getter
@Setter
public class Skybox {
    private float textureScale = 1;
    private float size = 1;
    private Mesh mesh;
    private boolean solid = true;
    private Vector3f position = new Vector3f();
    private Vector3f rotation = new Vector3f();

    public Skybox(String objectFileName, String textureFileName) throws ResourceException {
        Texture texture = new Texture(textureFileName);
        Material material = new Material();

        material.setTexture(texture);
        material.setReflectance(0);

        mesh = OBJLoader.loadMesh(objectFileName);
        mesh.setMaterial(material);

        setPositionFromCoordinates(0, 0, 0);
    }

    public void setPositionFromCoordinates(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void free() {
        if (mesh != null) {
            mesh.free();
        }
    }
}
