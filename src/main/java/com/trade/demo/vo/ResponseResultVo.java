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
@ApiModel(description = "响应信息")
public class ResponseResultVo<T> {
    @ApiModelProperty(required = true, value = "响应码")
    private String code;

    @ApiModelProperty(value = "响应信息")
    private String message;

    @ApiModelProperty(value = "响应详情")
    private String detail;

    @ApiModelProperty(required = true, value = "响应状态")
    private String status;

    @ApiModelProperty(value = "响应数据")
    private T data;

    @ApiModelProperty(value = "分页信息")
    private PageVo pageVo;
}
