package com.zpi.dayplanservice.weather;

import lombok.Data;

@Data
public class Weather {
    private Long attractionId;
    private double temperature;
    private double minTemperature;
    private double maxTemperature;
    private double pressure;
    private double windSpeed;
    private String weatherDescription;
}
