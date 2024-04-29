package com.zpi.notification.event.listener;

import com.zpi.notification.event.model.UserRegisteredEvent;
import com.zpi.notification.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = {"q.user-registration"})
    public void onUserRegistration(UserRegisteredEvent event) {
        notificationService.sendNotificationForUserRegistration(event);
    }
}
