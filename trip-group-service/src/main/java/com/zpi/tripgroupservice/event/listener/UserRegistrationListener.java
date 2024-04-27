package com.zpi.tripgroupservice.event.listener;

import com.zpi.tripgroupservice.event.model.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserRegistrationListener {

    @RabbitListener(queues = {"q.user-registration"})
    public void onUserRegistration(UserRegisteredEvent event) {
        log.info("User Registration Event Received: {}", event);
        System.out.println(event.getEventName());
    }
}
