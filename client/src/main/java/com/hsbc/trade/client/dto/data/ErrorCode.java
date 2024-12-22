package com.hsbc.trade.client.dto.data;

import org.apache.commons.text.CaseUtils;

public enum ErrorCode {
    ACCOUNT_NOT_FOUND(ErrorCategory.RESOURCE_NOT_FOUND, "账户不存在"),
    USER_NAME_OR_PASSWORD_INCORRECT(ErrorCategory.PARAMETER_INVALID, "用户名或密码不正确"),

    REQUIRED_PARAMETER_MISSING(ErrorCategory.PARAMETER_INVALID, "缺少必填参数"),
    PARAMETER_VALUE_INVALID(ErrorCategory.PARAMETER_INVALID, "参数值非法"),
    AWAIT_TO_RETRY(ErrorCategory.PARAMETER_INVALID, "等待重试");

    private final ErrorCategory errorCategory;
    private final String errDesc;

    ErrorCode(ErrorCategory errorCategory, String errDesc) {
        this.errorCategory = errorCategory;
        this.errDesc = errDesc;
    }

    public String getErrCode() {
        return CaseUtils.toCamelCase(errorCategory.name(), true, '_') +
                "." + CaseUtils.toCamelCase(name(), true, '_');
    }

    public String getErrDesc() {
        return errDesc;
    }
}
