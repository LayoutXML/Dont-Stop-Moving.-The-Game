package view.lights;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public class SpotLight {
    private PointLight pointLight;
    private Vector3f direction;
    private float cutoff;

    public SpotLight(SpotLight spotLight) {
        pointLight = new PointLight(spotLight.getPointLight());
        direction = new Vector3f(spotLight.getDirection());
        cutoff = spotLight.getCutoff();
    }
}
