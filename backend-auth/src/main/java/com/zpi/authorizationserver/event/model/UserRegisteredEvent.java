package com.zpi.authorizationserver.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRegisteredEvent {
    private String eventName;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Instant eventTimestamp;
}
