package fontys.group.greenpath.greenpaths.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import fontys.group.greenpath.greenpaths.domain.SensorData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DataFetcher {
    public CompletableFuture<List<SensorData>> fetchSensorApiData() throws JsonProcessingException;
}
