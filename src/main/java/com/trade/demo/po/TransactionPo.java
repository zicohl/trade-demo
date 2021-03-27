package com.trade.demo.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * @author zicohl
 * 交易日志
 */

@Data
@ToString
@NoArgsConstructor
public class TransactionPo {
    private Long id;
    private Long tradeId;
    private Integer version;
    private String securityCode;
    private Integer quantity;
    private int operationType;
    private int directionType;
    private Long createTime;
}
