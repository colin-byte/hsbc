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
@Table(name = "t_transaction")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private String transactionId;

    private String sourceAccount;

    private String destAccount;

    private long timestamp;

    private BigDecimal amount;

    private Timestamp createTime;

    private Timestamp updateTime;

    private int isDelete;

}
