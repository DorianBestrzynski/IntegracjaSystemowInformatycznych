package com.zpi.financeoptimizerservice.event.sender;

import com.zpi.financeoptimizerservice.event.model.NewExpenditureEvent;
import com.zpi.financeoptimizerservice.expenditure.Expenditure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewExpenditureSender {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(Expenditure expenditure) {
        var event = createEvent(expenditure);
        rabbitTemplate.convertAndSend("q.new-expenditure", event);
        log.info("New expenditure event sent: {}", event);
    }

    private NewExpenditureEvent createEvent(Expenditure expenditure) {
        return new NewExpenditureEvent(expenditure.getTitle(),
                expenditure.getPrice(),
                expenditure.getGroupId(),
                expenditure.getCreatorId(),
                expenditure.getExpenseDebtors(),
                Instant.now());
    }
}
