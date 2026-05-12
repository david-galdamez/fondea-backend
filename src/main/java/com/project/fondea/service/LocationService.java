package com.project.fondea.service;

import com.project.fondea.dto.location.RegisterLocation;
import com.project.fondea.model.Location;
import com.project.fondea.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public Location register(RegisterLocation registerLocation) {
        var location = Location.builder()
                .country(registerLocation.getCountry())
                .city(registerLocation.getCity())
                .build();

        return locationRepository.save(location);
    }

    public List<Location> getAll() {
        return locationRepository.findAll();
    }
}
