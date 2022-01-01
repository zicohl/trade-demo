package com.trade.demo.service;

import com.trade.demo.converter.DistrictMapper;
import com.trade.demo.po.DistrictPo;
import com.trade.demo.service.excel.IExcelDataProvider;
import com.trade.demo.vo.DistrictVo;
import com.trade.demo.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author honglu
 * @since 2021/12/25
 */
@Component("IExcelDataProvider.District")
public class DistrictExportDataProvider implements IExcelDataProvider<DistrictVo> {
    @Autowired
    private DistrictService districtService;

    @Autowired
    private DistrictMapper districtMapper;

    @Override
    public List<DistrictVo> getBatchData(PageVo pageVo, Map<String, Object> parameters) {
        return districtMapper.po2VoExport(districtService.getDistricts(0L, pageVo).getResults());
    }
}
