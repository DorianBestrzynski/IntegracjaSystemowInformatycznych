package com.zpi.notification.event.listener;

import com.zpi.notification.event.model.UserJoinedGroupEvent;
import com.zpi.notification.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserJoinedGroupListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = {"q.user-joined-group"})
    public void onUserJoiningGroup(UserJoinedGroupEvent event) {
        notificationService.sendNotificationForUserJoiningGroup(event);
    }
}
