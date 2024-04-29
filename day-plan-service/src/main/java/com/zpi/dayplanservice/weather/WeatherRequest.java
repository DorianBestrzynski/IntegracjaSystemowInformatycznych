package com.zpi.dayplanservice.weather;

import com.zpi.dayplanservice.attraction.AttractionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class WeatherRequest {
    private LocalDate date;
    private List<AttractionInfo> geolocations;
}
