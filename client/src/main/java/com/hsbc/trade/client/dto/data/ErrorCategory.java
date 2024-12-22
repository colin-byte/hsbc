package com.hsbc.trade.client.dto.data;

public enum ErrorCategory {
    /**
     * 接口请求参数异常
     */
    PARAMETER_INVALID,
    /**
     * 资源不存在，一般指某种领域模型
     */
    RESOURCE_NOT_FOUND,
    ;
}
