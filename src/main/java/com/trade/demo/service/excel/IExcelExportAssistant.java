package com.trade.demo.service.excel;

import java.util.Map;

/**
 * @author honglu
 * @since 2021/12/25
 */
public interface IExcelExportAssistant {
    long submitExportTask(String excelType, Map<String,Object> parameters);
}
