package fontys.group.greenpath.greenpaths.domain.requests;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import fontys.group.greenpath.greenpaths.domain.SensorType;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class TimeBasedRouteRequest {
    @NotNull
    private SensorType sensorType;
    @NonNull
    private TravelMode travelMode;
    @NotNull
    private LatLng origin;
    @NotNull
    private String languageCode;
    @NotNull
    private int pathTime;
}
