package com.zpi.dayplanservice.dto;

import com.zpi.dayplanservice.attraction.Attraction;
import com.zpi.dayplanservice.weather.Weather;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttractionFullInfoDto {
    Attraction attraction;
    Weather weather;
}
