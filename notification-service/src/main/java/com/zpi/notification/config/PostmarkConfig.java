package com.zpi.notification.config;



import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-keys.yml")
public class PostmarkConfig {

    @Value("${postmark_token}")
    private String postmarkToken;

    @Bean
    public ApiClient postmarkClient() {
        return Postmark.getApiClient(postmarkToken);
    }
}
