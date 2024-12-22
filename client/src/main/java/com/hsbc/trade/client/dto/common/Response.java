package com.hsbc.trade.client.dto.common;

import lombok.Getter;

@Getter
public class Response<T> extends DTO {

    private static final long serialVersionUID = 1L;


    private boolean success;

    private String errCode;

    private String errMessage;

    private String requestId;

    private T data;

    public static <T> Response buildSuccess(T data) {
        Response response = new Response();
        response.setSuccess(true);
        response.data = data;
        return response;
    }

    public static Response buildSuccess() {
        Response response = new Response();
        response.setSuccess(true);
        return response;
    }

    public static Response buildFailure(String errCode, String errMessage) {
        Response response = new Response();
        response.setSuccess(false);
        response.setErrCode(errCode);
        response.setErrMessage(errMessage);
        return response;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "Response [success=" + success + ", errCode=" + errCode + ", errMessage=" + errMessage + "]";
    }

}
