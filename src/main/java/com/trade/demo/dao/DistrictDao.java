package com.trade.demo.dao;


import com.trade.demo.po.DistrictPo;
import com.trade.demo.vo.PageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DistrictDao {

    List<DistrictPo> findAllDistrict();

    List<DistrictPo> findPagedDistrict(@Param("parentId") Long parentId, @Param("pageVo") PageVo pageVo);

    int findPagedDistrictCount(@Param("parentId") Long parentId);
}
