package com.trade.demo.service.report;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import java.io.ByteArrayOutputStream;

public enum ReportExportType implements IReportingExporter {

    PDF("PDF") {
        @SuppressWarnings("unchecked")
        @Override
        public JRAbstractExporter generateExporter(ByteArrayOutputStream bos) {
            JRAbstractExporter exporter = new JRPdfExporter();
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
            return exporter;
        }
    },
    XLS("XLS") {
        @SuppressWarnings("unchecked")
        @Override
        public JRAbstractExporter generateExporter(ByteArrayOutputStream bos) {
            JRAbstractExporter exporter = new JRXlsExporter();
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setDetectCellType(true);
            exporter.setConfiguration(configuration);
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
            return exporter;
        }
    },
    XLSX("XLSX") {
        @SuppressWarnings("unchecked")
        @Override
        public JRAbstractExporter generateExporter(ByteArrayOutputStream bos) {
            JRAbstractExporter exporter = new JRXlsExporter();
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setDetectCellType(true);
            exporter.setConfiguration(configuration);
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(bos));
            return exporter;
        }
    },
    HTML("HTML") {
        @SuppressWarnings("unchecked")
        @Override
        public JRAbstractExporter generateExporter(ByteArrayOutputStream bos) {
            JRAbstractExporter exporter = new HtmlExporter();
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(bos));
            return exporter;
        }
    },
    CSV("CSV") {
        @SuppressWarnings("unchecked")
        @Override
        public JRAbstractExporter generateExporter(ByteArrayOutputStream bos) {
            JRAbstractExporter exporter = new JRCsvExporter();
            exporter.setExporterOutput(new SimpleWriterExporterOutput(bos));
            return exporter;
        }
    };

    private String code;

    ReportExportType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static IReportingExporter getReportingExporterType(String code) {

        for (ReportExportType type : values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }

        return null;
    }
}
