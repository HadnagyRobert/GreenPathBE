package fontys.group.greenpath.greenpaths.business.impl;

import com.google.maps.model.DirectionsResult;
import fontys.group.greenpath.greenpaths.business.ComputeRouteUC;
import fontys.group.greenpath.greenpaths.business.GetRouteStatisticsUseCase;
import fontys.group.greenpath.greenpaths.business.GetRoutesUseCase;
import fontys.group.greenpath.greenpaths.business.SensorDataSelectorUC;
import fontys.group.greenpath.greenpaths.domain.RouteResult;
import fontys.group.greenpath.greenpaths.domain.RouteStatistics;
import fontys.group.greenpath.greenpaths.domain.requests.Google.RouteRequest;
import fontys.group.greenpath.greenpaths.domain.responses.ComputeRouteResponse;
import fontys.group.greenpath.greenpaths.domain.responses.GetRoutesResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GetRoutesUseCaseImpl implements GetRoutesUseCase {
    private SensorDataSelectorUC sensorDataSelectorUC;
    private GetRouteStatisticsUseCase getRouteStatisticsUseCase;

    public GetRoutesResponse getRoutes(RouteRequest request) {
        ComputeRouteResponse routeResponse = generateRoutes(request);

        int availableRoutes = Arrays.stream(routeResponse.getRoutes()).filter(Objects::nonNull).toArray().length;

        RouteResult[] routes = new RouteResult[availableRoutes];

        for (int i = 0; i < routes.length; i++) {
            if (routeResponse.getRoutes()[i] != null)
            {
                routes[i] = RouteResult.builder()
                        .path(routeResponse.getRoutes()[i])
                        .statistics(getRouteStatistics(routeResponse.getRoutes()[i]))
                        .build();
            }
        }


        return GetRoutesResponse.builder()
                .routes(routes)
                .wereThereEnoughSensors(routeResponse.isWereThereEnoughSensors())
                .build();
    }

    private ComputeRouteResponse generateRoutes(RouteRequest request) {
        ComputeRouteUC routesClient = sensorDataSelectorUC.instantiateRouteComputation(request);
        return routesClient.computeRoute();
    }

    private RouteStatistics getRouteStatistics(DirectionsResult request) {
        return getRouteStatisticsUseCase.getRouteStatistics(request);
    }
}
