package com.hsbc.trade.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private String accountId;

    private String userName;

    private String userId;

    private BigDecimal amount;

    private Timestamp createTime;

    private Timestamp updateTime;

    int isDelete;
}
