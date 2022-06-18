package com.trade.demo.controller;

import com.trade.demo.service.order.OrderService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@Slf4j
@Api(value = "订单服务")
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "创建订单", nickname = "create order", notes = "创建订单", tags = {"订单服务",})
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK", response = ResponseResultVo.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found")})
    @RequestMapping(value = "",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<ResponseResultVo<String>> createOrder(@ApiParam(value = "订单号") @Valid @RequestBody String orderNum) {
        orderService.createOrder(orderNum);

        ResponseResultVo<String> result = new ResponseResultVo<>();
        result.setStatus("success");
        result.setData(orderNum);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @ApiOperation(value = "完成订单", nickname = "complete order", notes = "完成订单", tags = {"订单服务",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ResponseResultVo.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found")})
    @RequestMapping(value = "",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    public ResponseEntity<ResponseResultVo<String>> completeOrder(@ApiParam(value = "订单号") @Valid @RequestBody String orderNum) {
        orderService.completeOrder(orderNum);

        ResponseResultVo<String> result = new ResponseResultVo<>();
        result.setStatus("success");
        result.setData(orderNum);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


}
