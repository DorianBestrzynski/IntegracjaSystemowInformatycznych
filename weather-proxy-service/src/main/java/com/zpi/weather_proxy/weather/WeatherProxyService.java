package com.zpi.weather_proxy.weather;

import com.zpi.weather_proxy.mapper.MapStructMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class WeatherProxyService {

    private final MapStructMapper mapper;
    private final WeatherProxyAPI weatherProxyAPI;


    public List<Weather> getWeather(WeatherRequestDto weatherRequestDto) {
        LocalDate date = weatherRequestDto.date();
        long daysDifference = LocalDate.now().until(date).getDays();
        if (daysDifference > 5) {
            throw new IllegalArgumentException("The requested date is too far in the future to provide accurate weather information.");
        }
        if (daysDifference < 0) {
            throw new IllegalArgumentException("The requested date cannot be in the past");
        }

        List<Weather> response = new ArrayList<>();
        for (WeatherRequestDto.AttractionInfo attraction : weatherRequestDto.geolocations()) {
            WeatherResponseDto responseDto = weatherProxyAPI.get5dayWeather(attraction.latitude(), attraction.longitude());
            if (!responseDto.responseCode().equals("200")) {
                throw new IllegalStateException("Failed to retrieve data: OpenWeather API returned a non-successful response code.");
            } else {
                //TODO: poprawić to filtrowanie
                var apiResponse = responseDto.dataList()
                        .stream()
                        .filter(w -> {
                            var converted = LocalDateTime.ofInstant(w.dt(), TimeZone
                                    .getDefault().toZoneId());
                            return converted.toLocalDate().equals(date) &&
                                    (converted.getHour() == 12 || converted.getHour() == 11 || converted.getHour() == 13);
                        })
                        .findFirst()
                        .map(dto -> Weather.toWeather(attraction.attractionId(), dto))
                        .orElseThrow(() -> new IllegalStateException("Weather information not found"));
                response.add(apiResponse);

            }

        }
        return response;
    }

}
