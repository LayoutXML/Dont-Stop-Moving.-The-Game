package view.graphics;

import lombok.Getter;
import model.exceptions.ResourceException;

@Getter
public class Face {
    private IndexGroup[] faceVertexIndexes = new IndexGroup[3];

    public Face(String line0, String line1, String line2) throws ResourceException {
        faceVertexIndexes[0] = parseLine(line0);
        faceVertexIndexes[1] = parseLine(line1);
        faceVertexIndexes[2] = parseLine(line2);
    }

    private IndexGroup parseLine(String line) throws ResourceException {
        IndexGroup indexGroup = new IndexGroup();

        String[] tokens = line.split("/");
        if (tokens.length == 0) {
            throw new ResourceException("RE9");
        }

        indexGroup.setIndexPosition(Integer.parseInt(tokens[0]) - 1);

        if (tokens.length > 1) {
            String textureCoordinates = tokens[1];
            indexGroup.setIndexTextureCoordinate(textureCoordinates.length() > 0 ? Integer.parseInt(textureCoordinates) - 1 : -1);

            if (tokens.length > 2) {
                indexGroup.setIndexVectorNormal(Integer.parseInt(tokens[2]) - 1);
            }
        }

        return indexGroup;
    }
}
