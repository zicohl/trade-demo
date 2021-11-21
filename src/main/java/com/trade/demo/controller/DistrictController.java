package com.trade.demo.controller;

import com.trade.demo.exception.BusinessException;
import com.trade.demo.exception.ResponseCode;
import com.trade.demo.po.PositionPo;
import com.trade.demo.service.DistrictService;
import com.trade.demo.service.PositionService;
import com.trade.demo.vo.ResponseResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import liquibase.pro.packaged.A;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Validated
@RestController
@Slf4j
@Api(value = "行政区域服务")
public class DistrictController {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DistrictService districtService;

    @ApiOperation(value = "行政区域服务", nickname = "queryDistrict", notes = "行政区域树查询", tags = {"行政区域服务",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not found")})
    @RequestMapping(value = "/districts",
            produces = {"application/json;charset=UTF-8"},
            method = RequestMethod.GET)
    public ResponseEntity<ResponseResultVo<List<Area1>>> queryDistrict(@ApiParam(value = "地区代码") @RequestParam(value = "district_code", required = false) String securityCode) {
        districtService.queryPosition("100000");
        //List<Area1> list1 = getSourceList();
        //conpareArea(list1);

        ResponseResultVo<List<Area1>> result = new ResponseResultVo<>();
        result.setStatus("success");
        // result.setData(list1);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    private void conpareArea(List<Area1> list) {
        Resource resource = resourceLoader.getResource("classpath:area.sql");
        try {
            InputStreamReader isr = new InputStreamReader(resource.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                for (Area1 area : list) {
                    if (line.contains(area.getCode())) {
                        area.setSql(line);
                        break;
                    }
                }
                //log.info("{}", line);
            }
            isr.close();
            br.close();
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.INTERNAL_ERROR, "get district error");
        }
        for (Area1 area : list) {
            log.info("{} {} {}", area.getSql(), area.getSql() == null ? area.getCode() : "", area.getSql() == null ? area.getName() : "");
        }
    }

    private List<Area1> getSourceList() {
        List<Area1> list = new ArrayList<>();
        Resource resource = resourceLoader.getResource("classpath:area.txt");
        try {
            InputStreamReader isr = new InputStreamReader(resource.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                String code = line.substring(0, 6);
                String name = line.substring(6).trim();
                Area1 area1 = new Area1();
                area1.setCode(code);
                area1.setName(name);
                list.add(area1);
                //log.info("[{}] [{}]", code, name);
            }
            isr.close();
            br.close();
        } catch (IOException e) {
            throw new BusinessException(ResponseCode.INTERNAL_ERROR, "get district error");
        }
        return list;
    }

    public class Area1 {
        private String code;
        private String name;
        private String sql;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        @Override
        public String toString() {
            return "Area1{" +
                    "code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", sql='" + sql + '\'' +
                    '}';
        }
    }
}
