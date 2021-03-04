package engine.graphics;

import engine.Camera;
import model.GameItem;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    private final Matrix4f projection = new Matrix4f();
    private final Matrix4f modelView = new Matrix4f();
    private final Matrix4f view = new Matrix4f();
    private final Matrix4f status = new Matrix4f();

    public Matrix4f getProjectionWithPerspective(float fov, float width, float height, float zNear, float zFar) {
        return projection.setPerspective(fov, width / height, zNear, zFar);
    }

    public Matrix4f getModelView(GameItem gameItem, Matrix4f view) {
        Vector3f rotation = gameItem.getRotation();
        Vector3f position = gameItem.getPosition();
        float scale = gameItem.getScale();

        modelView.identity().translation(position)
                .rotateX((float) Math.toRadians(-rotation.x))
                .rotateY((float) Math.toRadians(-rotation.y))
                .rotateZ((float) Math.toRadians(-rotation.z))
                .scale(scale);

        Matrix4f viewCopy = new Matrix4f(view);
        return viewCopy.mul(modelView);
    }

    public Matrix4f getCameraView(Camera camera) {
        Vector3f position = camera.getPosition();
        Vector3f rotation = camera.getRotation();

        return view.identity()
                .rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
                .translate(-position.x, -position.y, -position.z);
    }

    public Matrix4f getStatusProjectionMatrix(float left, float right, float bottom, float top) {
        status.identity();
        status.setOrtho2D(left, right, bottom, top);
        return status;
    }

    public Matrix4f getStatusMatrix(GameItem gameItem, Matrix4f projection) {
        Vector3f rotation = gameItem.getRotation();
        Vector3f position = gameItem.getPosition();
        float scale = gameItem.getScale();
        Matrix4f model = new Matrix4f();

        model.identity().translate(position)
                .rotateX((float) Math.toRadians(-rotation.x))
                .rotateX((float) Math.toRadians(-rotation.y))
                .rotateX((float) Math.toRadians(-rotation.z))
                .scale(scale);

        Matrix4f currentStatusMatrix = new Matrix4f(projection);
        currentStatusMatrix.mul(model);

        return currentStatusMatrix;
    }
}
