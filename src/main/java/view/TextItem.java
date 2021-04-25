package view;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import view.graphics.CharacterInformation;
import view.graphics.FontTexture;
import view.graphics.Material;
import view.graphics.Mesh;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TextItem {
    private static final float Z_COORDINATE = 0f;
    private static final int VERTICES = 4;

    private String text;
    private final FontTexture fontTexture;

    private float textureScale = 1;
    private float size = 1;
    private Mesh mesh;
    private boolean solid = true;
    private Vector3f position = new Vector3f();
    private Vector3f rotation = new Vector3f();

    public TextItem(String text, FontTexture fontTexture) {
        this.text = text;
        this.fontTexture = fontTexture;

        generateMesh();
    }

    private void generateMesh() {
        List<Float> positions = new ArrayList<>();
        List<Float> coordinates = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        float[] normals = new float[0];

        float currentWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            CharacterInformation characterInformation = fontTexture.getCharacterInformation(text.charAt(i));

            positions.add(currentWidth);
            positions.add(0f);
            positions.add(Z_COORDINATE);
            coordinates.add((float) characterInformation.getXPosition() / (float) fontTexture.getWidth());
            coordinates.add(0f);
            indexes.add(i * VERTICES);

            positions.add(currentWidth);
            positions.add((float) fontTexture.getHeight());
            positions.add(Z_COORDINATE);
            coordinates.add((float) characterInformation.getXPosition() / (float) fontTexture.getWidth());
            coordinates.add(1f);
            indexes.add(i * VERTICES + 1);

            positions.add(currentWidth + characterInformation.getWidth());
            positions.add((float) fontTexture.getHeight());
            positions.add(Z_COORDINATE);
            coordinates.add((float) (characterInformation.getWidth() + characterInformation.getXPosition()) / (float) fontTexture.getWidth());
            coordinates.add(1f);
            indexes.add(i * VERTICES + 2);

            positions.add(currentWidth + characterInformation.getWidth());
            positions.add(0f);
            positions.add(Z_COORDINATE);
            coordinates.add((float) (characterInformation.getWidth() + characterInformation.getXPosition()) / (float) fontTexture.getWidth());
            coordinates.add(0f);
            indexes.add(i * VERTICES + 3);

            indexes.add(i * VERTICES);
            indexes.add(i * VERTICES + 2);

            currentWidth += characterInformation.getWidth();
        }

        float[] positionsArray = convertFloatListToArray(positions);
        float[] coordinatesArray = convertFloatListToArray(coordinates);
        int[] indexArray = indexes.stream().mapToInt(index -> index).toArray();

        Material material = new Material();
        material.setTexture(fontTexture.getTexture());

        Mesh mesh = new Mesh(positionsArray, coordinatesArray, normals, indexArray);
        mesh.setMaterial(material);

        setMesh(mesh);
    }

    private float[] convertFloatListToArray(List<Float> list) {
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public void setText(String text) {
        this.text = text;
        getMesh().freeWithoutTexture();
        generateMesh();
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
