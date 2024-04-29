package com.zpi.weather_proxy.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record WeatherResponseDto(@JsonProperty("latitude") Double lat,
                                 @JsonProperty("longitude") Double lon,
                                 @JsonProperty("daily") DailyWeatherData dailyWeatherData){

    public record DailyWeatherData(List<Integer> weather_code,
                                   @JsonProperty("temperature_2m_min") List<Double> temp_min,
                                   @JsonProperty("temperature_2m_max") List<Double> temp_max,
                                   @JsonProperty("wind_speed_10m_max") List<Double> wind_speed,
                                   @JsonProperty("uv_index_max") List<Double> uv_index,
                                   List<Double> rain_sum) {}

}

