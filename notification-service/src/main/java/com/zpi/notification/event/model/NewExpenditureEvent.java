package com.zpi.notification.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewExpenditureEvent {
    private String title;
    private BigDecimal price;
    private Long groupId;
    private Long creatorId;
    private List<Long> expenseDebtors;
    private Instant creationDate;
}
