package com.zpi.financeoptimizerservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResolvedExpenditureEvent {
    private Long debtorId;
    private Long debteeId;
    private Long groupId;
    private BigDecimal amount;
    private Instant resolvedDate;
}
