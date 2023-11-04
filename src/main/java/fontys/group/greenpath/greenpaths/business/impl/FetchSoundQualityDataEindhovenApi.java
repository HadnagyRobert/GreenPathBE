package fontys.group.greenpath.greenpaths.business.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.LatLng;
import fontys.group.greenpath.greenpaths.business.DataFetcher;
import fontys.group.greenpath.greenpaths.business.SoundQuality;
import fontys.group.greenpath.greenpaths.domain.SensorData;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FetchSoundQualityDataEindhovenApi implements DataFetcher, SoundQuality {
    private final String base_url = "https://data.eindhoven.nl/api/explore/v2.0/catalog/datasets/locaties-geluidssensoren/records?limit=10&offset=0&timezone=UTC";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
//    @Override
    @Cacheable(value = "EindhovenApiSoundQuality")
    @Async
    public CompletableFuture<List<SensorData>> fetchSensorApiData() {
        return CompletableFuture.supplyAsync(() -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            System.out.println("Fetching data from Eindhoven sound quality API");
            String response = restTemplate.getForObject(base_url, String.class);
            HashMap<Integer, LatLng> sensorIdsWithLocation = getSensorIdsAndLocation(response);

            stopWatch.stop();
            System.out.println("Time taken to fetch data from Eindhoven sound quality API: " + stopWatch.getTotalTimeSeconds() + " seconds");

            return getSensorsValuesMappedToSensorData(sensorIdsWithLocation);
        });
    }

    private List<SensorData> getSensorsValuesMappedToSensorData(HashMap<Integer, LatLng> sensorIds)
    {
        String base_url = "https://opendata.munisense.net/api/v2/eindhoven2-geluid/soundmeasurementpoints/%s/lceq/query/presets/last_day";

        List<SensorData> sensorDataList = new ArrayList<>();
        for (Integer sensorId : sensorIds.keySet()) {
            try {
                String url = String.format(base_url, sensorId);
                String response = restTemplate.getForObject(url, String.class);

                JsonNode jsonNode = objectMapper.readTree(response);
                if (jsonNode != null) {
                    JsonNode results = jsonNode.get("results");
                    if (results != null) {
                        if (results.isArray()) {
                            ZonedDateTime latestTimeStamp = getLatestTimeStamp(results);

                            for (JsonNode result : results) {
                                String current = result.get("timestamp").asText();
                                ZonedDateTime currentZDT = ZonedDateTime.parse(current, DateTimeFormatter.ISO_DATE_TIME);

                                if (currentZDT.isEqual(latestTimeStamp)) {
                                    Double value = result.get("avg").asDouble();
                                    LatLng latLng = sensorIds.get(sensorId);
                                    sensorDataList.add(
                                            SensorData.builder()
                                                    .location(latLng)
                                                    .value(value)
                                                    .build()
                                    );
                                }
                            }
                        }
                    }
                }
            } catch (HttpClientErrorException e) {
                continue;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return sensorDataList;
    }

    private ZonedDateTime getLatestTimeStamp(JsonNode timestamps)
    {
        ZonedDateTime mostRecent = null;
        for (JsonNode timestamp : timestamps) {
            String current = timestamp.get("timestamp").asText();
            ZonedDateTime currentZDT = ZonedDateTime.parse(current, DateTimeFormatter.ISO_DATE_TIME);

            if (mostRecent == null || currentZDT.isAfter(mostRecent)) {
                mostRecent = currentZDT;
            }
        }
        return mostRecent;
    }

    private HashMap<Integer, LatLng>  getSensorIdsAndLocation(String response)
    {
        HashMap<Integer, LatLng> sensorIdsWithLocation = new HashMap<>();
        try{
            JsonNode jsonNode = objectMapper.readTree(response);
            if (jsonNode != null) {
                jsonNode = jsonNode.get("records");

                for (JsonNode node : jsonNode) {
                    JsonNode record = node.get("record");
                    if (record != null)
                    {
                        JsonNode fields = record.get("fields");
                        if (fields != null)
                        {
                            JsonNode sensorId = fields.get("object_id");
                            JsonNode location_latitude = fields.get("location_latitude");
                            JsonNode location_longitude = fields.get("location_longitude");

                            if (sensorId != null && location_latitude != null && location_longitude != null)
                            {
                                sensorIdsWithLocation.put(sensorId.asInt(), new LatLng(location_latitude.asDouble(), location_longitude.asDouble()));
                            }
                        }
                    }
                }

            }
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }

        return sensorIdsWithLocation;
    }
}
