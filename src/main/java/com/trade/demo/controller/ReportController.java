package com.trade.demo.controller;

import com.trade.demo.api.ReportsApi;
import com.trade.demo.api.model.ReportVo;
import com.trade.demo.service.PositionService;
import com.trade.demo.service.report.JasperreportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Locale;

@Validated
@RestController
@Slf4j
public class ReportController implements ReportsApi {
    @Autowired
    private JasperreportService reportService;

    @Override
    public ResponseEntity<Resource> generateReport(@Valid @RequestBody ReportVo vo) {
        HttpHeaders headers = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment").filename(vo.getTemplatePath() + "." + vo.getExportType().toString().toLowerCase(Locale.ROOT)).build();
        headers.setContentDisposition(contentDisposition);
        byte[] data = reportService.generateReport(vo.getTemplatePath(),vo.getParameters(),vo.getDataSource(),vo.getExportType().toString());
        return new ResponseEntity<>(new ByteArrayResource(data),headers, HttpStatus.OK);
    }
}
