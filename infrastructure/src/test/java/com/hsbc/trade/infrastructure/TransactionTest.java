package com.hsbc.trade.infrastructure;


import com.hsbc.trade.domain.entity.TransactionEntity;
import com.hsbc.trade.domain.service.TransactionDomainService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionTest extends BaseTest {

    @Autowired
    TransactionDomainService transactionDomainService;

    @Test
    @SneakyThrows
    public void sourceAccountNotExistTest() {
        //测试source账户不存在，报错
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionDomainService.modify(transactionEntity);
    }

    @Test
    @SneakyThrows
    public void availableAmountNotEnough() {
        //source账户存在，但是可用金额小于转账金额
        TransactionEntity transactionEntity = new TransactionEntity();
        String sourceAccount = "3da6111a917b4aa09a0cce02e133c85a";
        BigDecimal amount = new BigDecimal(200);
        transactionEntity.setAmount(amount);
        transactionEntity.setSourceAccount(sourceAccount);
        transactionDomainService.modify(transactionEntity);
    }

    @Test
    @SneakyThrows
    public void destAccountNotExist() {
        //dest账户不存在
        TransactionEntity transactionEntity = new TransactionEntity();
        String sourceAccount = "3da6111a917b4aa09a0cce02e133c85a";
        BigDecimal amount = new BigDecimal(2);
        transactionEntity.setAmount(amount);
        transactionEntity.setSourceAccount(sourceAccount);
        transactionDomainService.modify(transactionEntity);
    }

    @Test
    @SneakyThrows
    public void sourceAccountModifyErrorTest() {
        //正常交易测试
        //source账户
        TransactionEntity transactionEntity = new TransactionEntity();
        String sourceAccount = "3da6111a917b4aa09a0cce02e133c85a";
        String destAccount = "c684fa083ca14fe8ad8332b1f1d7c4aa";
        BigDecimal amount = new BigDecimal(1);
        transactionEntity.setAmount(amount);
        transactionEntity.setSourceAccount(sourceAccount);
        transactionEntity.setDestAccount(destAccount);
        transactionDomainService.modify(transactionEntity);
    }


}
