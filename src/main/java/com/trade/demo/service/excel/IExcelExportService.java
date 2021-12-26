package com.trade.demo.service.excel;

import com.trade.demo.bo.ExcelExport;

import java.util.Locale;
import java.util.Map;

/**
 * @author honglu
 * @since 2021/12/19
 */
public interface IExcelExportService {
    void submitExportTask(ExcelExport excelExport, Locale locale, Map<String,Object> parameters);
}
