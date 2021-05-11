package com.trade.demo.service.report;

import net.sf.jasperreports.engine.JRAbstractExporter;

import java.io.ByteArrayOutputStream;

public interface IReportingExporter {
    JRAbstractExporter generateExporter(ByteArrayOutputStream bos);
}
