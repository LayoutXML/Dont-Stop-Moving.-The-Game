package view.graphics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joml.Vector4f;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    private static final Vector4f DEFAULT_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    private Vector4f ambient = DEFAULT_COLOR;
    private Vector4f diffuse = DEFAULT_COLOR;
    private Vector4f specular = DEFAULT_COLOR;
    private float reflectance = 0;
    private Texture texture;

    public boolean hasTexture() {
        return texture != null;
    }
}
