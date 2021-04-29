package view.graphics;

import lombok.*;
import org.joml.Vector4f;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Material {
    // Color: percentage based RGB (hex/255) + transparency
    private static final Vector4f DEFAULT_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    @Builder.Default
    private Vector4f ambient = DEFAULT_COLOR;
    @Builder.Default
    private Vector4f diffuse = DEFAULT_COLOR;
    @Builder.Default
    private Vector4f specular = DEFAULT_COLOR;
    @Builder.Default
    private float reflectance = 0;
    private Texture texture;

    public boolean hasTexture() {
        return texture != null;
    }
}
