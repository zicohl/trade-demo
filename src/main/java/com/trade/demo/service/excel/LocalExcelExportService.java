package com.trade.demo.service.excel;

import com.trade.demo.bo.ExcelExportBo;
import com.trade.demo.exception.BusinessException;
import com.trade.demo.exception.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
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
 * @since 2021/12/19
 */
@Component
@Slf4j
public class LocalExcelExportService implements IExcelExportService {
    @Override
    @Async("excelExportExecutor")
    public void submitExportTask(String excelType, Map<String, Object> parameters) {
        log.info("submitExportTask");
    }
}
