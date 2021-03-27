package com.trade.demo.converter;


import com.trade.demo.api.model.TradeVo;
import com.trade.demo.constant.TradeConstant;

import com.trade.demo.po.TransactionPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    default TransactionPo vo2Po(TradeVo vo) {
        TransactionPo po = new TransactionPo();
        po.setTradeId(vo.getTradeId());
        po.setVersion(vo.getVersion());
        po.setSecurityCode(vo.getSecurityCode());
        po.setQuantity(vo.getQuantity());
        if (vo.getTradeDirection() == TradeVo.TradeDirectionEnum.BUY) {
            po.setDirectionType(TradeConstant.TRADE_DIRECTION_BUY);
        } else {
            po.setDirectionType(TradeConstant.TRADE_DIRECTION_SELL);
        }
        po.setCreateTime(new Date().getTime() / 1000);

        return po;
    }

}
