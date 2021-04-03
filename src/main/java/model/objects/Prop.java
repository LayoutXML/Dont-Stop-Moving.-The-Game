package model.objects;

import lombok.Getter;
import lombok.Setter;
import model.ObjectType;
import model.exceptions.ResourceException;
import org.joml.Vector4f;
import view.GameItem;
import view.graphics.Material;

@Getter
@Setter
public class Prop extends GameItem {
    private float textureScale = 1f;
    private float size = 1;
    private boolean solid = true;
    private ObjectType objectType = ObjectType.PROP;

    private boolean dangerous = true;

    public Prop(String name) throws ResourceException {
        super("/props/" + name + ".obj", Material.builder()
                .reflectance(10f)
                .ambient(new Vector4f(0.1f, 0.2f, 0.3f, 1f))
                .diffuse(new Vector4f(0.1f, 0.2f, 0.3f, 1f))
                .specular(new Vector4f(0.1f, 0.2f, 0.3f, 1f))
                .build());
    }

    public Prop(GameItem gameItem) {
        super(gameItem.getMesh());
    }
}
