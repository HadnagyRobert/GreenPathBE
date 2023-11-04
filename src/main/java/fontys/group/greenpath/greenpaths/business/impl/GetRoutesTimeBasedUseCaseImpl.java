package fontys.group.greenpath.greenpaths.business.impl;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import fontys.group.greenpath.greenpaths.business.*;
import fontys.group.greenpath.greenpaths.domain.RouteResult;
import fontys.group.greenpath.greenpaths.domain.RouteStatistics;
import fontys.group.greenpath.greenpaths.domain.requests.Google.RouteRequest;
import fontys.group.greenpath.greenpaths.domain.requests.TimeBasedRouteRequest;
import fontys.group.greenpath.greenpaths.domain.responses.ComputeRouteResponse;
import fontys.group.greenpath.greenpaths.domain.responses.GetRoutesResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
@AllArgsConstructor
public class GetRoutesTimeBasedUseCaseImpl implements GetRoutesTimeBaseUseCase {
    private final SensorDataSelectorUC sensorDataSelectorUC;
    private final GetRouteStatisticsUseCase getRouteStatisticsUseCase;
    private final CircumferenceUtils circumferenceUtils;

    private final static double AVG_WALKING_SPEED_IN_METERS = 5 /3.6;

    @Override
    public GetRoutesResponse getRoutes(TimeBasedRouteRequest request) {
        ComputeRouteResponse routeResponse = generateRoutes(RouteRequest.builder()
                .origin(request.getOrigin())
                .destination(getFinalDestination(request))
                .languageCode(request.getLanguageCode())
                .travelMode(request.getTravelMode())
                .sensorType(request.getSensorType())
                .build());

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

    private LatLng getFinalDestination(TimeBasedRouteRequest request) {
        return circumferenceUtils.getCircumferencePointBasedOnWalkingSpeed(AVG_WALKING_SPEED_IN_METERS, request.getOrigin(), request.getPathTime());
    }
}
