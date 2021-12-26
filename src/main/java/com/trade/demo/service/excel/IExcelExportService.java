package com.trade.demo.service.excel;

import com.trade.demo.bo.ExcelExport;

import java.util.Map;

/**
 * @author honglu
 * @since 2021/12/19
 */
public interface IExcelExportService {
    void submitExportTask(ExcelExport excelExport, Map<String,Object> parameters);
}
