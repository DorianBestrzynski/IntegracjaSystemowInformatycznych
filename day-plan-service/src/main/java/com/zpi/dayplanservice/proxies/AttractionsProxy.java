package com.zpi.dayplanservice.proxies;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.zpi.dayplanservice.configuration.CustomFeignConfiguration;
import com.zpi.dayplanservice.dto.AttractionCandidateDto;
import com.zpi.dayplanservice.dto.GroupInformationDto;
import com.zpi.dayplanservice.dto.RankByType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "attractions", url = "${app.attractions-proxy}:8090/api/v1", configuration = CustomFeignConfiguration.class)
public interface AttractionsProxy {

    @GetMapping("/find")
    List<AttractionCandidateDto> getCandidates(@RequestParam(name = "name")String name);

    @GetMapping( "/find-nearby")
    List<AttractionCandidateDto> getNearByCandidates(@RequestParam(name = "longitude") Double longitude,
                                                @RequestParam(name = "latitude") Double latitude,
                                                @RequestParam(name = "queryCategory", required = false) String queryCategory,
                                                @RequestParam(name = "rankByType", required = false) RankByType rankByType);

    @GetMapping("/distance-matrix")
    DistanceMatrix getDistanceMatrix(List<LatLng> coordinates);

}
