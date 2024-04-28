package com.zpi.attractions_proxy;

import com.google.maps.GeoApiContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class GeoApiContextConfig {

    @Value("${google_api_key}")
    private String googleApiKey;

    @Bean
    public GeoApiContext context() {
        return new GeoApiContext.Builder().apiKey(googleApiKey)
                .build();
    }
}
