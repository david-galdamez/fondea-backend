package com.project.fondea.controller;

import com.project.fondea.dto.location.RegisterLocation;
import com.project.fondea.model.Location;
import com.project.fondea.service.LocationService;
import com.project.fondea.util.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<ApiResponse<Location>> create(@RequestBody RegisterLocation location, HttpServletRequest request) {
        var category = locationService.register(location);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(category, "Categoria creada con exito", request.getRequestURI()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Location>>> get(HttpServletRequest request) {
        var category = locationService.getAll();

        return ResponseEntity.ok(ApiResponse.ok(category, "Categorias obtenidas con exito", request.getRequestURI()));
    }
}
