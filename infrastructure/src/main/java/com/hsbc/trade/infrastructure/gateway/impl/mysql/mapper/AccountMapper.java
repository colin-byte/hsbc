package com.hsbc.trade.infrastructure.gateway.impl.mysql.mapper;

import com.hsbc.trade.infrastructure.gateway.impl.mysql.dataobject.AccountDO;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

@Mapper
public interface AccountMapper extends BaseMapper<AccountDO> {

    public AccountDO getAccount(String accountId);

    public int updateBySelective(AccountDO accountDO);
    public int increaseBalance(AccountDO accountDO);
    public int decreaseBalance(AccountDO accountDO);

}
