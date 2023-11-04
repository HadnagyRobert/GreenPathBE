package fontys.group.greenpath.greenpaths.business.impl;

import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import fontys.group.greenpath.greenpaths.business.ComputeRouteUC;
import fontys.group.greenpath.greenpaths.business.GetRouteStatisticsUseCase;
import fontys.group.greenpath.greenpaths.business.SensorDataSelectorUC;
import fontys.group.greenpath.greenpaths.domain.RouteStatistics;
import fontys.group.greenpath.greenpaths.domain.SensorType;
import fontys.group.greenpath.greenpaths.domain.requests.Google.RouteRequest;
import fontys.group.greenpath.greenpaths.domain.responses.ComputeRouteResponse;
import fontys.group.greenpath.greenpaths.domain.responses.GetRoutesResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetRoutesUseCaseImplTest {

    @Mock
    SensorDataSelectorUC sensorDataSelectorUC;
    @Mock
    GetRouteStatisticsUseCase getRouteStatisticsUseCase;

    @InjectMocks
    GetRoutesUseCaseImpl getRoutesUseCase;

    @Test
    public void getRoutesTest() {
        // Prepare data
        RouteRequest request = RouteRequest.builder()
                .travelMode(TravelMode.DRIVING)
                .origin(new LatLng(0, 0))
                .destination(new LatLng(1, 1))
                .languageCode("en")
                .sensorType(SensorType.AIR)
                .build();

        ComputeRouteUC computeRouteUC = mock(ComputeRouteUC.class);

        DirectionsResult directionsResult = new DirectionsResult();
        DirectionsResult[] directionsResults = new DirectionsResult[1];
        directionsResults[0] = directionsResult;

        ComputeRouteResponse computeRouteResponse = ComputeRouteResponse.builder()
                .routes(directionsResults)
                .wereThereEnoughSensors(true)
                .build();

        when(sensorDataSelectorUC.instantiateRouteComputation(any(RouteRequest.class))).thenReturn(computeRouteUC);
        when(computeRouteUC.computeRoute()).thenReturn(computeRouteResponse);

        RouteStatistics routeStatistics = RouteStatistics.builder()
                .distance(1)
                .duration(1)
                .expectedCO2EmissionByCar(1)
                .build();
        when(getRouteStatisticsUseCase.getRouteStatistics(any(DirectionsResult.class))).thenReturn(routeStatistics);

        // Execute
        GetRoutesResponse result = getRoutesUseCase.getRoutes(request);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getRoutes().length);
        verify(sensorDataSelectorUC, times(1)).instantiateRouteComputation(any(RouteRequest.class));
        verify(computeRouteUC, times(1)).computeRoute();
        verify(getRouteStatisticsUseCase, times(1)).getRouteStatistics(any(DirectionsResult.class));
    }
}