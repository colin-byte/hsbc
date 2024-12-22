package com.hsbc.trade.infrastructure.gateway.impl;

import com.hsbc.trade.domain.entity.AccountEntity;
import com.hsbc.trade.domain.gateway.AccountRepository;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.dataobject.AccountDO;
import com.hsbc.trade.infrastructure.gateway.impl.mysql.mapper.AccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository("accountRepository")
public class AccountRepositoryImpl implements AccountRepository {

    private static final Logger log = LoggerFactory.getLogger(AccountRepositoryImpl.class);
    @Resource
    private AccountMapper accountMapper;

    @Override
    public AccountEntity getAccountById(String sourceAccount) {
        AccountDO accountDO = accountMapper.getAccount(sourceAccount);
        if (accountDO == null) {
            log.warn("account info is null,account id:{}", sourceAccount);
            return null;
        }
        return AccountEntity.builder()
                .accountId(accountDO.getAccountId())
                .userId(accountDO.getUserId())
                .amount(accountDO.getAmount())
                .createTime(accountDO.getCreateTime())
                .userName(accountDO.getUserName())
                .build();
    }
}
