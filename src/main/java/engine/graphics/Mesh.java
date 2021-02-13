package engine.graphics;

import lombok.Getter;
import model.Texture;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

@Getter
public class Mesh {
    private final int vertexArraysId;
    private final List<Integer> bufferIdList = new ArrayList<>();
    private final int vertexCount;
    private final Texture texture;

    public Mesh(float[] positions, float[] textture, int[] indexes, Texture texture) {
        this.texture = texture;

        vertexCount = indexes.length;

        vertexArraysId = glGenVertexArrays();
        glBindVertexArray(vertexArraysId);

        setupPositionBufferObject(positions);
        setupTextureBufferObject(textture);
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

    private void setupTextureBufferObject(float[] texture) {
        int bufferId = glGenBuffers();
        bufferIdList.add(bufferId);

        FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(texture.length);
        textureBuffer.put(texture).flip();
        glBindBuffer(GL_ARRAY_BUFFER, bufferId);
        glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);

        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        MemoryUtil.memFree(textureBuffer);
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

    public void free() {
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int bufferId: bufferIdList) {
            glDeleteBuffers(bufferId);
        }
        texture.free();
        glBindVertexArray(0);
        glDeleteVertexArrays(vertexArraysId);
    }

    public void render() {
        glActiveTexture(GL_TEXTURE);
        glBindTexture(GL_TEXTURE_2D, texture.getId());
        glBindVertexArray(vertexArraysId);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }
}
