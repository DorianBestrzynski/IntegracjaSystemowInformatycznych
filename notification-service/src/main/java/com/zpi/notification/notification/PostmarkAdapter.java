package com.zpi.notification.notification;

import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.message.Message;
import com.postmarkapp.postmark.client.exception.PostmarkException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PostmarkAdapter {

    @Value("${app.email-from}")
    private String fromEmailAddress;

    private final ApiClient postmarkClient;

    public void sendEmail(String recipient, String title, String htmlBody) throws PostmarkException, IOException {
        var message = new Message(fromEmailAddress, recipient, title, htmlBody);
        postmarkClient.deliverMessage(message);
    }
}
