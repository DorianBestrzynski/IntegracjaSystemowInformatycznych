package com.zpi.financeoptimizerservice.event.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue createNewExpendiutreQueue() {
        return new Queue("q.new-expenditure");
    }

    @Bean
    public Queue createResolvedExpendiutreQueue() {
        return new Queue("q.resolved-expenditure");
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(mapper);
    }
}