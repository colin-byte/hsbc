package com.hsbc.trade.domain.gateway;

import com.hsbc.trade.domain.entity.AccountEntity;
import com.hsbc.trade.domain.entity.TransactionEntity;

public interface TransactionRepository {
    public void processTransaction(TransactionEntity transactionEntity, AccountEntity sourceAccount, AccountEntity destAccount);

    public boolean processTransaction(TransactionEntity entity);

    void sendTransMsg2RecordTransaction(TransactionEntity entity);
}
