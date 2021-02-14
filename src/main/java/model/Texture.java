package model;

import lombok.Getter;
import model.exceptions.ResourceException;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

@Getter
public class Texture {
    private int id;
    private final String fileName;

    public Texture(String fileName) throws ResourceException {
        this.fileName = fileName;
        loadTexture();
    }

    public void loadTexture() throws ResourceException {
        if (fileName == null) {
            throw new ResourceException("RE2");
        }

        IntBuffer xBuffer;
        IntBuffer yBuffer;
        IntBuffer channelsBuffer;
        ByteBuffer byteBuffer;

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            xBuffer = memoryStack.mallocInt(1);
            yBuffer = memoryStack.mallocInt(1);
            channelsBuffer = memoryStack.mallocInt(1);

            byteBuffer = stbi_load(fileName, xBuffer, yBuffer, channelsBuffer, 4);
            if (byteBuffer == null) {
                throw new ResourceException("RE3");
            }
        }

        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, xBuffer.get(), yBuffer.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(byteBuffer);
    }

    public void bind() throws ResourceException {
        if (id == 0) {
            throw new ResourceException("RE3");
        }
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void free() {
        if (id == 0) {
            return;
        }
        glDeleteTextures(id);
    }
}
