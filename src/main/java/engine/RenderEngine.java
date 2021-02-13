package engine;

import engine.graphics.Shaders;
import engine.graphics.Transformation;
import model.GameItem;
import model.exceptions.InitializationException;
import org.joml.Matrix4f;
import utils.ResourceUtils;

import static org.lwjgl.opengl.GL11.*;

public class RenderEngine {
    private static final float FIELD_OF_VIEW = (float) Math.toRadians(70d);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000f;

    private final Transformation transformation = new Transformation();
    private Shaders shaders;

    public void initialize(Window window) throws InitializationException {
        shaders = new Shaders();
        shaders.createVertexShader(ResourceUtils.loadResource("/vertex.vs"));
        shaders.createFragmentShader(ResourceUtils.loadResource("/fragment.fs"));
        shaders.link();

        shaders.createUniform("projection");
        shaders.createUniform("world");

        window.setClearColor(0f, 0f, 0f, 0f);
    }

    public void render(Window window, GameItem[] gameItems) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight()); // TODO try to move this to window class
            window.setResized(false);
        }

        shaders.bind();

        Matrix4f projection = transformation.getProjectionWithPerspective(FIELD_OF_VIEW, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaders.setUniform("projection", projection);

        for (GameItem gameItem : gameItems) {
            Matrix4f world = transformation.getWorldWithRotationAndScale(gameItem.getPosition(), gameItem.getRotation(), gameItem.getScale());
            shaders.setUniform("world", world);
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
