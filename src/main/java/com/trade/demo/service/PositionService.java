package com.trade.demo.service;

import com.trade.demo.dao.PositionDao;
import com.trade.demo.po.PositionPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PositionService {
    @Autowired
    private PositionDao positionDao;

    /**
     * 查询仓位
     * @param securityCode 交易代码
     * @return 仓位列表
     */
    public List<PositionPo> queryPosition(String securityCode) {
       return positionDao.findPosition(securityCode);
    }
}
