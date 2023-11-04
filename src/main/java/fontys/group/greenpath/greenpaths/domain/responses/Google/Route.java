package fontys.group.greenpath.greenpaths.domain.responses.Google;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Route {
//    private List<RouteLabel> routeLabels;
//    private List<RouteLeg> legs;
    private int distanceMeters;
    private String duration;
//    private String staticDuration;
    private Polyline polyline;
//    private String description;
//    private List<String> warnings;
//    private Viewport viewport;
//    private RouteTravelAdvisory travelAdvisory;
//    private String routeToken;
}
