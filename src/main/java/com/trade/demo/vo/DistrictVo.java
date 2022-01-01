package com.trade.demo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
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
    private String name;
    private String nameZh;
    private String code;
    private String codePath;
    private int orderNumber;
    private Long createTime;
    private Date createTimeExport;
    List<DistrictVo> children;
}
