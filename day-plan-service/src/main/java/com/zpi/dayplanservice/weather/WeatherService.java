package com.zpi.dayplanservice.weather;

import com.zpi.dayplanservice.attraction.AttractionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherClient weatherClient;

    public Map<Long, Weather> getWeatherForAttraction(LocalDate date, List<AttractionInfo> attractions) {
            var weatherRequest = new WeatherRequest(date, attractions);
            var response = weatherClient.getWeather(weatherRequest);
            return response.stream()
                    .collect(toMap(Weather::getAttractionId, Function.identity()));
    }
}
