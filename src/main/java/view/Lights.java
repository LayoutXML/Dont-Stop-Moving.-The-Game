package view;

import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import view.lights.DirectionalLight;
import view.lights.PointLight;
import view.lights.SpotLight;

@Getter
@Setter
public class Lights {
    private Vector3f ambient;
    private PointLight[] pointLights;
    private SpotLight[] spotLights;
    private DirectionalLight directionalLight;
}
