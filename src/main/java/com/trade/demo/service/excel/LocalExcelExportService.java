package com.trade.demo.service.excel;

import com.trade.demo.bo.ExcelExport;
import com.trade.demo.bo.ExcelExportColumn;
import com.trade.demo.bo.ExcelExportSheet;
import com.trade.demo.exception.BusinessException;
import com.trade.demo.exception.ResponseCode;
import com.trade.demo.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author honglu
 * @since 2021/12/19
 */
@Component
@Slf4j
public class LocalExcelExportService implements IExcelExportService {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    @Async("excelExportExecutor")
    public void submitExportTask(ExcelExport excelExport, Locale locale, Map<String, Object> parameters) {
        try {
            log.info("start export {}", excelExport.getExcelType());
            HSSFWorkbook workbook = new HSSFWorkbook();
            int index = 0;
            for (ExcelExportSheet sheet : excelExport.getSheets()) {
                HSSFSheet bookSheet = workbook.createSheet();
                workbook.setSheetName(index, sheet.getSheetName().get(locale));
                HSSFRow row = bookSheet.createRow(0);
                fillColumnName(workbook, row, locale, sheet.getColumns());

                PageVo pageVo = new PageVo();
                pageVo.setPageNumber(1);
                pageVo.setPageSize(sheet.getBatchSize());
                pageVo.setTotalRow(1);

                IExcelDataProvider dataProvider = (IExcelDataProvider) applicationContext.getBean(sheet.getConsumerBean());
                List results = dataProvider.getBatchData(pageVo, new HashMap<>());

                int dataIndex = 1;
                fillData(workbook, bookSheet, results, dataIndex, sheet);
                dataIndex += results.size();

                while (results.size() >= pageVo.getPageSize()) {
                    pageVo.setPageNumber(pageVo.getPageNumber() + 1);
                    results = dataProvider.getBatchData(pageVo, new HashMap<>());

                    fillData(workbook, bookSheet, results, dataIndex, sheet);
                    dataIndex += results.size();
                }

                index++;
            }

            FileOutputStream out = new FileOutputStream(excelExport.getFileName().get(locale) + "_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".xlsx");
            workbook.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            throw new BusinessException(ResponseCode.EXCEL_EXPORT_ERROR, "export excel error");
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.EXCEL_EXPORT_ERROR, "export excel error");
        } finally {
            log.info("end export {}", excelExport.getExcelType());
        }
    }

    private void fillData(HSSFWorkbook workbook, HSSFSheet bookSheet, List results, int dataIndex, ExcelExportSheet sheet) {
        Map<String, HSSFCellStyle> styleMap = new HashMap<>();
        for (ExcelExportColumn column : sheet.getColumns()) {
            if (column.getFormat() != null) {
                HSSFCellStyle style = workbook.createCellStyle();
                style.setDataFormat(workbook.createDataFormat().getFormat(column.getFormat()));
                styleMap.put(column.getFieldName(), style);
            }
        }
        try {
            Class voClass = Class.forName(sheet.getVoClassName());
            int index = 0;
            for (Object result : results) {
                HSSFRow row = bookSheet.createRow(dataIndex + index);
                for (int j = 0; j < sheet.getColumns().size(); j++) {
                    ExcelExportColumn column = sheet.getColumns().get(j);
                    PropertyDescriptor pd = new PropertyDescriptor(column.getFieldName(), voClass);
                    Method method = pd.getReadMethod();
                    Type type = method.getAnnotatedReturnType().getType();

                    HSSFCell cell = row.createCell(j);
                    Object data = method.invoke(result);
                    if (data == null) {
                        continue;
                    }
                    if ("boolean".equals(type.getTypeName()) || data instanceof Boolean) {
                        cell.setCellValue((Boolean) data);
                    } else if (isTypeOfNumber(type.getTypeName())) {
                        cell.setCellValue(new Double(data.toString()));
                    } else if (isObjectOfNumber(data)) {
                        cell.setCellValue(((Number) data).doubleValue());
                    } else if (data instanceof Date) {
                        cell.setCellValue((Date) data);
                    } else if (data instanceof Calendar) {
                        cell.setCellValue((Calendar) data);
                    } else {
                        cell.setCellValue(data.toString());
                    }

                    if (column.getFormat() != null) {
                        cell.setCellStyle(styleMap.get(column.getFieldName()));
                    }
                }
                index++;
            }
        } catch (ClassNotFoundException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            throw new BusinessException(ResponseCode.EXCEL_EXPORT_ERROR, "get vo class");
        }
    }

    private boolean isObjectOfNumber(Object data) {
        if (data instanceof Number) {
            return true;
        }
        return false;
    }

    private boolean isTypeOfNumber(String typeName) {
        if ("short".equals(typeName)) {
            return true;
        } else if ("int".equals(typeName)) {
            return true;
        } else if ("long".equals(typeName)) {
            return true;
        } else if ("float".equals(typeName)) {
            return true;
        } else if ("double".equals(typeName)) {
            return true;
        }
        return false;
    }

    private void fillColumnName(HSSFWorkbook workbook, HSSFRow row, Locale locale, List<ExcelExportColumn> columns) {
        int index = 0;
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        for (ExcelExportColumn column : columns) {
            HSSFCell cell = row.createCell(index);
            cell.setCellValue(column.getDisplayName().get(locale));
            cell.setCellStyle(style);
            index++;
        }
    }
}
