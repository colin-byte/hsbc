package com.hsbc.trade.infrastructure.gateway.impl.mysql.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;


@Data
@Table(name = "t_account")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;

    private String accountId;

    private String userName;

    private String userId;

    private BigDecimal amount;

    private Timestamp createTime;

    private Timestamp updateTime;

    int isDelete;

}
