package fontys.group.greenpath.greenpaths.domain.requests.Google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LatLng {
    private double latitude;
    private double longitude;

    // Getters, setters, and constructors omitted for brevity
}
