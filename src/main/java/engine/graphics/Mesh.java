package engine.graphics;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

@Getter
@Setter
public class Mesh {

    private final int vertexArraysId;
    private final List<Integer> bufferIdList = new ArrayList<>();
    private final int vertexCount;
    private Material material;

    public Mesh(float[] positions, float[] textureCoordinates, float[] normals, int[] indexes) {
        vertexCount = indexes.length;

        vertexArraysId = glGenVertexArrays();
        glBindVertexArray(vertexArraysId);

        setupPositionBufferObject(positions);
        setupTextureBufferObject(textureCoordinates);
        setupNormalBufferObject(normals);
        setupIndexBufferObject(indexes);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void setupPositionBufferObject(float[] positions) {
        int bufferId = glGenBuffers();
        bufferIdList.add(bufferId);

        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(positions.length);
        positionBuffer.put(positions).flip();
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, positionBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        MemoryUtil.memFree(positionBuffer);
    }

    private void setupTextureBufferObject(float[] textureCoordinates) {
        int bufferId = glGenBuffers();
        bufferIdList.add(bufferId);

        FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(textureCoordinates.length);
        textureBuffer.put(textureCoordinates).flip();
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        MemoryUtil.memFree(textureBuffer);
    }

    private void setupNormalBufferObject(float[] normals) {
        int bufferId = glGenBuffers();
        bufferIdList.add(bufferId);

        FloatBuffer normalBuffer = MemoryUtil.memAllocFloat(normals.length);
        normalBuffer.put(normals).flip();
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, normalBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        MemoryUtil.memFree(normalBuffer);
    }

    private void setupIndexBufferObject(int[] indexes) {
        int bufferId = glGenBuffers();
        bufferIdList.add(bufferId);

        IntBuffer indexBuffer = MemoryUtil.memAllocInt(indexes.length);
        indexBuffer.put(indexes).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

        MemoryUtil.memFree(indexBuffer);
    }

    public void render() {
        Texture texture = material.getTexture();
        if (texture != null) {
            glActiveTexture(GL_TEXTURE);
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }

        glBindVertexArray(vertexArraysId);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);

        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void free() {
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        for (int bufferId : bufferIdList) {
            glDeleteBuffers(bufferId);
        }

        if (material != null) {
            Texture texture = material.getTexture();
            if (texture != null) {
                texture.free();
            }
        }

        glBindVertexArray(0);
        glDeleteVertexArrays(vertexArraysId);
    }

    public void freeWithoutTexture() {
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        for (int bufferId : bufferIdList) {
            glDeleteBuffers(bufferId);
        }

        glBindVertexArray(0);
        glDeleteVertexArrays(vertexArraysId);
    }

    public boolean hasTexture() {
        return material != null && material.getTexture() != null;
    }
}
