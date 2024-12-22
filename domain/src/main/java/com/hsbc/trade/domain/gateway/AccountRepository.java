package com.hsbc.trade.domain.gateway;

import com.hsbc.trade.domain.entity.AccountEntity;

public interface AccountRepository {

    AccountEntity getAccountById(String sourceAccount);
}
