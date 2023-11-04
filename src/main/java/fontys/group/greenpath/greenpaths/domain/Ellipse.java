package fontys.group.greenpath.greenpaths.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ellipse {
    private double centerX;
    private double centerY;
    private double semiMajorAxis;
    private double semiMinorAxis;
    private double rotationAngle;
}
