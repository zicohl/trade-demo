package com.trade.demo.controller;

import com.trade.demo.api.PositionsApi;
import com.trade.demo.api.model.ResponseResultVo;
import com.trade.demo.po.PositionPo;
import com.trade.demo.service.PositionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@Slf4j
public class PositionController implements PositionsApi {
    @Autowired
    private PositionService positionService;

    @Override
    public ResponseEntity<ResponseResultVo> queryPosition(@RequestParam(value = "securityCode", required = false)  String securityCode) {
        List<PositionPo> list = positionService.queryPosition(securityCode);
        ResponseResultVo result = new ResponseResultVo();
        result.setResult(list);
        result.setStatus("success");
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
}
