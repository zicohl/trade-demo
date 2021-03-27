package com.trade.demo.converter;

import com.trade.demo.api.model.TradeVo;
import com.trade.demo.constant.TradeConstant;
import com.trade.demo.po.TradeStatusPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface TradeStatusMapper {
    TradeStatusMapper INSTANCE = Mappers.getMapper(TradeStatusMapper.class);

    default TradeStatusPo vo2Po(TradeVo vo) {
        TradeStatusPo po = new TradeStatusPo();
        po.setTradeId(vo.getTradeId());
        po.setVersion(vo.getVersion());
        po.setSecurityCode(vo.getSecurityCode());
        po.setQuantity(vo.getQuantity());
        if (vo.getTradeDirection()==TradeVo.TradeDirectionEnum.BUY) {
            po.setDirectionType(TradeConstant.TRADE_DIRECTION_BUY);
        } else {
            po.setDirectionType(TradeConstant.TRADE_DIRECTION_SELL);
        }
        return po;
    }

}
