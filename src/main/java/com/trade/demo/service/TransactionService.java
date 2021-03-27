package com.trade.demo.service;

import com.trade.demo.dao.TransactionDao;
import com.trade.demo.po.TransactionPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TransactionService {
    @Autowired
    private TransactionDao transactionDao;

    /**
     * 记录交易日志，不参与其他事务回滚
     * @param 交易日志
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logTransaction(TransactionPo po) {
        try {
            log.info("logTransaction {} {}",po.getTradeId(),po.getVersion());
            transactionDao.insertTransaction(po);
        }catch (Exception e) {
            log.error("ex",e);
        }
    }
}
