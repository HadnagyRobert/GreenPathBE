package fontys.group.greenpath.greenpaths.business.impl;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import fontys.group.greenpath.greenpaths.domain.SensorDataInput;
import fontys.group.greenpath.greenpaths.business.EllipseUtils;
import fontys.group.greenpath.greenpaths.business.ComputeRouteUC;
import fontys.group.greenpath.greenpaths.domain.*;
import fontys.group.greenpath.greenpaths.domain.requests.Google.RouteRequest;
import fontys.group.greenpath.greenpaths.domain.responses.ComputeRouteResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.util.StopWatch;

import java.util.*;

@Builder
@AllArgsConstructor
public class ComputeRouteUCImpl implements ComputeRouteUC {
    private EllipseUtils ellipseUtils;
    private final SensorDataInput sensorDataInput;
    private final KNNFindNearstPointUseCaseImpl knnFindNearestPointUseCase;
    private final String apiKey;
    private Ellipse currentContextEllipse;
    private RouteRequest currentRequest;

    private final int K = 2;
    private final int SECTORS = 8;

    public ComputeRouteResponse computeRoute() {
        generateInitialEllipse(currentRequest.getOrigin(), currentRequest.getDestination());

        List<DirectionsResult> allGoogleGeneratedPath = generateGooglePathsWithPredefinedPoints(currentRequest);

        return getTopThreeRoutes(allGoogleGeneratedPath);
    }

    private HashMap<Integer, List<LatLng>> groupSensors(List<SensorData> sensors){
        if (sensors == null || sensors.size() == 0) {
            return null;
        }

        HashMap<Integer, List<LatLng>> groupedSensors = new HashMap<>();
        List<SensorData> healthyReadings = sensors.stream()
                .filter(sensor -> sensor.getValue() < sensorDataInput.getMinValue())
                .toList();
        List<SensorData> badReadings = sensors.stream()
                .filter(sensor -> sensor.getValue() > sensorDataInput.getMinValue())
                .toList();

        groupedSensors.put(0, healthyReadings.stream().map(SensorData::getLocation).toList());
        groupedSensors.put(1, badReadings.stream().map(SensorData::getLocation).toList());

        return groupedSensors;
    }

    private List<DirectionsResult> generateGooglePathsWithPredefinedPoints(RouteRequest request) {
        List<Path> paths = generateAllPossiblePaths(ellipseUtils.divideEllipse(currentContextEllipse, SECTORS));

        List<DirectionsResult> results = new ArrayList<>();

        for (Path path : paths) {
            request.setWaypoints(path.getPath());
            DirectionsResult result = generateGooglePath(request);

            results.add(result);
        }

        return results;
    }

    private DirectionsResult generateGooglePath(RouteRequest request) {

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        DirectionsResult result = null;
        try {
            result = DirectionsApi.newRequest(context)
                    .mode(request.getTravelMode())
                    .origin(request.getOrigin())
                    .waypoints(request.getWaypoints().toArray(new LatLng[0]))
                    .destination(request.getDestination())
                    .language(request.getLanguageCode())
//                    .alternatives(request.isComputeAlternativeRoutes())
                    .optimizeWaypoints(true)
                    .await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private ComputeRouteResponse getTopThreeRoutes(List<DirectionsResult> directionResults) {
        DirectionsResult[] finalTopThreeRoutes = new DirectionsResult[3];
        HashMap<Integer, List<LatLng>> allSensorsGrouped = groupSensors(getSensorsInsideEllipse());

        if (allSensorsGrouped == null || allSensorsGrouped.size() == 0) {
            finalTopThreeRoutes[0] = (generateGooglePath(currentRequest));

            return ComputeRouteResponse.builder()
                    .routes(finalTopThreeRoutes)
                    .wereThereEnoughSensors(false)
                    .build();
        }

        HashMap<DirectionsResult, Integer> goodResultsWithScores = new HashMap<>();
        HashMap<DirectionsResult, Integer> badResultsWithScores = new HashMap<>();
        for (DirectionsResult result : directionResults) {
            List<LatLng> path = result.routes[0].overviewPolyline.decodePath();
            for (LatLng point : path) {
                int classification = knnFindNearestPointUseCase.classifyPoint(allSensorsGrouped, point, K);
                if (classification == 0) {
                    goodResultsWithScores.put(result, goodResultsWithScores.getOrDefault(result, 0) + 1);
                }
                else {
                    badResultsWithScores.put(result, badResultsWithScores.getOrDefault(result, 0) + 1);
                }
            }
        }

        if (goodResultsWithScores.size() > 3) {
            List<Map.Entry<DirectionsResult, Integer>> sortedGoodResults = goodResultsWithScores.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .toList();

            for (int i = 0; i < Math.min(3, sortedGoodResults.size()); i++) {
                finalTopThreeRoutes[i] = sortedGoodResults.get(i).getKey();
            }
        }
        else if (badResultsWithScores.size() > 0) {
            List<Map.Entry<DirectionsResult, Integer>> sortedBadResults = badResultsWithScores.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .toList();

            for (int i = 0; i < Math.min(3, sortedBadResults.size()); i++) {
                finalTopThreeRoutes[i] = sortedBadResults.get(i).getKey();
            }
        }


        return ComputeRouteResponse.builder()
                .routes(finalTopThreeRoutes)
                .wereThereEnoughSensors(true)
                .build();
    }

    private List<Path> generateAllPossiblePaths(Graph graph) {
        List<Path> paths = new ArrayList<>();
        List<LatLng> startLayer = graph.getLayer(0);
        for (LatLng startVertex : startLayer) {
            Stack<LatLng> pathStack = new Stack<>();
            generatePathsUtil(graph, startVertex, 1, pathStack, paths);
        }
        return paths;
    }

    private void generatePathsUtil(Graph graph, LatLng currentVertex, int currentLayer,
                                          Stack<LatLng> pathStack, List<Path> paths) {
        pathStack.push(currentVertex);

        if (currentLayer == graph.getLayerCount()) {
            Path newPath = new Path();
            newPath.setPath(new ArrayList<>(pathStack));
            paths.add(newPath);
        } else {
            for (LatLng nextVertex : graph.getLayer(currentLayer)) {
                generatePathsUtil(graph, nextVertex, currentLayer + 1, pathStack, paths);
            }
        }

        pathStack.pop();
    }

    private List<SensorData> getSensorsInsideEllipse(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        HashSet<SensorData> availableNonRepeatedSensors = filterRepeatedSensors();
        List<SensorData> sensorsInEllipse = new ArrayList<>();
        for (SensorData sensor : availableNonRepeatedSensors) {
            if (ellipseUtils
                    .isPointInsideEllipse(
                            new LatLng(sensor.getLocation().lat, sensor.getLocation().lng),
                            currentContextEllipse
                    )
            ) {
                sensorsInEllipse.add(sensor);
            }
        }

        stopWatch.stop();
        System.out.println("Time elapsed in getSensorsInsideEllipse: " + stopWatch.getTotalTimeMillis());
        return sensorsInEllipse;
    }

    private HashSet<SensorData> filterRepeatedSensors() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        HashSet<SensorData> uniqueSensors = new HashSet<>();

        for (SensorData sensorData : sensorDataInput.getSensorData()) {
            if (!uniqueSensors.contains(sensorData)) {
                uniqueSensors.add(sensorData);
            }
        }
        stopWatch.stop();
        System.out.println("Time elapsed in filterRepeatedSensors: " + stopWatch.getTotalTimeMillis());
        return uniqueSensors;
    }

    private void generateInitialEllipse(LatLng origin, LatLng destination) {
        currentContextEllipse = ellipseUtils.calculateEllipseFromTwoPoints(origin, destination);
    }
}


//other ways to find the path
//generate the first path and get the good sensors and then compare the sensors to the points generated by google on the orignal path and
//if the sensor has a certain distance to the point then add it to the path