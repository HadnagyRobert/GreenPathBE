package fontys.group.greenpath.greenpaths.business;

import com.google.maps.model.DirectionsResult;
import fontys.group.greenpath.greenpaths.domain.RouteStatistics;

public interface GetRouteStatisticsUseCase {
    RouteStatistics getRouteStatistics(DirectionsResult route);
}
