package com.zpi.weather_proxy.weather;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/weather")
public class WeatherProxyController {
    private final WeatherProxyService weatherProxyService;

    @GetMapping()
    public ResponseEntity<List<Weather>> getWeather(@RequestBody WeatherRequestDto weatherRequestDto) {
        var result = weatherProxyService.getWeather(weatherRequestDto);
        return ResponseEntity.ok(result);
    }
}
