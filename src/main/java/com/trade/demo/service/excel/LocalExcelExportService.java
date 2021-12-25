package com.trade.demo.service.excel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
    public void submitExportTask(String excelType, Map<String, Object> parameters) {
        log.info("start export {}", excelType);
    }
}
