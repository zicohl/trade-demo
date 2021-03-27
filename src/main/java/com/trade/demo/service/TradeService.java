package com.trade.demo.service;

import com.trade.demo.api.model.TradeVo;
import com.trade.demo.constant.TradeConstant;
import com.trade.demo.converter.TradeStatusMapper;
import com.trade.demo.converter.TransactionMapper;
import com.trade.demo.dao.PositionDao;
import com.trade.demo.dao.TradeStatusDao;
import com.trade.demo.exception.BusinessException;
import com.trade.demo.exception.ResponseCode;
import com.trade.demo.po.PositionPo;
import com.trade.demo.po.TradeStatusPo;
import com.trade.demo.po.TransactionPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TradeService {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TradeStatusDao tradeStatusDao;

    @Autowired
    private PositionDao positionDao;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TradeStatusMapper tradeStatusMapper;

    /**
     * 新建交易
     * @param 交易信息
     */
    public void createTrade(TradeVo vo) {

        log.info("createTrade {}", vo.getTradeId());

        //先记交易日志
        TransactionPo transactionPo = transactionMapper.vo2Po(vo);
        transactionPo.setOperationType(TradeConstant.TRADE_OPERATION_INSERT);
        transactionService.logTransaction(transactionPo);

        //新建交易版本号必须为1
        if (vo.getVersion() != 1) {
            throw new BusinessException(ResponseCode.TRADE_VERSION_ERROR, "version must 1");
        }

        //通过行锁锁住仓位和交易
        PositionPo currentPosition = positionDao.findPositionByCode(vo.getSecurityCode());
        TradeStatusPo currentStatus = tradeStatusDao.findTradeStatusById(vo.getTradeId());

        TradeStatusPo newTradeStatusPo = tradeStatusMapper.vo2Po(vo);
        newTradeStatusPo.setOperationType(TradeConstant.TRADE_OPERATION_INSERT);
        //当前交易id若存在则不能创建
        if (currentStatus != null) {
            throw new BusinessException(ResponseCode.TRADE_STATUS_ERROR, "trade id exist");
        }
        tradeStatusDao.insertTradeStatus(newTradeStatusPo);

        PositionPo positionPo = new PositionPo();
        positionPo.setSecurityCode(vo.getSecurityCode());
        //更新仓位
        if (currentPosition != null) {
            if (vo.getTradeDirection() == TradeVo.TradeDirectionEnum.BUY) {
                positionPo.setQuantity(currentPosition.getQuantity() + vo.getQuantity());
            } else {
                positionPo.setQuantity(currentPosition.getQuantity() - vo.getQuantity());
            }
            positionDao.updatePosition(positionPo);
        } else {
            if (vo.getTradeDirection() == TradeVo.TradeDirectionEnum.BUY) {
                positionPo.setQuantity(vo.getQuantity());
            } else {
                positionPo.setQuantity(-vo.getQuantity());
            }
            positionDao.insertPosition(positionPo);
        }
    }

    /**
     *更新交易
     * @param 交易信息
     */
    public void updateTrade(TradeVo vo) {

        log.info("updateTrade {}", vo.getTradeId());

        //先记交易日志
        TransactionPo transactionPo = transactionMapper.vo2Po(vo);
        transactionPo.setOperationType(TradeConstant.TRADE_OPERATION_UPDATE);
        transactionService.logTransaction(transactionPo);

        //通过行锁锁住仓位和交易
        PositionPo currentPosition = positionDao.findPositionByCode(vo.getSecurityCode());
        TradeStatusPo currentTradeStatus = tradeStatusDao.findTradeStatusById(vo.getTradeId());

        TradeStatusPo newTradeStatus = tradeStatusMapper.vo2Po(vo);
        newTradeStatus.setOperationType(TradeConstant.TRADE_OPERATION_UPDATE);

        //当前交易id若不存在则不能更新
        if (currentTradeStatus == null) {
            throw new BusinessException(ResponseCode.TRADE_STATUS_ERROR, "trade id not exist");
        }
        //当前交易已取消则不能更新
        if (currentTradeStatus.getOperationType() == TradeConstant.TRADE_OPERATION_CANCEL) {
            throw new BusinessException(ResponseCode.TRADE_STATUS_ERROR, "trade is canceled");
        }
        //新交易版本号不能比当前的低
        if (newTradeStatus.getVersion() <= currentTradeStatus.getVersion()) {
            throw new BusinessException(ResponseCode.TRADE_VERSION_ERROR, "new version is lower than current");
        }
        //通过行锁锁住上次交易更新的仓位
        PositionPo lastUpdatePosition = positionDao.findPositionByCode(currentTradeStatus.getSecurityCode());

        //更新交易
        tradeStatusDao.updateTradeStatus(newTradeStatus);

        //更新仓位。先撤销上次更新的，
        if (currentTradeStatus.getDirectionType() == TradeConstant.TRADE_DIRECTION_BUY) {
            lastUpdatePosition.setQuantity(lastUpdatePosition.getQuantity() - currentTradeStatus.getQuantity());
        } else {
            lastUpdatePosition.setQuantity(lastUpdatePosition.getQuantity() + currentTradeStatus.getQuantity());
        }
        positionDao.updatePosition(lastUpdatePosition);

        //再更新现在的仓位
        if (lastUpdatePosition.getSecurityCode().equals(vo.getSecurityCode())) {
            currentPosition = lastUpdatePosition;
        }

        PositionPo newPosition = new PositionPo();
        newPosition.setSecurityCode(vo.getSecurityCode());
        if (currentPosition != null) {
            if (vo.getTradeDirection() == TradeVo.TradeDirectionEnum.BUY) {
                newPosition.setQuantity(currentPosition.getQuantity() + vo.getQuantity());
            } else {
                newPosition.setQuantity(currentPosition.getQuantity() - vo.getQuantity());
            }
            positionDao.updatePosition(newPosition);
        } else {
            if (vo.getTradeDirection() == TradeVo.TradeDirectionEnum.BUY) {
                newPosition.setQuantity(vo.getQuantity());
            } else {
                newPosition.setQuantity(-vo.getQuantity());
            }
            positionDao.insertPosition(newPosition);
        }

    }

    /**
     *撤销交易
     * @param 交易信息
     */
    public void cancelTrade(TradeVo vo) {
        log.info("cancelTrade {}", vo.getTradeId());

        //先记交易日志
        TransactionPo transactionPo = transactionMapper.vo2Po(vo);
        transactionPo.setOperationType(TradeConstant.TRADE_OPERATION_CANCEL);
        transactionService.logTransaction(transactionPo);

        //通过行锁锁住仓位和交易
        TradeStatusPo currentTradeStatus = tradeStatusDao.findTradeStatusById(vo.getTradeId());

        TradeStatusPo newTradeStatus = tradeStatusMapper.vo2Po(vo);
        newTradeStatus.setOperationType(TradeConstant.TRADE_OPERATION_CANCEL);

        //当前交易id若不存在则不能更新
        if (currentTradeStatus == null) {
            throw new BusinessException(ResponseCode.TRADE_STATUS_ERROR, "trade id not exist");
        }
        //当前交易已取消则不能更新
        if (currentTradeStatus.getOperationType() == TradeConstant.TRADE_OPERATION_CANCEL) {
            throw new BusinessException(ResponseCode.TRADE_STATUS_ERROR, "trade is canceled");
        }
        //新交易版本号不能比当前的低
        if (newTradeStatus.getVersion() <= currentTradeStatus.getVersion()) {
            throw new BusinessException(ResponseCode.TRADE_VERSION_ERROR, "new version is lower than current");
        }
        //通过行锁锁住上次交易更新的仓位
        PositionPo lastUpdatePosition = positionDao.findPositionByCode(currentTradeStatus.getSecurityCode());

        //更新交易
        tradeStatusDao.updateTradeStatus(newTradeStatus);

        //撤销上次更新的
        if (currentTradeStatus.getDirectionType() == TradeConstant.TRADE_DIRECTION_BUY) {
            lastUpdatePosition.setQuantity(lastUpdatePosition.getQuantity() - currentTradeStatus.getQuantity());
        } else {
            lastUpdatePosition.setQuantity(lastUpdatePosition.getQuantity() + currentTradeStatus.getQuantity());
        }
        positionDao.updatePosition(lastUpdatePosition);
    }
}
