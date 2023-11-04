package fontys.group.greenpath.greenpaths.domain.responses.Google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {
    private List<Route> routes;
//    private FallbackInfo fallbackInfo;
//    private GeocodingResults geocodingResults;
}
