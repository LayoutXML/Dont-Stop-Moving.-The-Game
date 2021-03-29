package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
@AllArgsConstructor
public class ObjectFromFileWrapper {
    private Vector3f position;
    private Vector3f rotation;
}
