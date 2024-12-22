package com.hsbc.trade.client.dto.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionVO {
    private Long id;

    private String sourceAccount;

    private String destAccount;

    private long timestamp;

    private BigDecimal amount;

}
