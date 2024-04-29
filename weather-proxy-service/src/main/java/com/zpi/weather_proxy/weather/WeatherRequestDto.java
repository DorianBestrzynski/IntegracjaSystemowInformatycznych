package com.zpi.weather_proxy.weather;

import java.time.LocalDate;
import java.util.List;

public record WeatherRequestDto(LocalDate date, List<AttractionInfo> geolocations) {

    public record AttractionInfo(
            long attractionId,
            double latitude,
            double longitude
    ){}
}
