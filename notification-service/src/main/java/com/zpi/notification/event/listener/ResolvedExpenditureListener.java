package com.zpi.notification.event.listener;

import com.zpi.notification.event.model.ResolvedExpenditureEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResolvedExpenditureListener {

    @RabbitListener(queues = {"q.resolved-expenditure"})
    public void onUserRegistration(ResolvedExpenditureEvent event) {
        log.info("Resolved Expenditure Event Received: {}", event);
        System.out.println(event.getResolvedDate());
    }
}
