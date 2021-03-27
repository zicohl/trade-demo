package com.trade.demo.dao;


import com.trade.demo.po.PositionPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PositionDao {
    int insertPosition(@Param("po") PositionPo po);

    PositionPo findPositionByCode(@Param("securityCode") String securityCode);

    List<PositionPo> findPosition(@Param("securityCode") String securityCode);

    int updatePosition(@Param("po") PositionPo po);
}
