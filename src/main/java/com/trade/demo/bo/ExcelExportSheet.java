package com.trade.demo.bo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author honglu
 * @since 2021/12/22
 */
@Data
@ToString
@NoArgsConstructor
public class ExcelExportSheet {
    private Map<Locale, String> sheetName;
    private int batchSize;
    private String voClassName;
    private String consumerBean;
    private List<ExcelExportColumn> columns;
}
