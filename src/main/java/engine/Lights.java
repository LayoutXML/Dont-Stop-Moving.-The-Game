package engine;

import engine.lights.DirectionalLight;
import engine.lights.PointLight;
import engine.lights.SpotLight;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public class Lights {
    private Vector3f ambient;
    private PointLight[] pointLights;
    private SpotLight[] spotLights;
    private DirectionalLight directionalLight;
}
