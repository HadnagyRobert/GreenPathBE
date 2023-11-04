package fontys.group.greenpath.greenpaths.business.impl;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.LatLng;
import fontys.group.greenpath.greenpaths.domain.RivmSensor;
import fontys.group.greenpath.greenpaths.domain.SensorData;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class FetchRivmDataUseCaseImpl {

//    implements DataFetcher, AirQuality

    private final String RIVM_API_URL = "https://api-samenmeten.rivm.nl/v1.0/Things?$filter=properties/codegemeente+eq+'772'";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

//    @Override
    @Cacheable(value = "RivmApi")
    @Async
    public CompletableFuture<List<SensorData>> fetchSensorApiData() {
        return CompletableFuture.supplyAsync(()->{
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            String response = restTemplate.getForObject(RIVM_API_URL, String.class);
            List<RivmSensor> rivmSensors = getAllSensorIds(response);
            try{
                getSensorDataStreamIds(rivmSensors);
                CompletableFuture<Void> futureSensorsWithLocations = getSensorsLocations(rivmSensors);
                CompletableFuture<Void> futureSensorsWithValues = getSensorValue(rivmSensors);

                CompletableFuture.allOf(futureSensorsWithLocations, futureSensorsWithValues).join();


            } catch (Exception e){
                return convertRivmSensorsToSensorData(rivmSensors);
            }

            stopWatch.stop();
            System.out.println("Rivm data fetched in: " + stopWatch.getTotalTimeSeconds() + " seconds");

            return convertRivmSensorsToSensorData(rivmSensors);
        });
    }

    private List<SensorData> convertRivmSensorsToSensorData(List<RivmSensor> rivmSensors) {
        List<SensorData> sensorDataList = new ArrayList<>();

        for (RivmSensor sensor : rivmSensors) {
            sensorDataList.add(SensorData.builder()
                    .location(sensor.getLocation())
                    .value(sensor.getValue())
                    .build());
        }

        return sensorDataList;
    }

    private List<RivmSensor> getAllSensorIds(String response) {
        List<RivmSensor> sensorsWithIds = new ArrayList<>();

        for (Integer thingId : readIdsFromJsonResponse(response)){
            sensorsWithIds.add(RivmSensor.builder()
                    .thingId(thingId)
                    .build());
        }

        return sensorsWithIds;
    }

    private List<RivmSensor> getSensorDataStreamIds(List<RivmSensor> rivmSensors) {
        String baseUrl = "https://api-samenmeten.rivm.nl/v1.0/Things(%s)/Datastreams?$filter=contains(name,'pm25_kal')";

        for (RivmSensor sensor : rivmSensors) {
            String url = String.format(baseUrl, sensor.getThingId());

            String response = restTemplate.getForObject(url, String.class);
            List<Integer> dataStreamId = readIdsFromJsonResponse(response);

            if (dataStreamId.size() > 0)
                sensor.setDataStreamId(dataStreamId.get(0));
        }

        return rivmSensors; //sensors with datastream id
    }

    @Async
    protected CompletableFuture<Void> getSensorsLocations(List<RivmSensor> rivmSensors)
    {
        //gets the sensors with location
        String baseUrl = "https://api-samenmeten.rivm.nl/v1.0/Things(%s)/Locations";

        for (RivmSensor sensor : rivmSensors) {
            String url = String.format(baseUrl, sensor.getThingId());

            String response = restTemplate.getForObject(url, String.class);
            try {
                JsonNode rootNode = objectMapper.readTree(response).get("value").get(0);
                JsonNode locationNode = rootNode.get("location");
                JsonNode coordinatesNode = locationNode.get("coordinates");

                if (coordinatesNode.isArray()) {
                    LatLng latLng = new LatLng(coordinatesNode.get(1).asDouble(), coordinatesNode.get(0).asDouble());
                    sensor.setLocation(latLng);
                }

            } catch (JacksonException e) {
                e.printStackTrace();
            }
        }

//        return rivmSensors; //sensors with location

        return CompletableFuture.completedFuture(null);
    }

    @Async
    protected CompletableFuture<Void> getSensorValue(List<RivmSensor> rivmSensors){
        //gets the sensors with value
        String baseUrl = "https://api-samenmeten.rivm.nl/v1.0/Datastreams(%s)/Observations?$top=1&$orderby=phenomenonTime+desc";

        for (RivmSensor sensor : rivmSensors) {
            String url = String.format(baseUrl, sensor.getDataStreamId());

            String response = restTemplate.getForObject(url, String.class);
            try {
                JsonNode rootNode = objectMapper.readTree(response);
                if (rootNode != null && rootNode.has("value")) {
                    JsonNode valueNode = rootNode.get("value");
                    if (valueNode.isArray() && valueNode.size() > 0) {
                        JsonNode firstValueNode = valueNode.get(0);
                        JsonNode resultNode = firstValueNode.get("result");
                        if (resultNode.isDouble())
                            sensor.setValue(resultNode.asDouble());
                    }
                }

            } catch (JacksonException e) {
                e.printStackTrace();
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    private List<Integer> readIdsFromJsonResponse(String response){
        List<Integer> ids = new ArrayList<>();

        try{
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode valuesNode = rootNode.get("value");

            if (valuesNode.isArray()) {
                List<Map<String, Object>> values = objectMapper.convertValue(valuesNode, new TypeReference<List<Map<String, Object>>>() {});
                for (Map<String, Object> map : values) {
                    if (map.containsKey("@iot.id")) {
                        ids.add((Integer) map.get("@iot.id"));
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ids;
    }
}
