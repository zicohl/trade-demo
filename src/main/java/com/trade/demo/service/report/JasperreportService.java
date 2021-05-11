package com.trade.demo.service.report;


import com.trade.demo.exception.BusinessException;
import com.trade.demo.exception.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class JasperreportService {
    private Map<String, JasperReport> jasperReportCache = new ConcurrentHashMap<>();

    public byte[] createReport(String reportUrl, Map<String, Object> parameters, Collection<Object> dataSource, String fileType) {
        JasperReport jasperReport = getJasperReport(reportUrl);
        if (jasperReport == null) {
            throw new BusinessException(ResponseCode.REPORT_COMPILE_ERROR);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] result = null;
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(dataSource,
                    false));

            IReportingExporter exporterType = ReportExportType.getReportingExporterType(fileType);
            JRAbstractExporter exporter = exporterType.generateExporter(bos);
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

            exporter.exportReport();
            result = bos.toByteArray();
        } catch (JRException e) {
            log.error("JRException", e);
            throw new BusinessException(ResponseCode.REPORT_COMPILE_ERROR);
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                throw new BusinessException(ResponseCode.REPORT_COMPILE_ERROR);
            }
        }
        return result;
    }

    private JasperReport getJasperReport(String reportUrl) {
        JasperReport report = jasperReportCache.get(reportUrl);
        if (report != null) {
            return report;
        }
        try {
            report = JasperCompileManager.compileReport(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream(reportUrl));
            jasperReportCache.put(reportUrl, report);
            return report;
        } catch (JRException e) {
            log.error("JRException", e);
            throw new BusinessException(ResponseCode.REPORT_COMPILE_ERROR);
        }
    }
}
