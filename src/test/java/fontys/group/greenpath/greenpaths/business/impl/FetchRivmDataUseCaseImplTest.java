package fontys.group.greenpath.greenpaths.business.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class FetchRivmDataUseCaseImplTest {
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();
    FetchRivmDataUseCaseImpl fetchRivmDataUseCase = new FetchRivmDataUseCaseImpl(restTemplate, objectMapper);

    @Test
    void fetchSensorApiData() {
        fetchRivmDataUseCase.fetchSensorApiData();
    }
}