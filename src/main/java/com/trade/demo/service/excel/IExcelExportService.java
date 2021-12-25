package com.trade.demo.service.excel;

import java.util.Map;

/**
 * @author honglu
 * @since 2021/12/19
 */
public interface IExcelExportService {
    void submitExportTask(String excelType, Map<String,Object> parameters);
}
