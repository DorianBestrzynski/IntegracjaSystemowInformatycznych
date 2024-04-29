package com.zpi.dayplanservice.proxies;

import com.zpi.dayplanservice.weather.Weather;
import com.zpi.dayplanservice.weather.WeatherRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "weather", url = "${app.weather-proxy-service}:8091/api/v1/weather")
public interface WeatherClient {
    @PostMapping
    List<Weather> getWeather(@RequestBody WeatherRequest weatherRequest);
}
