package com.trade.demo.service.excel;

import com.trade.demo.bo.ExcelExportBo;
import com.trade.demo.exception.BusinessException;
import com.trade.demo.exception.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author honglu
 * @since 2021/12/25
 */
@Service
@Slf4j
public class LocalExcelExportAssistant implements IExcelExportAssistant{
    private static final String SUFFIX = ".excelExport.xml";

    private Map<String, ExcelExportBo> excelExportMap = new ConcurrentHashMap<>();

    @Autowired
    private IExcelExportService excelExportService;

    @Value("${excel.export.template}")
    private String exportTemplate;

    @Override
    public long submitExportTask(String excelType, Map<String, Object> parameters) {
        getExcelExportDefine(excelType);
        excelExportService.submitExportTask(excelType,parameters);
        return 0;
    }

    private ExcelExportBo getExcelExportDefine(String excelType) {
        ExcelExportBo excelExportBo = excelExportMap.get(excelType);
        if (excelExportBo != null) {
            return excelExportBo;
        }
        return loadExcelExportDefine(excelType);
    }

    private ExcelExportBo loadExcelExportDefine(String excelType) {
        ExcelExportBo excelExportBo = new ExcelExportBo();
        try {
            ClassPathResource classPathResource = new ClassPathResource(exportTemplate + excelType + SUFFIX);
            InputStream is = classPathResource.getInputStream();

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = documentBuilder.parse(is);

            Element root = doc.getDocumentElement();
            NamedNodeMap attrs = root.getAttributes();
            NodeList sheets = root.getChildNodes();
            for (int i = 0; i < sheets.getLength(); i++) {
                Node sheet = sheets.item(i);
                if (sheet.getNodeType() == Node.ELEMENT_NODE) {
                    log.info("{} {}", sheet, sheet.getNodeType());
                } else {
                    log.info("{} {}", sheet, sheet.getNodeType());
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error("loadExcelExportDefine", e);
            throw new BusinessException(ResponseCode.EXCEL_EXPORT_ERROR, "load excel definition error");
        }
        excelExportMap.put(excelType, excelExportBo);
        return excelExportBo;
    }
}
