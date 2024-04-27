package com.zpi.tripgroupservice.event.sender;

import com.zpi.tripgroupservice.event.model.UserJoinedGroupEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserJoinedGroupSender {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Long userId, Long groupId) {
        var event = createEvent(userId, groupId);
        rabbitTemplate.convertAndSend("q.user-joined-group", event);
    }

    private UserJoinedGroupEvent createEvent(Long userId, Long groupId) {
        return new UserJoinedGroupEvent(userId, groupId);
    }
}
