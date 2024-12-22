package com.hsbc.trade.client.api;

import com.hsbc.trade.client.dto.data.TransactionVO;
import com.hsbc.trade.client.dto.TransModifyCmd;

public interface TransactionFacade {


    TransactionVO modify(TransModifyCmd transModifyCmd);
}
