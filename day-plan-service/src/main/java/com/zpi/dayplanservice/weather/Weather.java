package com.zpi.dayplanservice.weather;

import lombok.Data;

@Data
public class Weather {
    private Long attractionId;
    private double minTemperature;
    private double maxTemperature;
    private double uvIndex;
    private double rainSum;
    private double windSpeed;
    private String weatherDescription;
}
