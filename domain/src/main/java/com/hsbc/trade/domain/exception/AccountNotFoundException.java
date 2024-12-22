package com.hsbc.trade.domain.exception;

import com.hsbc.trade.client.dto.data.ErrorCode;
import com.hsbc.trade.client.exception.BizException;

public class AccountNotFoundException extends BizException {
    public AccountNotFoundException(ErrorCode errorCode) {
        super(errorCode.getErrCode(), errorCode.getErrDesc());
    }
}
