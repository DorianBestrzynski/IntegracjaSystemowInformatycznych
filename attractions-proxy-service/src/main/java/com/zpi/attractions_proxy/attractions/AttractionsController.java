package com.zpi.attractions_proxy.attractions;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController()
@RequestMapping("api/v1/attractions")
@RequiredArgsConstructor
@Slf4j
public class AttractionsController {
    private final AttractionsService attractionsService;

    @GetMapping("/find")
    public ResponseEntity<List<AttractionCandidateDto>> getCandidates(@RequestParam(name = "name") String name) throws IOException, InterruptedException, ApiException {
        var attractionCandidates = attractionsService.findCandidates(name);
        return ResponseEntity.ok(attractionCandidates);
    }

    @GetMapping("/find-nearby")
    public ResponseEntity<List<AttractionCandidateDto>> getNearByCandidates(@RequestParam(name = "longitude") Double longitude,
                                                                            @RequestParam(name = "latitude") Double latitude,
                                                                            @RequestParam(name = "queryCategory", required = false) String queryCategory,
                                                                            @RequestParam(name = "rankByType", required = false) RankByType rankByType) {
        var attractionCandidates = attractionsService.findNearbyCandidates(longitude, latitude, queryCategory, rankByType);
        return ResponseEntity.ok(attractionCandidates);
    }

    @PostMapping("/distance-matrix")
    public ResponseEntity<DistanceMatrix> getDistanceMatrix(@RequestBody List<LatLng> coordinates)  {
        var distanceMatrix = attractionsService.getDistanceMatrix(coordinates);
        return ResponseEntity.ok(distanceMatrix);
    }

}
