package com.hsbc.trade.domain.service;

import com.hsbc.trade.domain.entity.TransactionEntity;

public interface TransactionDomainService {
    TransactionEntity modify(TransactionEntity transactionEntity);


    TransactionEntity process(TransactionEntity transactionEntity);

}
