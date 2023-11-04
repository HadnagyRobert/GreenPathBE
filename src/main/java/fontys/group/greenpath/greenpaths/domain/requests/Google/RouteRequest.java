package fontys.group.greenpath.greenpaths.domain.requests.Google;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import fontys.group.greenpath.greenpaths.domain.Path;
import fontys.group.greenpath.greenpaths.domain.SensorType;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
public class RouteRequest {
    @NonNull
    private TravelMode travelMode;
    @NotNull
    private LatLng origin;
    @NotNull
    private LatLng destination;
    @NotNull
    private String languageCode;
//    private Waypoint origin;
//    private Waypoint destination;
//    private Optional<Waypoint[]> intermediates;
//    private RouteTravelMode travelMode;
//    private Optional<PolylineQuality> polylineQuality;
//    private Optional<PolylineEncoding> polylineEncoding;
//    private Optional<String> departureTime;
    private boolean computeAlternativeRoutes;
//    private String languageCode;
//    private String regionCode;
    private List<LatLng> waypoints;
    @NotNull
    private SensorType sensorType;
}
