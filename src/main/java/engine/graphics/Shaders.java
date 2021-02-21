package engine.graphics;

import engine.lights.DirectionalLight;
import engine.lights.PointLight;
import engine.lights.SpotLight;
import model.exceptions.InitializationException;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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

    public void createPointLightsUniform(String name, int size) throws InitializationException {
        for (int i = 0; i < size; i++) {
            createPointLightUniform(name + "[" + i + "]");
        }
    }

    public void createPointLightUniform(String name) throws InitializationException {
        createUniform(name + ".color");
        createUniform(name + ".position");
        createUniform(name + ".intensity");
        createUniform(name + ".attenuation.constant");
        createUniform(name + ".attenuation.linear");
        createUniform(name + ".attenuation.exponent");
    }

    public void createSpotLightsUniform(String name, int size) throws InitializationException {
        for (int i = 0; i < size; i++) {
            createSpotLightUniform(name + "[" + i + "]");
        }
    }

    public void createSpotLightUniform(String name) throws InitializationException {
        createPointLightUniform(name + ".pointLight");
        createUniform(name + ".direction");
        createUniform(name + ".cutoff");
    }

    public void createDirectionalLightUniform(String name) throws InitializationException {
        createUniform(name + ".color");
        createUniform(name + ".direction");
        createUniform(name + ".intensity");
    }

    public void createMaterialUniform(String name) throws InitializationException {
        createUniform(name + ".ambient");
        createUniform(name + ".diffuse");
        createUniform(name + ".specular");
        createUniform(name + ".hasTexture");
        createUniform(name + ".reflectance");
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
        glUniform1i(location, value);
    }

    public void setUniform(String name, float value) {
        int location = uniformLocations.get(name);
        glUniform1f(location, value);
    }

    public void setUniform(String name, Vector3f value) {
        int location = uniformLocations.get(name);
        glUniform3f(location, value.x, value.y, value.z);
    }

    public void setUniform(String name, Vector4f value) {
        int location = uniformLocations.get(name);
        glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    public void setUniform(String name, PointLight[] pointLights) {
        if (pointLights == null || pointLights.length == 0) {
            return;
        }

        for (int i = 0; i < pointLights.length; i++) {
            setUniform(name + "[" + i + "]", pointLights[i]);
        }
    }

    public void setUniform(String name, PointLight pointLight) {
        setUniform(name + ".color", pointLight.getColor());
        setUniform(name + ".position", pointLight.getPosition());
        setUniform(name + ".intensity", pointLight.getIntensity());
        setUniform(name + ".attenuation.constant", pointLight.getAttenuation().getConstant());
        setUniform(name + ".attenuation.linear", pointLight.getAttenuation().getLinear());
        setUniform(name + ".attenuation.exponent", pointLight.getAttenuation().getExponent());
    }

    public void setUniform(String name, SpotLight[] spotLights) {
        if (spotLights == null || spotLights.length == 0) {
            return;
        }

        for (int i = 0; i < spotLights.length; i++) {
            setUniform(name + "[" + i + "]", spotLights[i]);
        }
    }

    public void setUniform(String name, SpotLight spotLight) {
        setUniform(name + ".pointLight", spotLight.getPointLight());
        setUniform(name + ".direction", spotLight.getDirection());
        setUniform(name + ".cutoff", spotLight.getCutoff());
    }

    public void setUniform(String name, DirectionalLight directionalLight) {
        setUniform(name + ".color", directionalLight.getColor());
        setUniform(name + ".direction", directionalLight.getDirection());
        setUniform(name + ".intensity", directionalLight.getIntensity());
    }

    public void setUniform(String name, Material material) {
        setUniform(name + ".ambient", material.getAmbient());
        setUniform(name + ".diffuse", material.getDiffuse());
        setUniform(name + ".specular", material.getSpecular());
        setUniform(name + ".hasTexture", material.hasTexture() ? 1 : 0);
        setUniform(name + ".reflectance", material.getReflectance());
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
