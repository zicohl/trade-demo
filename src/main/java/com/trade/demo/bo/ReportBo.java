package com.trade.demo.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * @author zicohl
 * report
 */

@Data
@ToString
@NoArgsConstructor
public class ReportBo {
    private String template;
    private Map<String,String> imgMap;
    private Map<String,String> subTemplateMap;
}
