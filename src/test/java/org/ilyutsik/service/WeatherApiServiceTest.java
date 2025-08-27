package org.ilyutsik.service;

import org.ilyutsik.util.TestConfig;
import org.ilyutsik.dto.WeatherDto;
import org.ilyutsik.exception.WeatherNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource(properties = "app.weatherApi=${WeatherApiKey}")
class WeatherApiServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WeatherApiService weatherApiService;

    private MockRestServiceServer mockServer;
    private static final String LOCATION_RESPONSE = """
           [
                                 {
                                     "name": "Province of Turin",
                                     "local_names": {
                                         "mr": "मिन्‍स्‍क",
                                         "kn": "ಮಿನ್ಸ್ಕ್",
                                         "fi": "Minsk",
                                         "cu": "Мѣньскъ",
                                         "ur": "منسک",
                                         "gl": "Minsk",
                                         "ja": "ミンスク",
                                         "ml": "മിൻസ്ക്",
                                         "sk": "Minsk",
                                         "ky": "Минск",
                                         "be": "Мінск",
                                         "vo": "Minsk",
                                         "it": "Minsk",
                                         "ug": "مىنىسكى",
                                         "pl": "Mińsk",
                                         "sr": "Минск",
                                         "et": "Minsk",
                                         "tg": "Минск",
                                         "ku": "Mînsk",
                                         "oc": "Minsk",
                                         "ta": "மின்ஸ்க்",
                                         "feature_name": "Minsk",
                                         "ru": "Минск",
                                         "bo": "མིན་སིཀ།",
                                         "bg": "Минск",
                                         "nl": "Minsk",
                                         "zh": "明斯克",
                                         "tt": "Минск",
                                         "mk": "Минск",
                                         "en": "Minsk",
                                         "fa": "مینسک",
                                         "sv": "Minsk",
                                         "is": "Minsk",
                                         "cv": "Минск",
                                         "th": "มินสก์",
                                         "ko": "민스크",
                                         "ka": "მინსკი",
                                         "io": "Minsk",
                                         "cs": "Minsk",
                                         "fr": "Minsk",
                                         "kk": "Минск",
                                         "kv": "Минск",
                                         "de": "Minsk",
                                         "ia": "Minsk",
                                         "no": "Minsk",
                                         "yi": "מינסק",
                                         "os": "Минск",
                                         "ar": "مينسك",
                                         "ga": "Minsc",
                                         "lv": "Minska",
                                         "ascii": "Minsk",
                                         "he": "מינסק",
                                         "sl": "Minsk",
                                         "la": "Minscum",
                                         "el": "Μινσκ",
                                         "lt": "Minskas",
                                         "hy": "Մինսկ",
                                         "uk": "Мінськ",
                                         "es": "Minsk",
                                         "hr": "Minsk",
                                         "eo": "Minsko",
                                         "hi": "मिन्‍स्‍क",
                                         "hu": "Minszk",
                                         "pt": "Minsk",
                                         "vi": "Minxcơ"
                                     },
                                     "lat": 45.133,
                                     "lon": 7.367,
                                     "country": "BY"
                                 }
                             ]
    """;

    private static final String WEATHER_RESPONSE = """
                {
                   "coord": {
                      "lon": 7.367,
                      "lat": 45.133
                   },
                   "weather": [
                      {
                         "id": 501,
                         "main": "Rain",
                         "description": "moderate rain",
                         "icon": "10d"
                      }
                   ],
                   "base": "stations",
                   "main": {
                      "temp": 284.2,
                      "feels_like": 282.93,
                      "temp_min": 283.06,
                      "temp_max": 286.82,
                      "pressure": 1021,
                      "humidity": 60,
                      "sea_level": 1021,
                      "grnd_level": 910
                   },
                   "visibility": 10000,
                   "wind": {
                      "speed": 4.09,
                      "deg": 121,
                      "gust": 3.47
                   },
                   "rain": {
                      "1h": 2.73
                   },
                   "clouds": {
                      "all": 83
                   },
                   "dt": 1726660758,
                   "sys": {
                      "type": 1,
                      "id": 6736,
                      "country": "IT",
                      "sunrise": 1726636384,
                      "sunset": 1726680975
                   },
                   "timezone": 7200,
                   "id": 3165523,
                   "name": "Province of Turin",
                   "cod": 200
                }      \s
                """;


    private final String apiKey = System.getenv("WeatherApiKey");
    String uriGetLocationByCity = "http://api.openweathermap.org/geo/1.0/direct?q=Province%20of%20Turin&limit=5&appid=" + apiKey;
    String uriGetWeatherByLocation = "http://api.openweathermap.org/data/2.5/weather?lat=45.133&lon=7.367&limit=5&units=metric&appid=" + apiKey;
    BigDecimal lat = BigDecimal.valueOf(45.133);
    BigDecimal lon = BigDecimal.valueOf(7.367);
    String cityName = "Province of Turin";



    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetWeatherByLocation() {
        mockServer.expect(requestTo(uriGetWeatherByLocation)).andRespond(withSuccess(WEATHER_RESPONSE, MediaType.APPLICATION_JSON));


        WeatherDto result = weatherApiService.getWeatherByLocation(cityName, lat, lon);

        assertEquals(cityName, result.getName());

    }

    @Test
    void testGetWeatherByLocationResponseVoidBody() {
        mockServer.expect(requestTo(uriGetWeatherByLocation)).andRespond(withSuccess("", MediaType.APPLICATION_JSON));
        assertThrows(WeatherNotFoundException.class,
                () -> { WeatherDto result = weatherApiService.getWeatherByLocation(cityName, lat, lon); });

    }

    @Test
    void testGetWeatherByLocationException4XX() {
        mockServer.expect(requestTo(uriGetWeatherByLocation)).andRespond(withStatus(HttpStatus.NOT_FOUND));

       assertThrows(RuntimeException.class,
               () -> {WeatherDto result = weatherApiService.getWeatherByLocation(cityName, lat, lon);});

    }

    @Test
    void testGetWeatherByLocationException5XX() {
        mockServer.expect(requestTo(uriGetWeatherByLocation)).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RuntimeException.class,
                () -> weatherApiService.getWeatherByLocation(cityName, lat, lon));

    }

    @Test
    void testGetWeatherByCity() {
        mockServer.expect(requestTo(uriGetLocationByCity)).andRespond(withSuccess(LOCATION_RESPONSE, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(uriGetWeatherByLocation)).andRespond(withSuccess(WEATHER_RESPONSE, MediaType.APPLICATION_JSON));

        List<WeatherDto> result = weatherApiService.getWeatherByCity(cityName);

        assertEquals(cityName, result.get(0).getName());
    }

    @Test
    void testGetWeatherByCityLocationExceptionInLocationMethod4XX() {
        mockServer.expect(requestTo(uriGetLocationByCity)).andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(RuntimeException.class,
                () -> weatherApiService.getWeatherByCity(cityName));

    }

    @Test
    void testGetWeatherByCityLocationExceptionInLocationMethod5XX() {
        mockServer.expect(requestTo(uriGetLocationByCity)).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RuntimeException.class,
                () -> weatherApiService.getWeatherByCity(cityName));

    }

    @Test
    void testGetWeatherByCityLocationExceptionInWeatherMethod4XX() {
        mockServer.expect(requestTo(uriGetLocationByCity)).andRespond(withSuccess(LOCATION_RESPONSE, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(uriGetWeatherByLocation)).andRespond(withStatus(HttpStatus.NOT_FOUND));

        assertThrows(RuntimeException.class,
                () -> weatherApiService.getWeatherByCity(cityName));

    }

    @Test
    void testGetWeatherByCityLocationExceptionInWeatherMethod5XX() {
        mockServer.expect(requestTo(uriGetLocationByCity)).andRespond(withSuccess(LOCATION_RESPONSE, MediaType.APPLICATION_JSON));
        mockServer.expect(requestTo(uriGetWeatherByLocation)).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RuntimeException.class,
                () -> weatherApiService.getWeatherByCity(cityName));

    }

}