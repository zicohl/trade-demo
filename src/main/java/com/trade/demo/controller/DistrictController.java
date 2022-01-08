package com.trade.demo.controller;

import com.trade.demo.annotation.RequiresPermissions;
import com.trade.demo.po.DistrictPo;
import com.trade.demo.service.DistrictService;
import com.trade.demo.service.excel.IExcelExportAssistant;
import com.trade.demo.service.excel.IExcelExportService;
import com.trade.demo.vo.DistrictVo;
import com.trade.demo.vo.PageVo;
import com.trade.demo.vo.Pager;
import com.trade.demo.vo.ResponseResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Validated
@RestController
@Slf4j
@Api(value = "行政区域服务")
public class DistrictController {
    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private IExcelExportAssistant excelExportAssistant;

    @ApiOperation(value = "获取行政区域树", nickname = "queryDistrictTree", notes = "获取行政区域树", tags = {"行政区域服务",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"), @ApiResponse(code = 404, message = "Not found")})
    @RequestMapping(value = "/districts/tree", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public ResponseEntity<ResponseResultVo<DistrictVo>> queryDistrictTree(@ApiParam(value = "地区代码") @RequestParam(value = "districtCode", required = false) String districtCode) {
        DistrictVo district = districtService.getDistrictTree(districtCode);

        ResponseResultVo<DistrictVo> result = new ResponseResultVo<>();
        result.setStatus("success");
        result.setData(district);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ApiOperation(value = "获取行政区域列表", nickname = "queryDistrict", notes = "获取行政区域列表", tags = {"行政区域服务",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"), @ApiResponse(code = 404, message = "Not found")})
    @RequestMapping(value = "/districts", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    @RequiresPermissions({"districtmgmt"})
    public ResponseEntity<ResponseResultVo<List<DistrictPo>>> queryDistrict(@ApiParam(value = "page number") @RequestParam(value = "pageNumber", required = true) int pageNumber, @ApiParam(value = "page size") @RequestParam(value = "pageSize", required = true) int pageSize) {
        PageVo pageVo = new PageVo();
        pageVo.setPageNumber(pageNumber);
        pageVo.setPageSize(pageSize);
        Pager<DistrictPo> pagedData = districtService.getDistricts(0L, pageVo);

        ResponseResultVo<List<DistrictPo>> result = new ResponseResultVo<>();
        result.setStatus("success");
        result.setData(pagedData.getResults());
        result.setPageVo(pagedData.getPageVo());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @ApiOperation(value = "导出行政区域", nickname = "exportDistrict", notes = "导出行政区域", tags = {"行政区域服务",})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"), @ApiResponse(code = 404, message = "Not found")})
    @RequestMapping(value = "/districts/export", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public ResponseEntity<ResponseResultVo<Long>> exportDistrict(@ApiParam(value = "parent Id") @RequestParam(value = "parentId", required = true) long parentId) {
        ResponseResultVo<Long> result = new ResponseResultVo<>();
        result.setStatus("success");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("parentId", parentId);
        result.setData(excelExportAssistant.submitExportTask("district", new Locale("zh", "cn"), parameters));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
