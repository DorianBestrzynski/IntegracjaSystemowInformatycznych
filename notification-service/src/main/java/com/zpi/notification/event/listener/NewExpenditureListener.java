package com.zpi.notification.event.listener;

import com.zpi.notification.event.model.NewExpenditureEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NewExpenditureListener {

    @RabbitListener(queues = {"q.new-expenditure"})
    public void onNewExpenditureAdded(NewExpenditureEvent event) {
        log.info("New Expenditure Event Received: {}", event);
        System.out.println(event.getCreationDate());
    }
}
