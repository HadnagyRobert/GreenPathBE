package fontys.group.greenpath.greenpaths.business.impl;

import com.google.gson.*;
import com.google.maps.model.LatLng;
import fontys.group.greenpath.greenpaths.business.AirQuality;
import fontys.group.greenpath.greenpaths.business.DataFetcher;
import fontys.group.greenpath.greenpaths.domain.SensorData;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class FetchDataEindhovenApi implements DataFetcher, AirQuality {
    private final RestTemplate restTemplate;
    private final String url = "https://data.eindhoven.nl/api/v2/catalog/datasets/real-time-fijnstof-monitoring/records?limit=100&offset=0&timezone=UTC";

    @Override
    @Cacheable(value = "EindhovenApi")
    @Async
    public CompletableFuture<List<SensorData>> fetchSensorApiData() {
        return CompletableFuture.supplyAsync(() -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            List<SensorData> sensorDataList = new ArrayList<>();
            try {
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
                JsonElement jelement = new JsonParser().parse(responseEntity.getBody());
                JsonObject jobject = jelement.getAsJsonObject();
                JsonArray records = jobject.getAsJsonArray("records");

                for (int i = 0; i < records.size(); i++) {
                    JsonObject recordObject = records.get(i).getAsJsonObject().get("record").getAsJsonObject();
                    if (recordObject != null) {
                        JsonObject fieldsObject = recordObject.get("fields").getAsJsonObject();
                        if (fieldsObject != null) {
                            JsonElement dataElement = fieldsObject.get("data");
                            if (dataElement != null) {
                                String sensorData = dataElement.getAsString();
                                JsonArray sensorDataArray = new JsonParser().parse(sensorData).getAsJsonArray();

                                // Convert the JsonArray to a List of JsonObjects
                                List<JsonObject> jsonValues = new ArrayList<>();
                                for (int j = 0; j < sensorDataArray.size(); j++) {
                                    jsonValues.add(sensorDataArray.get(j).getAsJsonObject());
                                }

                                // Sort the array based on the timestamp
                                jsonValues.sort((a, b) -> {
                                    String aTimestamp = a.get("Timestamp").getAsString();
                                    String bTimestamp = b.get("Timestamp").getAsString();
                                    return bTimestamp.compareTo(aTimestamp);
                                });

                                double latestPM25Value = 0;
                                if(jsonValues.size() > 0) {
                                    JsonObject firstJsonValue = jsonValues.get(0);
                                    if(firstJsonValue != null) {
                                        JsonElement entitiesElement = firstJsonValue.get("Entities");
                                        if(entitiesElement != null) {
                                            JsonArray entities = entitiesElement.getAsJsonArray();
                                            for (int k = 0; k < entities.size(); k++) {
                                                JsonObject entity = entities.get(k).getAsJsonObject();
                                                if (entity.get("Name").getAsString().equals("PM2.5")) {
                                                    latestPM25Value = entity.get("Value").getAsDouble();
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }

                                JsonElement geoPointElement = fieldsObject.get("geopoint");
                                if (geoPointElement != null && geoPointElement.isJsonObject()) {
                                    JsonObject geoPointObject = geoPointElement.getAsJsonObject();
                                    if (geoPointObject != null) {
                                        JsonElement latElement = geoPointObject.get("lat");
                                        JsonElement lonElement = geoPointObject.get("lon");
                                        if (latElement != null && lonElement != null) {
                                            LatLng location = new LatLng();
                                            location.lat = latElement.getAsDouble();
                                            location.lng = lonElement.getAsDouble();

                                            SensorData sensorDataObject = SensorData.builder()
                                                    .location(location)
                                                    .value(latestPM25Value)
                                                    .build();

                                            if (sensorDataObject != null) {
                                                sensorDataList.add(sensorDataObject);
                                            }
                                        }
                                    } else {
                                        // handle case where geoPointObject is null
                                        System.out.println("Error: geopoint object is null for record " + i);
                                        // or set default value, etc.
                                    }
                                } else {
                                    // handle case where geoPointElement is null
                                    System.out.println("Error: geopoint element is null for record " + i);
                                    // or set default value, etc.
                                }

                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

            stopWatch.stop();
            System.out.println("Eindhoven API call took " + stopWatch.getTotalTimeMillis() + " ms");

            return sensorDataList;
        });
    }
}
