package com.zpi.notification.event.listener;

import com.zpi.notification.event.model.UserJoinedGroupEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserJoinedGroupListener {

    @RabbitListener(queues = {"q.user-joined-group"})
    public void onUserJoiningGroup(UserJoinedGroupEvent event) {
        log.info("User Joined Group Event Received: {}", event);
        System.out.println(event.getUserId());
    }
}
