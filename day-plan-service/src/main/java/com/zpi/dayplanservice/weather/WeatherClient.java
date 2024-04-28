package com.zpi.dayplanservice.weather;

import com.zpi.dayplanservice.configuration.CustomFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "weather-proxy", url = "${app.weather-proxy-service}:8091/api/v1/weather", configuration = CustomFeignConfiguration.class)
public interface WeatherClient {
    @GetMapping
    List<Weather> getWeather(@RequestBody WeatherRequest weatherRequest);
}
