package com.zpi.weather_proxy.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/weather")
public class WeatherProxyController {
    private final WeatherProxyService weatherProxyService;

    @PostMapping
    public ResponseEntity<List<Weather>> getWeather(@RequestBody WeatherRequestDto weatherRequestDto) {
        log.info("Getting weather for {}", weatherRequestDto);
        var result = weatherProxyService.getWeather(weatherRequestDto);
        return ResponseEntity.ok(result);
    }
}
