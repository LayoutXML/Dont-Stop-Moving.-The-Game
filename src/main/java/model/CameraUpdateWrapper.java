package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joml.Vector3f;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CameraUpdateWrapper {
    @Builder.Default
    private final Vector3f positionDelta = new Vector3f();
    @Builder.Default
    private final boolean win = false;
    @Builder.Default
    private final boolean positionReset = false;
}
