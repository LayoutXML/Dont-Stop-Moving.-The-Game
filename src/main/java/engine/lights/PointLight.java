package engine.lights;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class PointLight {
    private Vector3f color;
    private Vector3f position;
    private float intensity;
    private Attenuation attenuation = new Attenuation(1, 0, 0);

    public PointLight(PointLight pointLight) {
        color = new Vector3f(pointLight.getColor());
        position = new Vector3f(pointLight.getPosition());
        intensity = pointLight.getIntensity();
        attenuation = pointLight.getAttenuation();
    }
}
