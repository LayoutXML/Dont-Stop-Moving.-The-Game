package engine.lights;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Attenuation {
    private float constant;
    private float linear;
    private float exponent;
}
