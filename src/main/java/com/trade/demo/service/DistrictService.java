package com.trade.demo.service;

import com.trade.demo.converter.DistrictMapper;
import com.trade.demo.dao.DistrictDao;
import com.trade.demo.po.DistrictPo;
import com.trade.demo.vo.DistrictVo;
import com.trade.demo.vo.PageVo;
import com.trade.demo.vo.Pager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DistrictService {
    @Autowired
    private DistrictDao districtDao;

    @Autowired
    private DistrictMapper districtMapper;

    public DistrictVo getPositionTree(String code) {
        List<DistrictPo> list = districtDao.findAllDistrict();
        DistrictVo root = districtMapper.po2Vo(list.get(0));
        for (DistrictPo districtPo : list) {
            if (districtPo.getCode().equals(code)) {
                continue;
            }
            fillDistrict(districtMapper.po2Vo(districtPo), root);
        }

        return root;
    }

    public Pager<DistrictPo> getPositions(PageVo pageVo) {
        Pager<DistrictPo> result = new Pager<>();
        result.setResults(districtDao.findPagedDistrict(0L, pageVo));
        result.setPageVo(pageVo);
        return result;
    }

    private void fillDistrict(DistrictVo district, DistrictVo root) {
        if (district.getParentId().equals(root.getId())) {
            if (root.getChildren() == null) {
                root.setChildren(new ArrayList<>());
            }
            root.getChildren().add(district);
        } else {
            if (CollectionUtils.isNotEmpty(root.getChildren())) {
                for (DistrictVo subDistrict : root.getChildren()) {
                    fillDistrict(district, subDistrict);
                }
            }
        }
    }

}
