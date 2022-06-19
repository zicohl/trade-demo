package com.trade.demo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author honglu
 * @since 2021/10/24
 */

@Data
@ToString
@NoArgsConstructor
@ApiModel(description = "订单操作")
public class OrderOperationVo {
    @ApiModelProperty(required = true, value = "订单号")
    private String orderNum;

    @ApiModelProperty(required = true, value = "操作")
    private String action;

}
