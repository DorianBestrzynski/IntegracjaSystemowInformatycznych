package com.zpi.financeoptimizerservice.event.sender;

import com.zpi.financeoptimizerservice.event.model.ResolvedExpenditureEvent;
import com.zpi.financeoptimizerservice.financial_request.FinancialRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResolveExpenditureSender {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(FinancialRequest financialRequest) {
        var event = createEvent(financialRequest);
        rabbitTemplate.convertAndSend("q.resolved-expenditure", event);
        log.info("Resolve expenditure event sent: {}", event);
    }

    private ResolvedExpenditureEvent createEvent(FinancialRequest financialRequest) {
        return new ResolvedExpenditureEvent(financialRequest.getDebtor(),
                financialRequest.getDebtee(),
                financialRequest.getGroupId(),
                financialRequest.getAmount(),
                Instant.now());
    }
}
