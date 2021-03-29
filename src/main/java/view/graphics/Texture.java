package view.graphics;

import lombok.Getter;
import model.exceptions.ResourceException;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12C.GL_CLAMP_TO_EDGE;
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
            throw new ResourceException("Texture invalid");
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
                throw new ResourceException("Texture file read error");
            }
        }

        id = glGenTextures();

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        
        glBindTexture(GL_TEXTURE_2D, id);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, xBuffer.get(), yBuffer.get(), 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        stbi_image_free(byteBuffer);
    }

    public void bind() throws ResourceException {
        if (id == 0) {
            throw new ResourceException("Texture binding error");
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
