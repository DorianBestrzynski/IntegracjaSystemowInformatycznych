package com.zpi.dayplanservice.proxies;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
//import com.zpi.dayplanservice.configuration.CustomFeignConfiguration;
import com.zpi.dayplanservice.attraction.DistanceMatrixDTO;
import com.zpi.dayplanservice.dto.AttractionCandidateDto;
import com.zpi.dayplanservice.dto.GroupInformationDto;
import com.zpi.dayplanservice.dto.RankByType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "attractions", url = "${app.attractions-proxy}:8090/api/v1/attractions")
public interface AttractionsProxy {

    @GetMapping("/find")
    List<AttractionCandidateDto> getCandidates(@RequestParam(name = "name") String name);

    @GetMapping( "/find-nearby")
    List<AttractionCandidateDto> getNearByCandidates(@RequestParam(name = "longitude") Double longitude,
                                                @RequestParam(name = "latitude") Double latitude,
                                                @RequestParam(name = "queryCategory", required = false) String queryCategory,
                                                @RequestParam(name = "rankByType", required = false) RankByType rankByType);

    @PostMapping("/distance-matrix")
    DistanceMatrixDTO getDistanceMatrix(@RequestBody List<LatLng> coordinates);

}
