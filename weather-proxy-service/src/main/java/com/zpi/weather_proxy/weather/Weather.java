package com.zpi.weather_proxy.weather;

public record Weather(long attractionId, double temperature, double minTemperature, double maxTemperature,
                      double pressure,
                      double windSpeed, String weatherDescription) {

    public static Weather toWeather(long attractionId, WeatherResponseDto.ListData dto) {
        return new Weather(attractionId, dto.main().temp(), dto.main().temp_min(), dto.main().temp_max(),
                dto.main().pressure(), dto.wind().speed(), dto.weather().get(0).description());
    }
}
