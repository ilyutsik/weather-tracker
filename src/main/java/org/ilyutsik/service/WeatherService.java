package org.ilyutsik.service;

import org.ilyutsik.dto.CoordinatesDto;
import org.ilyutsik.dto.WeatherDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WeatherService {

    private final int limit = 5;
    private final String apiKey = "bb2d9279fad042bf1fc183dcce6d45bd";

    private final RestTemplate restTemplate = new RestTemplate();

    private List<CoordinatesDto> getCoordinatesByName(String city) {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("api.openweathermap.org")
                .path("/geo/1.0/direct")
                .queryParam("q", city)
                .queryParam("limit", limit)
                .queryParam("appid", apiKey)
                .build()
                .toUri();

        CoordinatesDto[] coordinatesDto = restTemplate.getForObject(uri, CoordinatesDto[].class);
        return Arrays.asList(coordinatesDto);
    }

    public List<WeatherDto> getWeather(String city) {
        List<CoordinatesDto> coordinates = getCoordinatesByName(city);
        List<WeatherDto> weatherDto = new ArrayList<>();
        for (int i = 0; i < coordinates.size(); i++) {
            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("api.openweathermap.org")
                    .path("/data/2.5/weather")
                    .queryParam("lat", coordinates.get(i).getLatitude())
                    .queryParam("lon", coordinates.get(i).getLongitude())
                    .queryParam("limit", limit)
                    .queryParam("units", "metric")
                    .queryParam("appid", apiKey)
                    .build()
                    .toUri();
            WeatherDto dto = restTemplate.getForObject(uri, WeatherDto.class);
            weatherDto.add(dto);
        }
        return weatherDto;
    }

    public WeatherDto getWeatherByCoordinates(BigDecimal lat, BigDecimal lon) {
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("api.openweathermap.org")
                .path("/data/2.5/weather")
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("limit", limit)
                .queryParam("units", "metric")
                .queryParam("appid", apiKey)
                .build()
                .toUri();
        return restTemplate.getForObject(uri, WeatherDto.class);
    }

}
