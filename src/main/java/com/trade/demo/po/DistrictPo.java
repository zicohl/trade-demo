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
public class DistrictPo {
    private Long id;
    private Long parentId;
    private String code;
    private String name;
    private String nameZh;
    private int orderNmuber;
}
