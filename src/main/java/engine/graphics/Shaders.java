package engine.graphics;

import model.exceptions.InitializationException;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20C.*;

public class Shaders {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;
    private final Map<String, Integer> uniformLocations = new HashMap<>();

    public Shaders() throws InitializationException {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new InitializationException("IE3");
        }
    }

    public void createVertexShader(String shaderCode) throws InitializationException {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws InitializationException {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int type) throws InitializationException {
        int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            throw new InitializationException("IE4");
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new InitializationException("IE5");
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void createUniform(String name) throws InitializationException {
        int uniformLocation = glGetUniformLocation(programId, name);
        if (uniformLocation < 0) {
            throw new InitializationException("IE8");
        }

        uniformLocations.put(name, uniformLocation);
    }

    public void setUniform(String name, Matrix4f matrix4f) {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            int location = uniformLocations.get(name);
            FloatBuffer floatBuffer = matrix4f.get(memoryStack.mallocFloat(16));
            glUniformMatrix4fv(location, false, floatBuffer);
        } catch (Exception ignored) {
        }
    }

    public void setUniform(String name, int value) {
        int location = uniformLocations.get(name);
        glUniform1i(location, 0);
    }

    public void link() throws InitializationException {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new InitializationException("IE6");
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }

        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void free() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
