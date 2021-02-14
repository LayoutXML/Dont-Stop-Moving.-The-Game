package engine;

import engine.graphics.Shaders;
import engine.graphics.Transformation;
import model.GameItem;
import model.exceptions.InitializationException;
import model.exceptions.ResourceException;
import org.joml.Matrix4f;
import utils.ResourceUtils;

import static org.lwjgl.opengl.GL11.*;

public class RenderEngine {
    private static final float FIELD_OF_VIEW = (float) Math.toRadians(70d);
    private static final float Z_FAR = 1000f;
    private static final float Z_NEAR = 0.01f;

    private final Transformation transformation = new Transformation();
    private Shaders shaders;

    public void initialize(Window window) throws InitializationException, ResourceException {
        shaders = new Shaders();
        shaders.createVertexShader(ResourceUtils.loadResource("/vertex.vs"));
        shaders.createFragmentShader(ResourceUtils.loadResource("/fragment.fs"));
        shaders.link();

        shaders.createUniform("projection");
        shaders.createUniform("models");
        shaders.createUniform("sampler");
    }

    public void render(Window window, Camera camera, GameItem[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight()); // TODO try to move this to window class
            window.setResized(false);
        }

        shaders.bind();

        Matrix4f projection = transformation.getProjectionWithPerspective(FIELD_OF_VIEW, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaders.setUniform("projection", projection);
        shaders.setUniform("sampler", 0);

        Matrix4f view = transformation.getCameraView(camera);

        for (GameItem gameItem : gameItems) {
            Matrix4f world = transformation.getModelView(gameItem, view);
            shaders.setUniform("models", world);
            gameItem.getMesh().render(); // TODO move rendering to gameitem
        }

        shaders.unbind();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void free() {
        if (shaders != null) {
            shaders.free();
        }
    }
}
