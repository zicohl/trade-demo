package com.trade.demo.exception;


import com.trade.demo.api.model.ResponseResultVo;

public class ExceptionHandlerUtil {
    private ExceptionHandlerUtil() {

    }

    public static ResponseResultVo defaultErrorHandler(Exception e) {
        ResponseResultVo response = new ResponseResultVo();
        response.setStatus("failed");
        response.setCode(ResponseCode.INTERNAL_ERROR.getCode());
        response.setMessage(ResponseCode.INTERNAL_ERROR.getMessage());
        return response;
    }
}
