package com.zpi.weather_proxy;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class WeatherClientInterceptor implements RequestInterceptor {
    private String apiKey;

    public WeatherClientInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override public void apply(RequestTemplate template) {
        template.query( "appid", apiKey);
    }
}
