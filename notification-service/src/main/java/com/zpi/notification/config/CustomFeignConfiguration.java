package com.zpi.notification.config;

import com.zpi.notification.exceptions.CustomFeignErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFeignConfiguration extends FeignClientProperties.FeignClientConfiguration {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}
