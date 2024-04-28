package com.zpi.weather_proxy.weather;

import com.zpi.weather_proxy.CustomWeatherFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "test", url = "https://api.openweathermap.org/data/2.5", configuration = CustomWeatherFeignConfiguration.class)
public interface WeatherProxyAPI {

    @GetMapping("/forecast?units=metric")
    WeatherResponseDto get5dayWeather(@RequestParam("lat") Double latitude, @RequestParam("lon") Double longitude);
}
