package com.trade.demo.exception;


import com.trade.demo.api.model.ResponseResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常返回处理
 */
@ControllerAdvice
@Slf4j
public class GloablDefaultExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<?> defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error("Exception", e);
        return new ResponseEntity<>(ExceptionHandlerUtil.defaultErrorHandler(e), HttpStatus.OK);
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseEntity<?> businessErrorHandler(HttpServletRequest req, BusinessException e) {
        log.error("BusinessException", e);
        ResponseResultVo vo = new ResponseResultVo();
        vo.setStatus("failed");
        vo.setCode(e.getErrorCode().getCode());
        vo.setMessage(e.getErrorCode().getMessage());
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }
}
