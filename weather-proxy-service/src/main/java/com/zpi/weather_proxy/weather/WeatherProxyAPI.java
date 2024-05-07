package com.zpi.weather_proxy.weather;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "open-meteo", url = "https://api.open-meteo.com/v1/")
public interface WeatherProxyAPI {

    @GetMapping("/forecast?daily=weather_code,temperature_2m_max,temperature_2m_min,uv_index_max,rain_sum,wind_speed_10m_max&timezone=Europe%2FBerlin")
    ResponseEntity<List<WeatherResponseDto>> getForecastList(@RequestParam("latitude") List<Double> latitude, @RequestParam("longitude") List<Double> longitude,
                                                             @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate);

    @GetMapping("/forecast?daily=weather_code,temperature_2m_max,temperature_2m_min,uv_index_max,rain_sum,wind_speed_10m_max&timezone=Europe%2FBerlin")
    ResponseEntity<WeatherResponseDto> getForecast(@RequestParam("latitude") List<Double> latitude, @RequestParam("longitude") List<Double> longitude,
                                                   @RequestParam("start_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam("end_date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate);
}
