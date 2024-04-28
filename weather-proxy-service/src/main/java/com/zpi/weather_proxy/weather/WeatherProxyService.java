package com.zpi.weather_proxy.weather;

import com.zpi.weather_proxy.mapper.MapStructMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherProxyService {

    private final MapStructMapper mapper;
    private final WeatherProxyAPI weatherProxyAPI;


    public List<Weather> getWeather(WeatherRequestDto weatherRequestDto) {
        LocalDate date = weatherRequestDto.date();
        var daysDifference = Math.abs(ChronoUnit.DAYS.between(LocalDate.now(), date));
        if (daysDifference > 16) {
            throw new DatesOutOfRangeException("The requested date is too far in the future to provide accurate weather information.");
        }

        if (daysDifference < -90) {
            throw new DatesOutOfRangeException("The requested data is too far in the past");
        }

        List<Weather> response = new ArrayList<>();
        List<Double> longitudes = new ArrayList<>();
        List<Double> latitudes = new ArrayList<>();
        for (WeatherRequestDto.AttractionInfo attraction : weatherRequestDto.geolocations()) {
            longitudes.add(attraction.longitude());
            latitudes.add(attraction.latitude());
        }

        ResponseEntity<List<WeatherResponseDto>> responseEntity = weatherProxyAPI.getForecast(latitudes, longitudes, date, date);
        if (responseEntity.getStatusCode().value() != 200 || responseEntity.getBody() == null) {
            throw new IllegalStateException("Failed to retrieve data from OpenWeather API");
        } else if (weatherRequestDto.geolocations().size() == responseEntity.getBody().size()) {
            var apiResponse = responseEntity.getBody();
            int i = 0;
            for (WeatherRequestDto.AttractionInfo geolocation : weatherRequestDto.geolocations()) {
                var weather = Weather.toWeather(geolocation.attractionId(), apiResponse.get(i).dailyWeatherData());
                response.add(weather);
                i++;
            }

        }
        return response;
    }

}
