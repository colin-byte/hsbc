package com.hsbc.trade.domain.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionEntity {

    private long id;

    private String transactionId;

    private String sourceAccount;

    private String destAccount;

    private long timestamp;

    private BigDecimal amount;
}
