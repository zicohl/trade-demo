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
@ApiModel(description = "分页信息")
public class PageVo {
    @ApiModelProperty(required = true, value = "当前页")
    private Integer curPage;

    @ApiModelProperty(required = true, value = "分页size")
    private Integer pageSize ;

    private Integer startIndex ;

    private Integer endIndex ;

    private Integer totalRow ;

    private Integer totalPage;
}
