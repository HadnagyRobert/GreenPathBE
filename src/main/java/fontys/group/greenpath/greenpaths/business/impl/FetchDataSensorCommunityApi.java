package fontys.group.greenpath.greenpaths.business.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.maps.model.LatLng;
import fontys.group.greenpath.greenpaths.business.AirQuality;
import fontys.group.greenpath.greenpaths.business.DataFetcher;
import fontys.group.greenpath.greenpaths.domain.SensorData;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class FetchDataSensorCommunityApi implements DataFetcher, AirQuality {

    private final RestTemplate restTemplate;
    private final String url = "https://data.sensor.community/airrohr/v1/filter/country=NL&area=51.4416,5.4697,1&type=SDS011";
    @Override
    @Cacheable(value = "DataSensorCommunityApi")
    @Async
    public CompletableFuture<List<SensorData>> fetchSensorApiData() {
        return CompletableFuture.supplyAsync(()->{
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            List<SensorData> sensorDataList = new ArrayList<>();
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "MyApp/1 (you@gmail.com)");

            try {
                ResponseEntity<String> responseEntity = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        String.class
                );
                Gson gson = new Gson();
                JsonArray records = gson.fromJson(responseEntity.getBody(), JsonArray.class);

                List<Map<String, Object>> unFormattedData = new ArrayList<>();

                for (int i = 0; i < records.size(); i++){
                    unFormattedData.add(gson.fromJson(records.get(i).toString(), Map.class));
                }

                List<Map<String, Object>> filteredData = filterLatestTimestamps(unFormattedData);

                filteredData.forEach((item) -> {
                    Map<String, Object> locationDTO = (Map<String, Object>) item.get("location");
                    double latitude = Double.parseDouble(locationDTO.get("latitude").toString());
                    double longitude = Double.parseDouble(locationDTO.get("longitude").toString());
                    List<Map<String, Object>> value = (List<Map<String, Object>>) item.get("sensordatavalues");

                    LatLng location = new LatLng();
                    location.lat = latitude;
                    location.lng = longitude;

                    sensorDataList.add(
                            SensorData.builder()
                                    .location(location)
                                    .value(Double.parseDouble(value.get(1).get("value").toString()))
                                    .build());
                });

            } catch (Exception e) {
                System.out.println(e);
            }

            stopWatch.stop();
            System.out.println("SensorCommunityApi: " + stopWatch.getTotalTimeSeconds());

            return sensorDataList;
        });
    }

    public List<Map<String, Object>> filterLatestTimestamps(List<Map<String, Object>> data) {
        Map<String, Map<String, Object>> locationMap = new HashMap<>();

        data.forEach((sensor) -> {
            Map<String, Object> location = (Map<String, Object>) sensor.get("location");
            String timestamp = sensor.get("timestamp").toString();
            String locationKey = location.get("latitude") + "," + location.get("longitude");
            LocalDateTime timestampDate = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            if (!locationMap.containsKey(locationKey) ||
                    LocalDateTime.parse(locationMap.get(locationKey)
                            .get("timestamp").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            .isBefore(timestampDate)
            )
            {
                locationMap.put(locationKey, sensor);
            }
        });

        return new ArrayList<>(locationMap.values());
    }

}
