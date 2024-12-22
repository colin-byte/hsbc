package com.hsbc.trade.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.hsbc.trade.client.dto.common.DTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class TransModifyCmd extends DTO {
    private String sourceAccount;

    private String destAccount;

    private long timestamp;

    private BigDecimal amount;

}
