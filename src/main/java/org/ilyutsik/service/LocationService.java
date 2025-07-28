package org.ilyutsik.service;

import lombok.RequiredArgsConstructor;
import org.ilyutsik.exeption.UserNotFoundException;
import org.ilyutsik.model.Location;
import org.ilyutsik.model.User;
import org.ilyutsik.repository.LocationRepository;
import org.ilyutsik.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addLocation(Location location) {
        locationRepository.save(location);
    }

    @Transactional
    public List<Location> getUserLocations(User user) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findUserById(user.getId());
        if (optionalUser.isEmpty()) {
            // todo
//            throw new UserNotFoundException(user.getLogin());
        }
        return locationRepository.findByUserId(user.getId());
    }

    @Transactional
    public void deleteLocation(BigDecimal lat, BigDecimal lon, String name) {
        locationRepository.deleteByLatitudeAndLongitudeAndName(lat, lon, name);
    }
}
