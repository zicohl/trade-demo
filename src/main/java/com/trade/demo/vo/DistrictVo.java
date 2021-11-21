package com.trade.demo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author zicohl
 * 仓位
 */

@Data
@ToString
@NoArgsConstructor
public class DistrictVo {
    private Long id;
    private Long parentId;
    private String code;
    private String name;
    private String nameZh;
    private int orderNmuber;
    private String codePath;
    List<DistrictVo> children;
}
