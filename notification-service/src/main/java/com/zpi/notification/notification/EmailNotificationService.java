package com.zpi.notification.notification;

import com.postmarkapp.postmark.client.exception.PostmarkException;
import com.zpi.notification.dto.UserDto;
import com.zpi.notification.event.model.NewExpenditureEvent;
import com.zpi.notification.event.model.ResolvedExpenditureEvent;
import com.zpi.notification.event.model.UserJoinedGroupEvent;
import com.zpi.notification.event.model.UserRegisteredEvent;
import com.zpi.notification.proxies.AppUserProxy;
import com.zpi.notification.proxies.UserGroupProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {
    private static final String INNER_COMMUNICATION = "microserviceCommunication";
    private final PostmarkAdapter postmarkAdapter;
    private final AppUserProxy appUserProxy;
    private final UserGroupProxy userGroupProxy;

    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm";
    private static final String BIG_DECIMAL_FORMAT = "#0.00";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
    DecimalFormat df = new DecimalFormat(BIG_DECIMAL_FORMAT);

    @Override
    public void sendNotificationForUserRegistration(UserRegisteredEvent event) {
        try {
            postmarkAdapter.sendEmail(event.getEmail(), Constants.USER_REGISTERED_TITLE, processUserRegisteredUserEmailTemplate(event.getFirstName()));
        } catch (PostmarkException | IOException e) {
            e.printStackTrace();
        }
    }

    private String processUserRegisteredUserEmailTemplate(String name) {
        return Constants.USER_REGISTERED_HTML_BODY.replace("{{ name }}", name);
    }

    @Override
    public void sendNotificationForUserJoiningGroup(UserJoinedGroupEvent event) {
        UserDto groupJoiner = appUserProxy.getUserById(INNER_COMMUNICATION, event.getUserId());
        List<UserDto> groupCoordinators = userGroupProxy.getAllGroupCoordinators(INNER_COMMUNICATION, event.getGroupId());

        for (UserDto coordinator : groupCoordinators) {
            try {
                postmarkAdapter.sendEmail(coordinator.email(), Constants.USER_ADDED_TO_GROUP_TITLE, processUserJoiningGroupEmailTemplate(groupJoiner.firstName(), groupJoiner.surname()));
            } catch (PostmarkException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processUserJoiningGroupEmailTemplate(String name, String surname) {
        return Constants.USER_ADDED_TO_GROUP_HTML_BODY.replace("{{ name }}", name).replace("{{ surname }}", surname);
    }

    @Override
    public void sendNotificationForNewExpenditure(NewExpenditureEvent event) {
        String formattedDate = LocalDateTime.ofInstant(event.getCreationDate(), ZoneId.systemDefault()).format(formatter);
        String formattedPrice = df.format(event.getPrice());
        UserDto creator = appUserProxy.getUserById(INNER_COMMUNICATION, event.getCreatorId());
        List<UserDto> debtors = appUserProxy.getUsersByIds(INNER_COMMUNICATION, event.getExpenseDebtors());

        for (UserDto debtor: debtors) {
            try {
                postmarkAdapter.sendEmail(debtor.email(), Constants.NEW_EXPENDITURE_TITLE, processNewExpenditureEmailTemplate(debtor.firstName(), creator.firstName(), creator.surname(), event.getTitle(), formattedDate, formattedPrice));
            } catch (PostmarkException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String processNewExpenditureEmailTemplate(String debtorName, String name, String surname, String title, String date, String price) {
        return Constants.NEW_EXPENDITURE_HTML_BODY
                .replace("{{ debtorName }}", debtorName)
                .replace("{{ name }}", name)
                .replace("{{ surname }}", surname)
                .replace("{{ title }}", title)
                .replace("{{ date }}", date)
                .replace("{{ price }}", price);
    }

    @Override
    public void sendNotificationForResolvedExpenditure(ResolvedExpenditureEvent event) {
        String formattedDate = LocalDateTime.ofInstant(event.getResolvedDate(), ZoneId.systemDefault()).format(formatter);
        String formattedPrice = df.format(event.getAmount());
        UserDto debtee = appUserProxy.getUserById(INNER_COMMUNICATION, event.getDebteeId());
        UserDto debtor = appUserProxy.getUserById(INNER_COMMUNICATION, event.getDebtorId());

        try {
            postmarkAdapter.sendEmail(debtee.email(), Constants.RESOLVED_EXPENDITURE_TITLE, processResolvedExpenditureEmailTemplate(debtee.firstName(), debtor.firstName(), debtor.surname(), formattedDate, formattedPrice));
        } catch (PostmarkException | IOException e) {
            e.printStackTrace();
        }
    }

    private String processResolvedExpenditureEmailTemplate(String debtorName, String name, String surname, String date, String price) {
        return Constants.RESOLVED_EXPENDITURE_HTML_BODY
                .replace("{{ debteeName }}", debtorName)
                .replace("{{ name }}", name)
                .replace("{{ surname }}", surname)
                .replace("{{ date }}", date)
                .replace("{{ price }}", price);
    }
}
