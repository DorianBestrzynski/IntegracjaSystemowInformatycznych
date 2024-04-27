package com.zpi.attractions_proxy.attractions;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("api/v1/attractions")
@RequiredArgsConstructor
public class AttractionsController {
    private final AttractionsService attractionsService;

    @GetMapping("/find")
    public ResponseEntity<List<AttractionCandidateDto>> getCandidates(@RequestParam(name = "name") String name) {
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

    @GetMapping("/distance-matrix")
    public ResponseEntity<DistanceMatrix> getDistanceMatrix(List<LatLng> coordinates) {
        var distanceMatrix = attractionsService.getDistanceMatrix(coordinates);
        return ResponseEntity.ok(distanceMatrix);
    }

}
