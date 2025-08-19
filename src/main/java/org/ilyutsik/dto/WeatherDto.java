package org.ilyutsik.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WeatherDto {

        private Coord coord;
        private List<Weather> weather;
        private String base;
        private Main main;
        private int visibility;
        private Wind wind;
        private Rain rain;
        private Clouds clouds;
        private int dt;
        private Sys sys;
        private int timezone;
        private int id;
        private String name;
        private int cod;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Coord {
            private BigDecimal lat;
            private BigDecimal lon;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Weather {
            private int id;
            private String main;
            private String description;

            private String icon;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Main {
            private double temp;

            @JsonProperty("feels_like")
            private double feelsLike;

            @JsonProperty("temp_min")
            private double tempMin;

            @JsonProperty("temp_max")
            private double tempMax;

            private int pressure;

            private int humidity;

            @JsonProperty("sea_level")
            private Integer seaLevel;

            @JsonProperty("grnd_level")
            private Integer grndLevel;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Wind {
            private double speed;
            private int deg;
            private Double gust;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Rain {
            @JsonProperty("1h")
            private double h;

        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Clouds {
            private int all;

        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Sys {
            private int type;
            private int id;
            private String country;
            private int sunrise;
            private int sunset;
        }

}
