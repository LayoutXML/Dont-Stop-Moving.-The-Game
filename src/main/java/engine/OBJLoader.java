package engine;

import engine.graphics.Face;
import engine.graphics.IndexGroup;
import engine.graphics.Mesh;
import model.exceptions.ResourceException;
import org.joml.Vector2f;
import org.joml.Vector3f;
import utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

public class OBJLoader {
    public static Mesh loadMesh(String fileName) throws ResourceException {
        List<String> file = ResourceUtils.readFile(fileName);

        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textureCoordinates = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        for (String line : file) {
            String[] tokens = line.split("\\s+");
            if (tokens.length == 0) {
                throw new ResourceException("RE5");
            }
            switch (tokens[0]) {
                case "v":
                    createVertexOrNormals(vertices, tokens);
                    break;
                case "vt":
                    createTextureCoordinate(textureCoordinates, tokens);
                    break;
                case "vn":
                    createVertexOrNormals(normals, tokens);
                    break;
                case "f":
                    createFace(faces, tokens);
                    break;
                default:
                    break;
            }
        }
        return orderLists(vertices, textureCoordinates, normals, faces);
    }

    private static void createVertexOrNormals(List<Vector3f> vertices, String[] tokens) throws ResourceException {
        if (tokens.length < 4) {
            throw new ResourceException("RE6 " + tokens[0]);
        }
        vertices.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
    }

    private static void createTextureCoordinate(List<Vector2f> textures, String[] tokens) throws ResourceException {
        if (tokens.length < 3) {
            throw new ResourceException("RE7");
        }
        textures.add(new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
    }

    private static void createFace(List<Face> faces, String[] tokens) throws ResourceException {
        if (tokens.length < 4) {
            throw new ResourceException("RE8");
        }
        faces.add(new Face(tokens[1], tokens[2], tokens[3]));
    }

    private static Mesh orderLists(List<Vector3f> vertices, List<Vector2f> textureCoordinates, List<Vector3f> normals, List<Face> faces) {
        List<Integer> indexes = new ArrayList<>();
        float[] vertexArray = new float[vertices.size() * 3];

        for (int i = 0; i < vertices.size(); i++) {
            Vector3f vertex = vertices.get(i);

            vertexArray[i * 3] = vertex.x;
            vertexArray[i * 3 + 1] = vertex.y;
            vertexArray[i * 3 + 2] = vertex.z;
        }

        float[] textureCoordinateArray = new float[vertices.size() * 2];
        float[] normalArray = new float[vertices.size() * 3];

        for (Face face : faces) {
            IndexGroup[] faceVertexIndexes = face.getFaceVertexIndexes();

            for (IndexGroup faceVertexIndex : faceVertexIndexes) {
                processFaceVertex(faceVertexIndex, textureCoordinates, normals, indexes, textureCoordinateArray, normalArray);
            }
        }

        int[] indexesArray = indexes.stream().mapToInt(value -> value).toArray();

        return new Mesh(vertexArray, textureCoordinateArray, normalArray, indexesArray);
    }

    private static void processFaceVertex(IndexGroup faceVertexIndex, List<Vector2f> textureCoordinates, List<Vector3f> normals, List<Integer> indexes, float[] textureCoordinateArray, float[] normalArray) {
        int position = faceVertexIndex.getIndexPosition();
        indexes.add(position);

        if (faceVertexIndex.getIndexTextureCoordinate() >= 0) {
            Vector2f textureCoordinate = textureCoordinates.get(faceVertexIndex.getIndexTextureCoordinate());
            textureCoordinateArray[position * 2] = textureCoordinate.x;
            textureCoordinateArray[position * 2 + 1] = 1 - textureCoordinate.y;
        }

        if (faceVertexIndex.getIndexVectorNormal() >= 0) {
            Vector3f normal = normals.get(faceVertexIndex.getIndexVectorNormal());
            normalArray[position * 3] = normal.x;
            normalArray[position * 3 + 1] = normal.y;
            normalArray[position * 3 + 2] = normal.z;
        }
    }
}
