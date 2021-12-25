package com.trade.demo.bo;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author honglu
 * @since 2021/12/22
 */
public class ExcelExportSheet {
    private Map<Locale, String> sheetName;
    private int batchSize;
    private String voClassName;
    private String consumerBean;
    private List<ExcelExportColumn> columns;
}
