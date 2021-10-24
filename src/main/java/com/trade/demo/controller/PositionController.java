package com.trade.demo.controller;

import com.trade.demo.po.PositionPo;
import com.trade.demo.service.PositionService;
import com.trade.demo.vo.ResponseResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@Slf4j
@Api(value = "仓位服务")
public class PositionController {
    @Autowired
    private PositionService positionService;

    @ApiOperation(value = "仓位查询", nickname = "queryPosition", notes = "仓位查询", tags = {"仓位服务",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found")})
    @RequestMapping(value = "/positions",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public ResponseEntity<ResponseResultVo<List<PositionPo>>> queryPosition(@ApiParam(value = "交易代码") @RequestParam(value = "securityCode", required = false) String securityCode) {
        List<PositionPo> list = positionService.queryPosition(securityCode);
        ResponseResultVo<List<PositionPo>> result = new ResponseResultVo<>();
        result.setData(list);
        result.setStatus("success");
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
}
