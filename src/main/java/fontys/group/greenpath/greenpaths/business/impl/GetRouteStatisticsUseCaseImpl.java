package fontys.group.greenpath.greenpaths.business.impl;

import com.google.maps.model.DirectionsResult;
import fontys.group.greenpath.greenpaths.business.GetRouteStatisticsUseCase;
import fontys.group.greenpath.greenpaths.domain.RouteStatistics;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
public class GetRouteStatisticsUseCaseImpl implements GetRouteStatisticsUseCase {
    @Override
    public RouteStatistics getRouteStatistics(DirectionsResult route) {
        long distance = getRouteDistance(route);

        return RouteStatistics.builder()
                .distance(distance)
                .duration(getRouteDuration(route))
                .expectedCO2EmissionByCar(getRouteCO2Emission(distance))
                .build();
    }

    private long getRouteDistance(DirectionsResult route) {
        return Arrays.stream(route.routes[0].legs).map(leg -> leg.distance.inMeters).reduce(0L, Long::sum);
    }

    private double getRouteDuration(DirectionsResult route) {
        return Arrays.stream(route.routes[0].legs).map(leg -> leg.duration.inSeconds).reduce(0L, Long::sum);
    }

    private double getRouteCO2Emission(long distance) {
        double fuelEfficiency = 15.5;
        double emissionFactor = 2.3; //kg
        double distanceInKm = (double) distance / 1000;
        return (distanceInKm / fuelEfficiency) * emissionFactor; // amount of co3 predicted in kg
    }
}
