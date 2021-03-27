package com.trade.demo.controller;

import com.trade.demo.api.TradesApi;
import com.trade.demo.api.model.ResponseResultVo;
import com.trade.demo.api.model.TradeVo;
import com.trade.demo.service.TradeService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@Slf4j
public class TradeController implements TradesApi {
    @Autowired
    private TradeService tradeService;

    @Override
    public ResponseEntity<ResponseResultVo> createTrade(@Valid @RequestBody TradeVo vo) {
        tradeService.createTrade(vo);
        ResponseResultVo result = new ResponseResultVo();
        result.setStatus("success");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }

    @Override
    public ResponseEntity<ResponseResultVo> updateTrade(@ApiParam(value = "交易号",required=true,example = "1") @PathVariable("tradeId") Long tradeId, @Valid @RequestBody TradeVo vo) {
        tradeService.updateTrade(vo);
        ResponseResultVo result = new ResponseResultVo();
        result.setStatus("success");
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @Override
    public ResponseEntity<ResponseResultVo> cancelTrade(@ApiParam(value = "交易号",required=true,example = "1") @PathVariable("tradeId") Long tradeId, @Valid @RequestBody TradeVo vo) {
        tradeService.cancelTrade(vo);
        ResponseResultVo result = new ResponseResultVo();
        result.setStatus("success");
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
}
