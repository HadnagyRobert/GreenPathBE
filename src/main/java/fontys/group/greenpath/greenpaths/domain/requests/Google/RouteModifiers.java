package fontys.group.greenpath.greenpaths.domain.requests.Google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteModifiers {
    private boolean avoidTolls;
    private boolean avoidHighways;
    private boolean avoidFerries;
    private boolean avoidIndoor;
    private VehicleInfo vehicleInfo;
    private TollPass tollPasses;
}
