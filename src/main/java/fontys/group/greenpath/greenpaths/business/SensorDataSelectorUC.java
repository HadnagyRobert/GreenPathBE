package fontys.group.greenpath.greenpaths.business;

import fontys.group.greenpath.greenpaths.domain.requests.Google.RouteRequest;

public interface SensorDataSelectorUC {
    ComputeRouteUC instantiateRouteComputation(RouteRequest request);
}
