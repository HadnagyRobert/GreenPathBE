package fontys.group.greenpath.greenpaths.business;

import fontys.group.greenpath.greenpaths.domain.requests.Google.RouteRequest;
import fontys.group.greenpath.greenpaths.domain.requests.TimeBasedRouteRequest;
import fontys.group.greenpath.greenpaths.domain.responses.GetRoutesResponse;

public interface GetRoutesTimeBaseUseCase {
    GetRoutesResponse getRoutes(TimeBasedRouteRequest request);
}
