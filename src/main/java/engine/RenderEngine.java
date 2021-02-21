package engine;

import engine.graphics.Shaders;
import engine.graphics.Transformation;
import engine.lights.DirectionalLight;
import engine.lights.PointLight;
import engine.lights.SpotLight;
import model.GameItem;
import model.exceptions.InitializationException;
import model.exceptions.ResourceException;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import utils.ResourceUtils;

import static org.lwjgl.opengl.GL11.*;

public class RenderEngine {
    private static final float FIELD_OF_VIEW = (float) Math.toRadians(70d);
    private static final float Z_FAR = 1000f;
    private static final float Z_NEAR = 0.01f;
    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;

    private final Transformation transformation = new Transformation();
    private Shaders shaders;
    private final float specular = 10f;

    public void initialize(Window window) throws InitializationException, ResourceException {
        shaders = new Shaders();
        shaders.createVertexShader(ResourceUtils.loadResource("/vertex.vs"));
        shaders.createFragmentShader(ResourceUtils.loadResource("/fragment.fs"));
        shaders.link();

        shaders.createUniform("projection");
        shaders.createUniform("models");
        shaders.createUniform("sampler");
        shaders.createUniform("specular");
        shaders.createMaterialUniform("material");
        shaders.createUniform("ambient");
        shaders.createPointLightsUniform("pointLights", MAX_POINT_LIGHTS);
        shaders.createSpotLightsUniform("spotLights", MAX_SPOT_LIGHTS);
        shaders.createDirectionalLightUniform("directionalLight");
    }

    public void render(Window window, Camera camera, GameItem[] gameItems, Vector3f ambient, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {
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

        renderLights(view, ambient, pointLights, spotLights, directionalLight);

        for (GameItem gameItem : gameItems) {
            Matrix4f world = transformation.getModelView(gameItem, view);
            shaders.setUniform("models", world);
            shaders.setUniform("material", gameItem.getMesh().getMaterial());
            gameItem.getMesh().render(); // TODO move rendering to gameitem
        }

        shaders.unbind();
    }

    private void renderLights(Matrix4f view, Vector3f ambient, PointLight[] pointLights, SpotLight[] spotLights, DirectionalLight directionalLight) {
        shaders.setUniform("ambient", ambient);
        shaders.setUniform("specular", specular);

        renderPointLights(view, pointLights);
        renderSpotLights(view, spotLights);
        renderDirectionalLight(view, directionalLight);
    }

    private void renderPointLights(Matrix4f view, PointLight[] pointLights) {
        if (pointLights == null) {
            return;
        }

        for (int i = 0; i < pointLights.length; i++) {
            PointLight pointLight = new PointLight(pointLights[i]);
            Vector3f position = pointLight.getPosition();

            Vector4f position4 = new Vector4f(position, 1);
            position4.mul(view);

            position.x = position4.x;
            position.y = position4.y;
            position.z = position4.z;

            shaders.setUniform("pointLights[" + i + "]", pointLight);
        }
    }

    private void renderSpotLights(Matrix4f view, SpotLight[] spotLights) {
        if (spotLights == null) {
            return;
        }

        for (int i = 0; i < spotLights.length; i++) {
            SpotLight spotLight = new SpotLight(spotLights[i]);
            Vector3f position = spotLight.getPointLight().getPosition();
            Vector4f direction = new Vector4f(spotLight.getDirection(), 0);
            direction.mul(view);

            spotLight.setDirection(new Vector3f(direction.x, direction.y, direction.z));

            Vector4f position4 = new Vector4f(position, 1);
            position4.mul(view);

            position.x = position4.x;
            position.y = position4.y;
            position.z = position4.z;

            shaders.setUniform("spotLights[" + i + "]", spotLight);
        }
    }

    private void renderDirectionalLight(Matrix4f view, DirectionalLight directionalLight) {
        DirectionalLight currentDirectionalLight = new DirectionalLight(directionalLight);
        Vector4f direction = new Vector4f(currentDirectionalLight.getDirection(), 0);
        direction.mul(view);

        currentDirectionalLight.setDirection(new Vector3f(direction.x, direction.y, direction.z));

        shaders.setUniform("directionalLight", currentDirectionalLight);
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
