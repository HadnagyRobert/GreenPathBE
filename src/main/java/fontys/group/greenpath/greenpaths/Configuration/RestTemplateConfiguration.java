package fontys.group.greenpath.greenpaths.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(5000);  // 5 seconds
        httpRequestFactory.setConnectTimeout(5000);           // 5 seconds
        httpRequestFactory.setReadTimeout(300000);             // 30 seconds
        return new RestTemplate(httpRequestFactory);
    }
}
