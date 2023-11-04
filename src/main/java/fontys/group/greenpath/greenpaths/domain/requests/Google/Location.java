package fontys.group.greenpath.greenpaths.domain.requests.Google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private LatLng latLng;
    private int heading;

    // Getters, setters, and constructors omitted for brevity
}
