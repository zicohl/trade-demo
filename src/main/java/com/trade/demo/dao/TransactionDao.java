package com.trade.demo.dao;


import com.trade.demo.po.TransactionPo;
import org.apache.ibatis.annotations.Param;

public interface TransactionDao {
    int insertTransaction(@Param("po") TransactionPo po);
}
