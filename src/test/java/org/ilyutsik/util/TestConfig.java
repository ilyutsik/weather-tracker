package org.ilyutsik.util;

import org.ilyutsik.service.WeatherApiService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@TestConfiguration
public class TestConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WeatherApiService weatherService(RestTemplate restTemplate) {
        return new WeatherApiService(restTemplate);
    }
}
