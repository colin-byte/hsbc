package com.hsbc.trade.infrastructure.gateway.impl;

import com.hsbc.trade.domain.entity.TransactionEntity;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.dataobject.AccountDO;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.dataobject.TransactionDO;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.mapper.AccountMapper;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.mapper.TransactionMapper;
import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

@Repository
public class TransactionProxy {

    private static final Logger log = LoggerFactory.getLogger(TransactionProxy.class);
    @Resource
    private TransactionMapper transactionMapper;

    @Resource
    private AccountMapper accountMapper;

    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000L, multiplier = 2))
    @Transactional(rollbackFor = Throwable.class)
    public void processTransaction(TransactionEntity entity) {
        TransactionDO transactionDO = new TransactionDO();
        String transId = UUID.randomUUID().toString().replace("-", "");
        transactionDO.setTransactionId(transId);
        transactionDO.setSourceAccount(entity.getSourceAccount());
        transactionDO.setDestAccount(entity.getDestAccount());
        transactionDO.setAmount(entity.getAmount());
        transactionDO.setTimestamp(System.currentTimeMillis());
        int row = transactionMapper.insert(transactionDO);

        if (row < 1) {
            throw new RuntimeException("record transaction flow error");
        }

        AccountDO sourceDo = new AccountDO();
        sourceDo.setAccountId(entity.getSourceAccount());
        sourceDo.setAmount(entity.getAmount());
        sourceDo.setAccountId(sourceDo.getAccountId());
        row = accountMapper.decreaseBalance(sourceDo);
        if (row < 1) {
            throw new PersistenceException("更新源账户异常");
        }
        AccountDO destDo = new AccountDO();
        destDo.setAccountId(entity.getDestAccount());
        destDo.setAmount(entity.getAmount());
        row = accountMapper.increaseBalance(destDo);
        if (row < 1) {
            throw new PersistenceException("更新目标账户异常");
        }
    }

    @Recover
    private void transactionErrorRecover(Exception e, TransactionEntity entity) {
        //todo 处理最终失败的情况
        log.info("交易异常重复执行失败,message:{},entity:{}", e.getMessage(), entity);
    }
}
