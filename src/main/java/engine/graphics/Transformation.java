package engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {
    private final Matrix4f projection = new Matrix4f();
    private final Matrix4f world = new Matrix4f();

    public Matrix4f getProjectionWithPerspective(float fov, float width, float height, float zNear, float zFar) {
        return projection.setPerspective(fov, width / height, zNear, zFar);
    }

    public Matrix4f getWorldWithRotationAndScale(Vector3f offset, Vector3f rotation, float scale) {
        return world.identity().translation(offset)
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .rotateZ((float) Math.toRadians(rotation.z))
                .scale(scale);
    }
}
