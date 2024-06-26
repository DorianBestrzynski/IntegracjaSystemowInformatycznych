package com.zpi.dayplanservice.configuration;
import com.zpi.dayplanservice.exception.CustomFeignErrorDecoder;
import com.zpi.dayplanservice.exception.CustomFeignErrorDecoder;
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
