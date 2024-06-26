package com.zpi.dayplanservice.attraction;

import com.zpi.dayplanservice.dto.AttractionCandidateDto;
import com.zpi.dayplanservice.dto.AttractionFullInfoDto;
import com.zpi.dayplanservice.dto.AttractionPlanDto;
import com.zpi.dayplanservice.dto.RankByType;
import com.zpi.dayplanservice.weather.Weather;
import com.zpi.dayplanservice.weather.WeatherRequest;
import com.zpi.dayplanservice.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("api/v1/attraction")
@RequiredArgsConstructor
public class AttractionController {
    private final AttractionService attractionService;
    private final WeatherService weatherService;

    @GetMapping()
    public ResponseEntity<List<AttractionFullInfoDto>> getAllAttractionsForDay(@RequestParam Long groupId, @RequestParam Long dayPlanId){
        var result = attractionService.getAllAttractionsForDay(groupId, dayPlanId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/weather")
    public ResponseEntity<Collection<Weather>> getWeatherForGeolocation(@RequestBody WeatherRequest weatherRequest){
        var result = weatherService.getWeatherForAttraction(weatherRequest.getDate(), weatherRequest.getGeolocations()).values();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/find")
    public ResponseEntity<List<AttractionCandidateDto>> getCandidates(@RequestParam(name = "name") String name) {
        var attractionCandidates = attractionService.findCandidates(name);
        return ResponseEntity.ok(attractionCandidates);
    }

    @GetMapping("/find-nearby")
    public ResponseEntity<List<AttractionCandidateDto>> getCandidates(@RequestParam(name = "longitude") Double longitude,
                                                                      @RequestParam(name = "latitude") Double latitude,
                                                                      @RequestParam(name = "queryCategory", required = false) String queryCategory,
                                                                      @RequestParam(name = "rankByType", required = false) RankByType rankByType) {
        var attractionCandidates = attractionService.findNearbyCandidates(longitude, latitude, queryCategory, rankByType);
        return ResponseEntity.ok(attractionCandidates);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAttraction(@RequestParam Long attractionId, @RequestParam Long dayPlanId){
        attractionService.deleteAttraction(attractionId, dayPlanId);
    }

    @PostMapping()
    public ResponseEntity<Attraction> addAttraction(@RequestParam(name = "dayPlanId") List<Long> dayPlanIds,
                                                    @RequestBody AttractionCandidateDto attractionCandidateDto){
        var result = attractionService.addAttraction(dayPlanIds,attractionCandidateDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping()
    public ResponseEntity<Attraction> editAttraction(@RequestBody Attraction attraction) {
        var result = attractionService.editAttraction(attraction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/optimize/{dayPlanId}")
    public ResponseEntity<List<AttractionPlanDto>> getOptimizedDay(@PathVariable Long dayPlanId) {
        var result = attractionService.findOptimalDayPlan(dayPlanId);
        return ResponseEntity.ok(result);
    }
}
