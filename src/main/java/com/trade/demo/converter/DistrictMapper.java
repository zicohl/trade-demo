package com.trade.demo.converter;


import com.trade.demo.api.model.TradeVo;
import com.trade.demo.constant.TradeConstant;
import com.trade.demo.po.DistrictPo;
import com.trade.demo.po.TransactionPo;
import com.trade.demo.vo.DistrictVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper(componentModel = "spring")
public interface DistrictMapper {
    DistrictMapper INSTANCE = Mappers.getMapper(DistrictMapper.class);

    DistrictVo po2Vo(DistrictPo po) ;
}
