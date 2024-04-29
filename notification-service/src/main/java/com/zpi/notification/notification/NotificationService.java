package com.zpi.notification.notification;

import com.zpi.notification.event.model.NewExpenditureEvent;
import com.zpi.notification.event.model.ResolvedExpenditureEvent;
import com.zpi.notification.event.model.UserJoinedGroupEvent;
import com.zpi.notification.event.model.UserRegisteredEvent;

public interface NotificationService {
    void sendNotificationForUserRegistration(UserRegisteredEvent event);
    void sendNotificationForUserJoiningGroup(UserJoinedGroupEvent event);
    void sendNotificationForNewExpenditure(NewExpenditureEvent event);
    void sendNotificationForResolvedExpenditure(ResolvedExpenditureEvent event);
}
