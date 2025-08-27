package org.ilyutsik.service;


import lombok.RequiredArgsConstructor;
import org.ilyutsik.dto.LocationDto;
import org.ilyutsik.dto.WeatherDto;
import org.ilyutsik.exception.LocationNotFoundException;
import org.ilyutsik.exception.WeatherNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WeatherApiService {

    private final RestTemplate restTemplate;

    private final int limit = 5;

    @Value("${app.weatherApi}")
    private String apiKey;

    public List<WeatherDto> getWeatherByCity(String city) {
        URLEncoder.encode(city, StandardCharsets.UTF_8);
        List<LocationDto> coordinates = getLocationByCity(city);
        List<WeatherDto> weatherDtoList = new ArrayList<>();
        for (LocationDto coordinate : coordinates) {
            WeatherDto dto = getWeatherByLocation(coordinate.getName(), coordinate.getLatitude(), coordinate.getLongitude());
            dto.setName(coordinate.getName());
            weatherDtoList.add(dto);
        }
        if (weatherDtoList.isEmpty()) {
            throw new WeatherNotFoundException(city);
        }
        return weatherDtoList;
    }

    public WeatherDto getWeatherByLocation(String city, BigDecimal lat, BigDecimal lon) {
        URLEncoder.encode(city, StandardCharsets.UTF_8);
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

        try {
            WeatherDto dto = restTemplate.getForObject(uri, WeatherDto.class);
            if (dto == null) {
                throw new WeatherNotFoundException(city);
            }
            dto.setName(city);
            return dto;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Client error from OpenWeather API: " + e.getStatusCode(), e);
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("OpenWeather API server error", e);
        }

    }

    private List<LocationDto> getLocationByCity(String city) {
        URLEncoder.encode(city, StandardCharsets.UTF_8);
        URI uri = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("api.openweathermap.org")
                .path("/geo/1.0/direct")
                .queryParam("q", city)
                .queryParam("limit", limit)
                .queryParam("appid", apiKey)
                .build()
                .toUri();

        try {
            LocationDto[] locationDto = restTemplate.getForObject(uri, LocationDto[].class);
            if (locationDto.length == 0) {
                throw new LocationNotFoundException(city);
            }
            return Arrays.asList(locationDto);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Client error from OpenWeather API: " + e.getStatusCode(), e);
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("OpenWeather API server error", e);
        }
    }

}
