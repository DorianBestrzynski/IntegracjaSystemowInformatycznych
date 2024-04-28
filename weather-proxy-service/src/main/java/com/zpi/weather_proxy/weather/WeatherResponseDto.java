package com.zpi.weather_proxy.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record WeatherResponseDto(@JsonProperty("cod") String responseCode,
                                 @JsonProperty("list") List<ListData> dataList) {

    public record ListData(Instant dt, MainData main, WindData wind, List<WeatherData> weather) {

        public record MainData(double temp, double temp_min, double temp_max, int pressure) {
        }

        public record WindData(double speed) {
        }

        public record WeatherData(String description) {
        }
    }
}

