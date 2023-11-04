package fontys.group.greenpath.greenpaths.business.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class FetchSoundQualityDataEindhovenApiTest {
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();
    FetchSoundQualityDataEindhovenApi fetchSoundQualityDataEindhovenApi = new FetchSoundQualityDataEindhovenApi(restTemplate, objectMapper);
    @Test
    void fetchSensorApiData() {
        fetchSoundQualityDataEindhovenApi.fetchSensorApiData();
    }
}