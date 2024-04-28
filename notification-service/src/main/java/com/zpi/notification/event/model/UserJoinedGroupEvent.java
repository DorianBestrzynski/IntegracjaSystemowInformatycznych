package com.zpi.notification.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinedGroupEvent {
    private Long userId;
    private Long groupId;
}
