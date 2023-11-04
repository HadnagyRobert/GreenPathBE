package fontys.group.greenpath.greenpaths.business.impl;

import fontys.group.greenpath.greenpaths.business.*;
import fontys.group.greenpath.greenpaths.domain.SensorData;
import fontys.group.greenpath.greenpaths.domain.SensorDataInput;
import fontys.group.greenpath.greenpaths.domain.SensorType;
import fontys.group.greenpath.greenpaths.domain.requests.Google.RouteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorDataSelectorUCImpl implements SensorDataSelectorUC {
    private final List<AirQuality> airQualitySensorsImpl;
    private final List<SoundQuality> soundQualitySensorsImpl;
    private final KNNFindNearstPointUseCaseImpl knnFindNearestPointUseCase;
    private final EllipseUtils ellipseUtils;

    @Value("${googlemaps-api-key}")
    private String apiKey;


    public ComputeRouteUC instantiateRouteComputation(RouteRequest request) {
        try {
            if (request.getSensorType().equals(SensorType.AIR)) {
                SensorDataInput sensorDataInput = SensorDataInput.builder()
                        .sensorData(getSensorDataFromSelectedType(request.getSensorType()))
                        .sensorType(request.getSensorType())
                        .minValue(5)
                        .build();

                return ComputeRouteUCImpl.builder()
                        .sensorDataInput(sensorDataInput)
                        .ellipseUtils(ellipseUtils)
                        .knnFindNearestPointUseCase(knnFindNearestPointUseCase)
                        .apiKey(apiKey)
                        .currentRequest(request)
                        .build();

            } else if (request.getSensorType().equals(SensorType.SOUND)) {
                SensorDataInput sensorDataInput = SensorDataInput.builder()
                        .sensorData(getSensorDataFromSelectedType(request.getSensorType()))
                        .sensorType(request.getSensorType())
                        .minValue(50)
                        .build();

                return ComputeRouteUCImpl.builder()
                        .sensorDataInput(sensorDataInput)
                        .ellipseUtils(ellipseUtils)
                        .knnFindNearestPointUseCase(knnFindNearestPointUseCase)
                        .apiKey(apiKey)
                        .currentRequest(request)
                        .build();
            } else {
                throw new RuntimeException("No strategy found for " + request.getSensorType());
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private List<SensorData> getSensorDataFromSelectedType(SensorType sensorType) throws ExecutionException, InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<CompletableFuture<List<SensorData>>> futureList = new ArrayList<>();
        if (sensorType.equals(SensorType.AIR)) {
            for (AirQuality airQuality : airQualitySensorsImpl) {
                futureList.add(airQuality.fetchSensorApiData());
            }
        } else if (sensorType.equals(SensorType.SOUND)) {
            for (SoundQuality soundQuality : soundQualitySensorsImpl) {
                futureList.add(soundQuality.fetchSensorApiData());
            }
        } else {
            throw new RuntimeException("No strategy found for " + sensorType);
        }

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));

        CompletableFuture<List<SensorData>> allData = allFutures.thenApply(future ->
                futureList.stream()
                        .flatMap(futureData -> {
                            try {
                                return futureData.get().stream();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toList())
        );
        stopWatch.stop();
        System.out.println("Time taken to fetch data: " + stopWatch.getTotalTimeSeconds() + " seconds");

        return allData.get();
    }
}
