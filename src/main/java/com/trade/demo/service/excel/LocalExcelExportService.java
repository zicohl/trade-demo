package com.trade.demo.service.excel;

import com.trade.demo.bo.ExcelExport;
import com.trade.demo.bo.ExcelExportSheet;
import com.trade.demo.exception.BusinessException;
import com.trade.demo.exception.ResponseCode;
import com.trade.demo.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author honglu
 * @since 2021/12/19
 */
@Component
@Slf4j
public class LocalExcelExportService implements IExcelExportService {
    @Override
    @Async("excelExportExecutor")
    public void submitExportTask(ExcelExport excelExport, Locale locale, Map<String, Object> parameters) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        log.info("start export {}", excelExport.getExcelType());
        int index = 0;
        for (ExcelExportSheet sheet : excelExport.getSheets()) {
            HSSFSheet bookSheet = workbook.createSheet();
            workbook.setSheetName(index, sheet.getSheetName().get(locale));
            index++;
        }

        FileOutputStream out;
        try {
            out = new FileOutputStream(excelExport.getFileName().get(locale) + "_" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".xlsx");
            workbook.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            throw new BusinessException(ResponseCode.EXCEL_EXPORT_ERROR, "export excel error");
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.EXCEL_EXPORT_ERROR, "export excel error");
        }
    }
}
