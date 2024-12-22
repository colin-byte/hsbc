package com.hsbc.trade.infrastructure.convertor;

import com.hsbc.trade.domain.entity.TransactionEntity;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.dataobject.TransactionDO;

public class TransactionConvertor {

    private TransactionConvertor() {
    }
    public static TransactionEntity toEntity(TransactionDO transactionDO) {
        TransactionEntity userEntity = new TransactionEntity();
        userEntity.setTransactionId(transactionDO.getTransactionId());
        userEntity.setSourceAccount(transactionDO.getSourceAccount());
        userEntity.setAmount(transactionDO.getAmount());
        userEntity.setDestAccount(transactionDO.getDestAccount());
        userEntity.setTimestamp(transactionDO.getTimestamp());
        return userEntity;
    }
}
