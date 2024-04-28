package com.zpi.authorizationserver.event.sender;

import com.zpi.authorizationserver.dto.RegisterRequestDto;
import com.zpi.authorizationserver.event.model.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.zpi.authorizationserver.event.model.Constants.USER_REGISTERED_EVENT;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRegisteredSender {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(RegisterRequestDto requestDto) {
        var event = createEvent(requestDto);
        rabbitTemplate.convertAndSend("q.user-registration", event);
        log.info("User Registered event sent: {}", event);
    }

    private UserRegisteredEvent createEvent(RegisterRequestDto requestDto) {
        return new UserRegisteredEvent(USER_REGISTERED_EVENT,
                requestDto.email(),
                requestDto.firstName(),
                requestDto.surname(),
                requestDto.phoneNumber(),
                Instant.now());
    }
}

