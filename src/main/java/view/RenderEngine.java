package view;

import view.graphics.Mesh;
import view.graphics.Shaders;
import view.graphics.Transformation;
import view.lights.DirectionalLight;
import view.lights.PointLight;
import view.lights.SpotLight;
import model.exceptions.InitializationException;
import model.exceptions.ResourceException;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import model.utils.ResourceUtils;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class RenderEngine {
    private static final float FIELD_OF_VIEW = (float) Math.toRadians(90d);
    private static final float Z_FAR = 1000f;
    private static final float Z_NEAR = 0.01f;
    private static final int MAX_POINT_LIGHTS = 5;
    private static final int MAX_SPOT_LIGHTS = 5;

    private final float specular = 10f;

    private final Transformation transformation = new Transformation();
    private Shaders shaders;
    private Shaders statusShader;
    private Shaders skybox;

    public RenderEngine() throws ResourceException, InitializationException {
        initialize();
    }

    public void initialize() throws InitializationException, ResourceException {
        initializeSkybox();
        initializeGameShader();
        initializeStatusShader();
    }

    private void initializeSkybox() throws InitializationException, ResourceException {
        skybox = new Shaders();
        skybox.createVertexShader(ResourceUtils.loadResource("/skybox_vertex.vs"));
        skybox.createFragmentShader(ResourceUtils.loadResource("/skybox_fragment.fs"));
        skybox.link();

        skybox.createUniform("projection");
        skybox.createUniform("models");
        skybox.createUniform("sampler");
        skybox.createUniform("ambient");
    }

    private void initializeGameShader() throws InitializationException, ResourceException {
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

    private void initializeStatusShader() throws InitializationException, ResourceException {
        statusShader = new Shaders();
        statusShader.createVertexShader(ResourceUtils.loadResource("/status_vertex.vs"));
        statusShader.createFragmentShader(ResourceUtils.loadResource("/status_fragment.fs"));
        statusShader.link();

        statusShader.createUniform("matrix");
        statusShader.createUniform("color");
        statusShader.createUniform("hasTexture");
    }

    public void render(Window window, Camera camera, Level level, Status status) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight()); // TODO try to move this to window class
            window.setResized(false);
        }

        transformation.updateProjectionWithPerspective(FIELD_OF_VIEW, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        transformation.updateCameraView(camera);

        renderScene(window, camera, level);
        renderSkybox(window, camera, level);
        renderStatus(window, status);
    }

    private void renderScene(Window window, Camera camera, Level level) {
        shaders.bind();

        Matrix4f projection = transformation.getProjection();
        shaders.setUniform("projection", projection);
        shaders.setUniform("sampler", 0);

        Matrix4f view = transformation.getView();

        Lights sceneLights = level.getLights();
        renderLights(view, sceneLights);

        Map<Mesh, List<GameItem>> meshes = level.getMeshes();
        for (Mesh mesh : meshes.keySet()) {
            shaders.setUniform("material", mesh.getMaterial());
            mesh.renderList(meshes.get(mesh), this, view);
        }

        shaders.unbind();
    }

    public void renderGameItemFromMesh(GameItem gameItem, Matrix4f view) {
        Matrix4f models = transformation.getModelView(gameItem, view);
        shaders.setUniform("models", models);
    }

    private void renderSkybox(Window window, Camera camera, Level level) {
        skybox.bind();

        Matrix4f projection = transformation.getProjection();
        skybox.setUniform("projection", projection);
        skybox.setUniform("sampler", 0);

        Matrix4f view = transformation.getView();
        view.m30(0);
        view.m31(0);
        view.m32(0);

        Skybox sceneSkybox = level.getSkybox();
        Matrix4f models = transformation.getModelView(sceneSkybox, view);
        skybox.setUniform("models", models);
        skybox.setUniform("ambient", level.getLights().getAmbient());

        sceneSkybox.getMesh().render();

        skybox.unbind();
    }

    private void renderStatus(Window window, Status status) {
        status.render(window);

        statusShader.bind();

        Matrix4f projectionMatrix = transformation.getStatusProjectionMatrix(0, window.getWidth(), window.getHeight(), 0);

        for (TextItem gameItem : status.getGameItems()) {
            Mesh mesh = gameItem.getMesh();
            Matrix4f matrix = transformation.getStatusMatrix(gameItem);

            statusShader.setUniform("matrix", matrix);
            statusShader.setUniform("color", mesh.getMaterial().getAmbient());
            statusShader.setUniform("hasTexture", mesh.getMaterial().hasTexture() ? 1 : 0);

            mesh.render();
        }

        statusShader.unbind();
    }

    private void renderLights(Matrix4f view, Lights lights) {
        shaders.setUniform("ambient", lights.getAmbient());
        shaders.setUniform("specular", specular);

        renderPointLights(view, lights.getPointLights());
        renderSpotLights(view, lights.getSpotLights());
        renderDirectionalLight(view, lights.getDirectionalLight());
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
        if (skybox != null) {
            skybox.free();
        }
        if (shaders != null) {
            shaders.free();
        }
        if (statusShader != null) {
            statusShader.free();
        }
    }
}
