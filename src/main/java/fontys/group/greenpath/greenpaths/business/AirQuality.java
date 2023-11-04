package fontys.group.greenpath.greenpaths.business;

import fontys.group.greenpath.greenpaths.domain.SensorData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AirQuality {
    CompletableFuture<List<SensorData>> fetchSensorApiData();
}
