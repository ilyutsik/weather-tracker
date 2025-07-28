package org.ilyutsik.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WeatherDto {

        private Coord coord;
        private Main main;
        private Sys sys;
        private String name;
        private List<Weather> weather;

        @Data
        public static class Coord {
            private BigDecimal lat;
            private BigDecimal lon;
        }
        @Data
        public static class Main {
            private int temp;

            @JsonProperty("feels_like")
            private int feelsLike;

            private int humidity;
        }

        @Data
        public static class Sys {
            private String country;
        }

        @Data
        public static class Weather {
            private String description;
        }

}
