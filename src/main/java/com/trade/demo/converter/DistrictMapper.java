package com.trade.demo.converter;

import com.trade.demo.po.DistrictPo;
import com.trade.demo.vo.DistrictVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DistrictMapper {
    DistrictMapper INSTANCE = Mappers.getMapper(DistrictMapper.class);

    DistrictVo po2Vo(DistrictPo po) ;
}
