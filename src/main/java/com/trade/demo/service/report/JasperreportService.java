package com.trade.demo.service.report;


import com.trade.demo.bo.ReportBo;
import com.trade.demo.exception.BusinessException;
import com.trade.demo.exception.ResponseCode;
import io.netty.buffer.ByteBufInputStream;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class JasperreportService {
    private static final String CLASSPATH_URL_PREFIX = "classpath:reports";
    private static final String REPORT_DEFINITION = "report.xml";
    private static final String REPORT_NODE_TEMPLATE = "template";
    private static final String REPORT_NODE_IMAGE = "img";
    private static final String REPORT_ATTR_NAME = "name";
    private static final String REPORT_ATTR_SRC = "src";

    private Map<String, JasperReport> jasperReportCache = new ConcurrentHashMap<>();

    @Autowired
    ResourceLoader resourceLoader;

    public byte[] generateReport(String reportUrl, Map<String, Object> parameters, Collection<Object> dataSource, String fileType) {
        if (parameters == null) {
            parameters = new HashMap<>();
        }

        if (fileType.equals("HTML") || fileType.equals("XLS") || fileType.equals("XLSX")) {
            parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
        }

        ReportBo reportBo = getReportDefinition(reportUrl);

        InputStream reportIs = null;
        try (ZipInputStream zis = new ZipInputStream(resourceLoader.getResource(CLASSPATH_URL_PREFIX + File.separator + reportUrl).getInputStream())) {
            ZipEntry nextEntry = zis.getNextEntry();
            while (nextEntry != null) {
                String fileName = nextEntry.getName();
                if (fileName.equals(REPORT_DEFINITION)) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(new Long(nextEntry.getSize()).intValue());
                    int n;
                    byte[] bytes = new byte[1024];
                    while ((n = zis.read(bytes)) != -1) {
                        byteArrayOutputStream.write(bytes, 0, n);
                    }
                    if (fileName.equals(reportBo.getTemplate())) {
                        reportIs = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    } else if (reportBo.getImgMap().containsKey(fileName)) {
                        parameters.put(reportBo.getImgMap().get(fileName), new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                    }
                }

                zis.closeEntry();
                nextEntry = zis.getNextEntry();
            }
        } catch (IOException e) {
            log.error("Exception", e.getMessage());
            throw new BusinessException(ResponseCode.REPORT_COMPILE_ERROR);
        }


        JasperReport jasperReport = getJasperReport(reportUrl, reportIs);
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

    private ReportBo getReportDefinition(String reportUrl) {
        ReportBo reportBo = new ReportBo();
        reportBo.setImgMap(new HashMap<>());
        reportBo.setSubTemplateMap(new HashMap<>());

        try (ZipInputStream zis = new ZipInputStream(resourceLoader.getResource(CLASSPATH_URL_PREFIX + File.separator + reportUrl).getInputStream())) {
            ZipEntry nextEntry = zis.getNextEntry();
            while (nextEntry != null) {
                String fileName = nextEntry.getName();
                if (fileName.equals(REPORT_DEFINITION)) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(new Long(nextEntry.getSize()).intValue());
                    int n;
                    byte[] bytes = new byte[1024];
                    while ((n = zis.read(bytes)) != -1) {
                        byteArrayOutputStream.write(bytes, 0, n);
                    }

                    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                    Node root = document.getFirstChild();
                    NodeList childNodes = root.getChildNodes();
                    Pattern pattern = Pattern.compile("#");
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        Node item = childNodes.item(i);
                        if (!pattern.matcher(item.getNodeName()).find()) {
                            NamedNodeMap attributes = item.getAttributes();
                            if (item.getNodeName().equals(REPORT_NODE_TEMPLATE)) {
                                Node attrSrc = attributes.getNamedItem(REPORT_ATTR_SRC);
                                reportBo.setTemplate(attrSrc.getNodeValue());
                            } else if (item.getNodeName().equals(REPORT_NODE_IMAGE)) {
                                Node attrName = attributes.getNamedItem(REPORT_ATTR_NAME);
                                Node attrSrc = attributes.getNamedItem(REPORT_ATTR_SRC);
                                reportBo.getImgMap().put(attrSrc.getNodeValue(), attrName.getNodeValue());
                            }
                        }
                    }

                    zis.closeEntry();
                    break;
                }

                zis.closeEntry();
                nextEntry = zis.getNextEntry();
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            log.error("Exception", e.getMessage());
            throw new BusinessException(ResponseCode.REPORT_COMPILE_ERROR);
        }

        return reportBo;
    }

    private JasperReport getJasperReport(String reportUrl, InputStream reportIs) {
        JasperReport report = jasperReportCache.get(reportUrl);
        if (report != null) {
            return report;
        }
        try {
            report = JasperCompileManager.compileReport(reportIs);
            jasperReportCache.put(reportUrl, report);
            return report;
        } catch (JRException e) {
            log.error("JRException", e);
            throw new BusinessException(ResponseCode.REPORT_COMPILE_ERROR);
        } finally {
            try {
                reportIs.close();
            } catch (IOException e) {
                throw new BusinessException(ResponseCode.REPORT_COMPILE_ERROR);
            }
        }
    }
}
