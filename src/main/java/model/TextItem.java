package model;

import engine.graphics.CharacterInformation;
import engine.graphics.FontTexture;
import engine.graphics.Material;
import engine.graphics.Mesh;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TextItem extends GameItem {
    private static final float Z_COORDINATE = 0f;
    private static final int VERTICES = 4;

    private String text;
    private final FontTexture fontTexture;

    public TextItem(String text, FontTexture fontTexture) {
        super();

        this.text = text;
        this.fontTexture = fontTexture;

        generateMesh();
    }

    private void generateMesh() {
        List<Float> positions = new ArrayList<>(); // TODO: refactor to arrays of size text.length * 4 or 6
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

}
