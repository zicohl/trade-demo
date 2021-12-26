package com.trade.demo.service.excel;

import com.trade.demo.bo.ExcelExport;
import com.trade.demo.bo.ExcelExportColumn;
import com.trade.demo.bo.ExcelExportSheet;
import com.trade.demo.exception.BusinessException;
import com.trade.demo.exception.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author honglu
 * @since 2021/12/25
 */
@Service
@Slf4j
public class LocalExcelExportAssistant implements IExcelExportAssistant {
    private static final String SUFFIX = ".excelExport.xml";

    private Map<String, ExcelExport> excelExportMap = new ConcurrentHashMap<>();

    @Autowired
    private IExcelExportService excelExportService;

    @Value("${excel.export.template}")
    private String exportTemplate;

    @Override
    public long submitExportTask(String excelType, Map<String, Object> parameters) {
        ExcelExport excelExport = getExcelExportDefine(excelType);
        excelExportService.submitExportTask(excelExport, parameters);
        return 0;
    }

    private ExcelExport getExcelExportDefine(String excelType) {
        ExcelExport excelExportBo = excelExportMap.get(excelType);
        if (excelExportBo != null) {
            return excelExportBo;
        }
        return loadExcelExportDefine(excelType);
    }

    private ExcelExport loadExcelExportDefine(String excelType) {
        ExcelExport excelExport = new ExcelExport();
        excelExport.setExcelType(excelType);
        try {
            ClassPathResource classPathResource = new ClassPathResource(exportTemplate + excelType + SUFFIX);
            InputStream is = classPathResource.getInputStream();

            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = documentBuilder.parse(is);

            Element root = doc.getDocumentElement();
            NamedNodeMap attrs = root.getAttributes();
            Node fileName = attrs.getNamedItem("fileName");
            excelExport.setFileName(parseLocaleName(fileName.getNodeValue()));

            NodeList sheets = root.getChildNodes();
            excelExport.setSheets(new ArrayList<>());

            for (int i = 0; i < sheets.getLength(); i++) {
                Node sheet = sheets.item(i);

                if (sheet.getNodeType() == Node.ELEMENT_NODE) {
                    ExcelExportSheet excelExportSheet = new ExcelExportSheet();
                    NamedNodeMap sheetAttrs = sheet.getAttributes();
                    excelExportSheet.setSheetName(parseLocaleName(sheetAttrs.getNamedItem("displayName").getNodeValue()));
                    excelExportSheet.setBatchSize(Integer.parseInt(sheetAttrs.getNamedItem("batchSize").getNodeValue()));
                    excelExportSheet.setConsumerBean(sheetAttrs.getNamedItem("consumerBean").getNodeValue());
                    excelExportSheet.setVoClassName(sheetAttrs.getNamedItem("voClassName").getNodeValue());
                    excelExportSheet.setColumns(new ArrayList<>());

                    NodeList columns = sheet.getChildNodes();
                    for (int j = 0; j < columns.getLength(); j++) {
                        Node column = columns.item(j);

                        if (column.getNodeType() == Node.ELEMENT_NODE) {
                            ExcelExportColumn excelExportColumn = new ExcelExportColumn();
                            NamedNodeMap columnAttrs = column.getAttributes();
                            excelExportColumn.setDisplayName(parseLocaleName(columnAttrs.getNamedItem("displayName").getNodeValue()));
                            excelExportColumn.setFieldName(columnAttrs.getNamedItem("fieldName").getNodeValue());
                            excelExportColumn.setFormat(columnAttrs.getNamedItem("format").getNodeValue());

                            excelExportSheet.getColumns().add(excelExportColumn);
                        }
                    }

                    excelExport.getSheets().add(excelExportSheet);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error("loadExcelExportDefine", e);
            throw new BusinessException(ResponseCode.EXCEL_EXPORT_ERROR, "load excel definition error");
        }
        excelExportMap.put(excelType, excelExport);
        return excelExport;
    }

    private Map<Locale, String> parseLocaleName(String value) {
        Map<Locale, String> localeMap = new HashMap<>();
        String[] localeValues = value.split(",");
        for (String localeValue : localeValues) {
            String[] localeKeyValue = localeValue.split("=");
            String[] localeNames = localeKeyValue[0].split("_");
            localeMap.put(localeNames.length == 1 ? new Locale(localeNames[0].toLowerCase(Locale.ROOT)) : new Locale(localeNames[0].toLowerCase(Locale.ROOT), localeNames[1].toLowerCase(Locale.ROOT)), localeKeyValue[1]);
        }
        return localeMap;
    }
}
