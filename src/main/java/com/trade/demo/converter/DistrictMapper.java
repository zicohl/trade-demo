package com.trade.demo.converter;

import com.trade.demo.po.DistrictPo;
import com.trade.demo.vo.DistrictVo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DistrictMapper {
    DistrictMapper INSTANCE = Mappers.getMapper(DistrictMapper.class);

    DistrictVo po2Vo(DistrictPo po);

    @Named(value = "po2VoExport")
    @Mapping(target = "createTimeExport", expression = "java(com.trade.demo.util.DateUtil.utcToDate(po.getCreateTime()))")
    DistrictVo po2VoExport(DistrictPo po);

    @IterableMapping(qualifiedByName = "po2VoExport")
    List<DistrictVo> po2VoExport(List<DistrictPo> user);
}
