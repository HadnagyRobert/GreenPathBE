package fontys.group.greenpath.greenpaths.domain.requests.Google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Waypoint {
    private boolean via;
    private boolean vehicleStopover;
    private boolean sideOfRoad;
    private Location location;
    private String placeId;
    private String address;

    // Getters, setters, and constructors omitted for brevity
}
