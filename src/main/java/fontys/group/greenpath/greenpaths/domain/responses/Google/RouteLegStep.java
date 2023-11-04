package fontys.group.greenpath.greenpaths.domain.responses.Google;

import fontys.group.greenpath.greenpaths.domain.requests.Google.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteLegStep {
    private int distanceMeters;
    private String staticDuration;
    private Polyline polyline;
    private Location startLocation;
    private Location endLocation;
    private NavigationInstruction navigationInstruction;
}