package com.hsbc.trade.infrastructure.gateway.impl.mysql.mapper;

import com.hsbc.trade.infrastructure.gateway.impl.mysql.dataobject.TransactionDO;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;

@Mapper
public interface TransactionMapper extends BaseMapper<TransactionDO> {
    public TransactionDO getTransaction(String transactionId);

    public int insert(TransactionDO transactionDO);
}
