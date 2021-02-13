package engine.graphics;

import lombok.Getter;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

@Getter
public class Mesh {
    private final int vertexArrayObjectId;
    private int positionBufferObjectId;
    private int colorBufferObjectId;
    private int indexBufferObjectId;
    private final int vertexCount;

    public Mesh(float[] positions, float[] colors, int[] indexes) {
        vertexCount = indexes.length;

        vertexArrayObjectId = glGenVertexArrays();
        glBindVertexArray(vertexArrayObjectId);

        setupPositionBufferObject(positions);
        setupColorBufferObject(colors);
        setupIndexBufferObject(indexes);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void setupPositionBufferObject(float[] positions) {
        positionBufferObjectId = glGenBuffers();
        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(positions.length);
        positionBuffer.put(positions).flip();
        glBindBuffer(GL_ARRAY_BUFFER, positionBufferObjectId);
        glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        MemoryUtil.memFree(positionBuffer);
    }

    private void setupColorBufferObject(float[] colors) {
        colorBufferObjectId = glGenBuffers();
        FloatBuffer colorBuffer = MemoryUtil.memAllocFloat(colors.length);
        colorBuffer.put(colors).flip();
        glBindBuffer(GL_ARRAY_BUFFER, colorBufferObjectId);
        glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
        MemoryUtil.memFree(colorBuffer);
    }

    private void setupIndexBufferObject(int[] indexes) {
        indexBufferObjectId = glGenBuffers();
        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indexes.length);
        indexBuffer.put(indexes).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferObjectId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
        MemoryUtil.memFree(indexBuffer);
    }

    public void free() {
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(positionBufferObjectId);
        glDeleteBuffers(colorBufferObjectId);
        glDeleteBuffers(indexBufferObjectId);
        glBindVertexArray(0);
        glDeleteVertexArrays(vertexArrayObjectId);
    }

    public void render() {
        glBindVertexArray(vertexArrayObjectId);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }
}
