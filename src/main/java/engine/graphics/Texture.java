package engine.graphics;

import lombok.Getter;
import model.exceptions.ResourceException;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;

@Getter
public class Texture {
    private int id;
    private String fileName;
    private ByteBuffer image;

    public Texture(String fileName) throws ResourceException {
        this.fileName = fileName;
        loadTexture();
    }

    public Texture(ByteBuffer byteBuffer) throws ResourceException {
        this.image = byteBuffer;
        loadTexture();
    }

    public void loadTexture() throws ResourceException {
        if (fileName == null && image == null) {
            throw new ResourceException("RE2");
        }

        IntBuffer xBuffer;
        IntBuffer yBuffer;
        IntBuffer channelsBuffer;
        ByteBuffer byteBuffer;

        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            xBuffer = memoryStack.mallocInt(2);
            yBuffer = memoryStack.mallocInt(2);
            channelsBuffer = memoryStack.mallocInt(1);

            byteBuffer = null;
            if (fileName != null && !fileName.isEmpty()) {
                byteBuffer = stbi_load(fileName, xBuffer, yBuffer, channelsBuffer, 4);
            } else {
                byteBuffer = stbi_load_from_memory(image, xBuffer, yBuffer, channelsBuffer, 4);
            }

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
            throw new ResourceException("RE4");
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
