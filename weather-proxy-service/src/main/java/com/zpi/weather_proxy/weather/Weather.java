package com.zpi.weather_proxy.weather;

public record Weather(long attractionId, double minTemperature, double maxTemperature,
                      double uvIndex, double rainSum, double windSpeed, String weatherDescription) {

    public static Weather toWeather(long attractionId, WeatherResponseDto.DailyWeatherData dwd) {
        return new Weather(attractionId, dwd.temp_min().get(0), dwd.temp_max().get(0), dwd.uv_index().get(0),
                dwd.rain_sum().get(0), dwd.wind_speed().get(0), WeatherCode.fromCode(dwd.weather_code().get(0)).getDescription());
    }
}
