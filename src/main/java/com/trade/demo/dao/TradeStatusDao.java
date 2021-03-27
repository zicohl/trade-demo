package com.trade.demo.dao;


import com.trade.demo.po.TradeStatusPo;
import org.apache.ibatis.annotations.Param;

public interface TradeStatusDao {
    int insertTradeStatus(@Param("po") TradeStatusPo po);

    TradeStatusPo findTradeStatusById(@Param("tradeId") Long id);

    int updateTradeStatus(@Param("po") TradeStatusPo po);
}
