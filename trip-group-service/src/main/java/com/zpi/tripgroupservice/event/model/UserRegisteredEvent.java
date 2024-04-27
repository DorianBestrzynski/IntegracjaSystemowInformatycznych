package com.zpi.tripgroupservice.event.model;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;
import java.time.LocalDate;

@Getter
public class UserRegisteredEvent extends ApplicationEvent {
    private String eventName;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birhday;
    private Instant eventTimestamp;

    public UserRegisteredEvent(Object source, String eventName, String email, String firstName, String lastName, String phoneNumber, LocalDate birhday, Instant eventTimestamp) {
        super(source);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.birhday = birhday;
        this.eventName = eventName;
        this.eventTimestamp = eventTimestamp;
    }
}
