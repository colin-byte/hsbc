package com.hsbc.trade.app.transaction.command;

import com.hsbc.trade.app.transaction.assembler.TransactionAssembler;
import com.hsbc.trade.client.dto.data.ErrorCode;
import com.hsbc.trade.client.dto.data.TransactionVO;
import com.hsbc.trade.client.dto.TransModifyCmd;
import com.hsbc.trade.domain.service.TransactionDomainService;
import com.hsbc.trade.domain.entity.TransactionEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import com.hsbc.trade.domain.exception.ParamValidException;

import javax.annotation.Resource;

@Component
public class TransactionModifyCmdExe {
    @Resource
    TransactionDomainService transactionDomainService;

    public TransactionVO execute(TransModifyCmd transModifyCmd) {
        if (transModifyCmd == null || transModifyCmd.getAmount() == null || transModifyCmd.getAmount().doubleValue() < 1
                || StringUtils.isEmpty(transModifyCmd.getSourceAccount())
                || StringUtils.isEmpty(transModifyCmd.getDestAccount())) {
            throw new ParamValidException(ErrorCode.PARAMETER_VALUE_INVALID);
        }
        TransactionEntity entity = transactionDomainService.process(TransactionAssembler.toEntity(transModifyCmd));
        return TransactionAssembler.toValueObject(entity);
    }
}
