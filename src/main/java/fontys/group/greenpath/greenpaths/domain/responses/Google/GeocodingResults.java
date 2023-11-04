package fontys.group.greenpath.greenpaths.domain.responses.Google;

import com.google.maps.model.GeocodedWaypoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeocodingResults {
    private GeocodedWaypoint origin;
    private GeocodedWaypoint destination;
    private List<GeocodedWaypoint> intermediates;
}