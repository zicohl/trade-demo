package com.trade.demo.service;

import com.trade.demo.converter.DistrictMapper;
import com.trade.demo.dao.DistrictDao;
import com.trade.demo.po.DistrictPo;
import com.trade.demo.vo.DistrictVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Slf4j
public class DistrictService {
    @Autowired
    private DistrictDao districtDao;

    @Autowired
    private DistrictMapper districtMapper;

    public List<DistrictPo> queryPosition(String code) {
        List<DistrictPo> list = districtDao.findAllDistrict();
        DistrictVo root = districtMapper.po2Vo(list.get(0));
        root.setCodePath(root.getCode());
        for (DistrictPo districtPo : list) {
            if (districtPo.getCode().equals(code)) {
                continue;
            }
            fillDistrict(districtMapper.po2Vo(districtPo), root);
        }
/*
        for (DistrictPo districtPo : list) {
            if (districtPo.getCode().equals(code)) {
                continue;
            }
            if (!findDistrit(root,districtPo)) {
                log.info("{} {}", districtPo.getCode(), districtPo.getNameZh());
            }
        }*/

        for (DistrictPo districtPo : list) {
            //log.info("{} {}", districtPo.getCode(), districtPo.getNameZh());
        }
        return list;
    }

    private boolean findDistrit(DistrictVo root, DistrictPo districtPo) {
        if (root.getCode().equals(districtPo.getCode())) {
            return true;
        }
        if (CollectionUtils.isNotEmpty(root.getChildren())) {
            for (DistrictVo districtVo:root.getChildren()) {
                if (findDistrit(districtVo,districtPo)) {
                    return true;
                }
            }
        }

        return false;
    }


    private void fillDistrict(DistrictVo district, DistrictVo root) {
        if (district.getParentId().equals(root.getId())) {
            if (root.getChildren() == null) {
                root.setChildren(new ArrayList<>());
            }
            district.setCodePath(root.getCodePath() + "." + district.getCode());
            district.setName(district.getName().substring(0,1).toUpperCase(Locale.ROOT)+district.getName().substring(1));
            root.getChildren().add(district);
            logSql(district);
        } else {
            if (CollectionUtils.isNotEmpty(root.getChildren())) {
                for (DistrictVo subDistrict : root.getChildren()) {
                    fillDistrict(district, subDistrict);
                }
            }
        }
    }

    private void logSql(DistrictVo district) {
        log.info("insert into `district` (`id`, `parent_id`, `name`, `name_zh`, `code`, `code_path`, `order_number`) " +
                "values({},{},{},{},{},{},{});", district.getId(),district.getParentId(),district.getName(), district.getNameZh(),
                district.getCode(),district.getCodePath(),district.getOrderNmuber());
    }

}
