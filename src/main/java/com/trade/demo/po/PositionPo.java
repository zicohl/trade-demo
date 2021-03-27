package com.trade.demo.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zicohl
 * 仓位
 */

@Data
@ToString
@NoArgsConstructor
public class PositionPo {
    private String securityCode;
    private Integer quantity;
}
