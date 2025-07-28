package org.ilyutsik.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ilyutsik.dto.WeatherDto;
import org.ilyutsik.model.Location;
import org.ilyutsik.model.User;
import org.ilyutsik.service.LocationService;
import org.ilyutsik.service.SessionService;
import org.ilyutsik.service.UserService;
import org.ilyutsik.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {

    private final WeatherService weatherService;
    public final LocationService locationService;

    public HomeController(SessionService sessionService, UserService userService,
                          WeatherService weatherService, LocationService locationService) {
        super(sessionService, userService);
        this.weatherService = weatherService;
        this.locationService = locationService;
    }

    @GetMapping
    public String home(HttpServletRequest request, HttpServletResponse response, Model model) {
        Optional<User> optionalUser = checkUserAuthorization(request, model);
        if (optionalUser.isEmpty()) {
            return "redirect:/authorization";
        }
        List<Location> locations = locationService.getUserLocations(optionalUser.get());
        List<WeatherDto> locationsDto = new ArrayList<>();
        for (Location location : locations) {
            locationsDto.add(weatherService.getWeatherByCoordinates(location.getLatitude(), location.getLongitude()));
        }
        model.addAttribute("locations", locationsDto);
        return "home";
    }

    @GetMapping("/find-city")
    public String find(@RequestParam("city") String city, HttpServletRequest request, Model model) {
        Optional<User> optionalUser = checkUserAuthorization(request, model);
        if (optionalUser.isEmpty()) {
            return "redirect:/authorization";
        }
        List<WeatherDto> cityWeatherList = weatherService.getWeather(city);
        if (cityWeatherList.isEmpty()) {
            model.addAttribute("isListEmpty", true);
            model.addAttribute("city", city);
        }
        model.addAttribute("cityWeatherList", cityWeatherList);
        return "cities";

    }

    @PostMapping("/add-location")
    public String addLocation(@RequestParam("lat") BigDecimal lat, @RequestParam("lon") BigDecimal lon,
                              @RequestParam("name") String name, HttpServletRequest request, Model model) {

        Optional<User> optionalUser = checkUserAuthorization(request, model);
        if (optionalUser.isEmpty()) {
            return "redirect:/authorization";
        }

        Location newLocation = Location.builder()
                .user(optionalUser.get())
                .name(name)
                .latitude(lat)
                .longitude(lon)
                .build();

        locationService.addLocation(newLocation);

        return "redirect:/home";
    }

    @PostMapping("/delete-location")
    public String deleteLocation(@RequestParam("lat") BigDecimal lat, @RequestParam("lon") BigDecimal lon,
                                 @RequestParam("name") String name, HttpServletRequest request, Model model) {

        Optional<User> optionalUser = checkUserAuthorization(request, model);
        if (optionalUser.isEmpty()) {
            return "redirect:/authorization";
        }

        locationService.deleteLocation(lat, lon, name);
        return "redirect:/home";
    }

}
