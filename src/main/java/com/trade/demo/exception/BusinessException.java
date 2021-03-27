package com.trade.demo.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ResponseCode errorCode;
    private final Throwable throwable;
    private String detail;

    public BusinessException(ResponseCode errorCode) {
        this.errorCode = errorCode;
        this.throwable= null;
    }
    public BusinessException(ResponseCode errorCode,String detail) {
        this.errorCode = errorCode;
        this.throwable= null;
        this.detail = detail;
    }
    public BusinessException(ResponseCode errorCode,Throwable throwable) {
        this.errorCode = errorCode;
        this.throwable= throwable;
    }
}
