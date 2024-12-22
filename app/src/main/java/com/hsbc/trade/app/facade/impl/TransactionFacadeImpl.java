package com.hsbc.trade.app.facade.impl;

import com.hsbc.trade.app.transaction.command.TransactionModifyCmdExe;
import com.hsbc.trade.client.api.TransactionFacade;
import com.hsbc.trade.client.dto.data.TransactionVO;
import com.hsbc.trade.client.dto.TransModifyCmd;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class TransactionFacadeImpl implements TransactionFacade {
    @Resource
    TransactionModifyCmdExe transactionModifyCmdExe;

    @Override
    public TransactionVO modify(TransModifyCmd transModifyCmd) {
        return transactionModifyCmdExe.execute(transModifyCmd);
    }
}
