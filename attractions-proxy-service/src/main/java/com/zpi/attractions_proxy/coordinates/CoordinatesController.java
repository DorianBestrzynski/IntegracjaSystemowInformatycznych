package com.zpi.attractions_proxy.coordinates;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coordinates")
public class CoordinatesController {
    private final GeolocationService geolocationService;

    @GetMapping()
    public ResponseEntity<Double[]> getCoordinates(String startLocation) {
        return ResponseEntity.ok(geolocationService.findCoordinates(startLocation));
    }
}
