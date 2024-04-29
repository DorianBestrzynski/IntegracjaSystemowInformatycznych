package com.zpi.notification.event.listener;

import com.zpi.notification.event.model.ResolvedExpenditureEvent;
import com.zpi.notification.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResolvedExpenditureListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = {"q.resolved-expenditure"})
    public void onResolvedExpenditure(ResolvedExpenditureEvent event) {
       notificationService.sendNotificationForResolvedExpenditure(event);
    }
}
