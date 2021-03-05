package engine;

import engine.graphics.Material;
import engine.graphics.Mesh;
import engine.graphics.Texture;
import model.GameItem;
import model.exceptions.ResourceException;

public class Skybox extends GameItem {

    public Skybox(String objectFileName, String textureFileName) throws ResourceException {
        super();

        Mesh mesh = OBJLoader.loadMesh(objectFileName);
        Texture texture = new Texture(textureFileName);
        Material material = new Material();

        material.setTexture(texture);
        material.setReflectance(0);

        mesh.setMaterial(material);
        setMesh(mesh);

        setPositionFromCoordinates(0, 0, 0);
    }
}
