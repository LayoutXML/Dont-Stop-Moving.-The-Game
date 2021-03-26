package view.lights;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
@AllArgsConstructor
public class DirectionalLight {
    private Vector3f color;
    private Vector3f direction;
    private float intensity;

    public DirectionalLight(DirectionalLight directionalLight) {
        color = new Vector3f(directionalLight.getColor());
        direction = new Vector3f(directionalLight.getDirection());
        intensity = directionalLight.getIntensity();
    }
}
