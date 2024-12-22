package com.hsbc.trade.app.transaction.assembler;

import com.hsbc.trade.client.dto.TransModifyCmd;
import com.hsbc.trade.client.dto.data.TransactionVO;
import com.hsbc.trade.domain.entity.TransactionEntity;

public class TransactionAssembler {

    public static TransactionVO toValueObject(TransactionEntity transEntity) {
        TransactionVO transVO = new TransactionVO();
        if(transEntity == null){
            return transVO;
        }
        transVO.setId(transEntity.getId());
        transVO.setAmount(transEntity.getAmount());
        transVO.setTimestamp(transEntity.getTimestamp());
        transVO.setSourceAccount(transEntity.getSourceAccount());
        transVO.setDestAccount(transEntity.getDestAccount());
        return transVO;
    }

    public static TransactionEntity toEntity(TransModifyCmd transModifyCmd) {
        TransactionEntity transactionEntity = new  TransactionEntity();
        transactionEntity.setDestAccount(transModifyCmd.getDestAccount());
        transactionEntity.setSourceAccount(transModifyCmd.getSourceAccount());
        transactionEntity.setAmount(transModifyCmd.getAmount());
        return transactionEntity;
    }
}
