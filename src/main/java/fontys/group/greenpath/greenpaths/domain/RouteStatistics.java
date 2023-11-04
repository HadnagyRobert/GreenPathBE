package fontys.group.greenpath.greenpaths.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteStatistics {
    private double distance;
    private double duration;
    private double expectedCO2EmissionByCar;
}
