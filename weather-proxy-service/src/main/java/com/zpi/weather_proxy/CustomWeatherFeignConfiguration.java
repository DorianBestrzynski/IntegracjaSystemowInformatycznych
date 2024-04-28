package com.zpi.weather_proxy;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-keys.yml")
public class CustomWeatherFeignConfiguration extends FeignClientProperties.FeignClientConfiguration {

    @Value("${open_weather_api_key}")
    private String apiKey;

    @Bean
    public RequestInterceptor clerkClientInterceptor() {
        return new WeatherClientInterceptor(apiKey);
    }
}
